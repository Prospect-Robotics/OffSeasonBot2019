package com.team2813.frc2019.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ControlType;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Subsystem {

    // Physical Constants
    private static final double WHEEL_DIAMETER_INCHES = 1.0; // TODO: 10/05/2019 correct number
    private static final double WHEEL_CIRCUMFERENCE_INCHES = Math.PI * WHEEL_DIAMETER_INCHES;

    // Motor Controllers
    private static final CANSparkMaxWrapper LEFT = SubsystemMotorConfig.driveLeft;
    private static final CANSparkMaxWrapper RIGHT = SubsystemMotorConfig.driveRight;
    private double right_demand;
    private double left_demand;
    private boolean isBrakeMode;

    // Encoders
    private static final double ENCODER_TICKS_PER_REVOLUTION = 0.0; // TODO: 10/05/2019 replace with correct value
    private static final double ENCODER_TICKS_PER_INCH = ENCODER_TICKS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE_INCHES;
    private static final double ENCODER_TICKS_PER_FOOT = ENCODER_TICKS_PER_INCH / 12;

    // Controls
    private static final double TELEOP_DEAD_ZONE = 0.01;
    private static final Joystick JOYSTICK = SubsystemControlsConfig.driveJoystick;
    private static final int X_AXIS = 0; // steer
    private static final int Y_AXIS_POS = 3; // arcade drive y axis; curvature drive forward
    private static final int Y_AXIS_NEG = 2; // curvature drive reverse
    private static final int PIVOT_BUTTON_ID = 1;
    private static final TeleopDriveType TELEOP_DRIVE_TYPE = TeleopDriveType.CURVATURE;
    private static final int AUTO_BUTTON_ID = 2; // TODO: 10/05/2019 replace with correct button id

    // Mode
    private static DriveMode driveMode = DriveMode.OPEN_LOOP;

    // Auto
    private static final double ENCODER_TICKS_PER_DEGREE_TANK_TURN = 0.0; // TODO: 10/05/2019 need to find correct value using robot
    private static boolean isAuto = false;
    private static double limelightDegrees = 0.0; // TODO: 10/05/2019 replace with actual Limelight angle
    private static final double ALLOWABLE_LIMELIGHT_ERROR = 0.0; // TODO: 10/05/2019 replace with actual allowable angle error
    private static final double MIN_AUTO_POS_CHANGE = 0.0; // TODO: 10/05/2019 tune
    //	private static final double MIN_AUTO_SPEED_FPS = 0.33; // TODO: 10/05/2019 tune
//	private static final double MIN_AUTO_SPEED_ENCODER_TICKS = MIN_AUTO_SPEED_FPS * ENCODER_TICKS_PER_FOOT;
	private static final double CORRECTION_MAX_STEER_SPEED = 0.5;
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry x = table.getEntry("tx");
    NetworkTableEntry y = table.getEntry("ty");

    public enum TeleopDriveType {
        ARCADE, CURVATURE
    }

    private void teleopDrive(TeleopDriveType driveType) {
        // PATH CORRECTION
		double correctionSteer = 0;
		if (Math.abs(x.getDouble(0)) > 1) {
			System.out.println(x.getDouble(0));
			double rawSteer = x.getDouble(0) / 27;
			correctionSteer = CORRECTION_MAX_STEER_SPEED * (rawSteer + .3);
		}

		if (JOYSTICK.getRawButton(AUTO_BUTTON_ID)) {
			curvatureDrive(JOYSTICK.getRawAxis(Y_AXIS_POS), JOYSTICK.getRawAxis(Y_AXIS_NEG), correctionSteer, true);
		} else if (driveType == TeleopDriveType.ARCADE) {
            arcadeDrive(JOYSTICK.getRawAxis(Y_AXIS_POS), JOYSTICK.getRawAxis(X_AXIS));
        } else if (driveType == TeleopDriveType.CURVATURE) {
            curvatureDrive(JOYSTICK.getRawAxis(Y_AXIS_POS), JOYSTICK.getRawAxis(Y_AXIS_NEG), JOYSTICK.getRawAxis(X_AXIS), JOYSTICK.getRawButton(PIVOT_BUTTON_ID));
        }
    }

    private void curvatureDrive(double throttleForward, double throttleBackward, double steerX, boolean pivot) {
        double throttle = Math.pow(throttleForward, 2) - Math.pow(throttleBackward, 2);
//        double steer = Math.sin((Math.PI / 2) * steerX );
        double steer = Math.abs(steerX) * steerX;
        arcadeDrive(pivot ? steer * .4 : throttle * steer, throttle);
    }

    private void arcadeDrive(double x, double y) {
        double maxPercent = 1.0;
        double throttleLeft = 0;
        double throttleRight = 0;

        double steer = 0;

        if (Math.abs(y) > TELEOP_DEAD_ZONE) { // dead zone
            throttleLeft = maxPercent * y;
            throttleRight = maxPercent * y;
        }

        if (Math.abs(x) > TELEOP_DEAD_ZONE) {
            double xMax = 0.4;
            steer = 1.0 * x;
        }
//
//        System.out.println(throttleLeft + " " + throttleRight);

//        System.out.println((throttleLeft - steer) + " " + (throttleLeft + steer));

        left_demand = throttleLeft + steer;
        right_demand = throttleRight - steer;
    }

    private void autoDrive(double angle) {
        // If turning right, left moves forward and right moves backward
        // If turning left, right moves forward and left moves backward
        // TODO: 10/05/2019 I'm not sure this is right. I think there might be a better way to do it using trig.
        left_demand = MIN_AUTO_POS_CHANGE + LEFT.getEncoderPosition() + (angle * ENCODER_TICKS_PER_DEGREE_TANK_TURN);
        right_demand = MIN_AUTO_POS_CHANGE + RIGHT.getEncoderPosition() - (angle * ENCODER_TICKS_PER_DEGREE_TANK_TURN);
    }

    @Override
    protected boolean checkSystem_() throws CTREException {
        return false;
    }

    @Override
    protected void outputTelemetry_() throws CTREException {

    }

    @Override
    public void teleopControls_() throws CTREException, SparkMaxException {
        if (!isAuto) {
            driveMode = DriveMode.OPEN_LOOP;
            teleopDrive(TELEOP_DRIVE_TYPE);
        } else {
            driveMode = DriveMode.SMART_MOTION;
            if (JOYSTICK.getRawButtonPressed(AUTO_BUTTON_ID))
                while (Math.abs(limelightDegrees) > ALLOWABLE_LIMELIGHT_ERROR) {
                    autoDrive(limelightDegrees);
                }
        }
    }

    @Override
    protected void onEnabledStart_(double timestamp) throws CTREException {
//		setBrakeMode(false);

    }

    @Override
    protected void onEnabledLoop_(double timestamp) throws CTREException {
    }

    @Override
    protected void onEnabledStop_(double timestamp) throws CTREException {
    }

    protected synchronized void writePeriodicOutputs_() throws SparkMaxException {
        RIGHT.set(right_demand, driveMode.controlType);
        LEFT.set(left_demand, driveMode.controlType);
    }

    public synchronized void setBrakeMode(boolean brake) {
        if (isBrakeMode != brake) {
            isBrakeMode = brake;
            IdleMode mode = brake ? IdleMode.kBrake : IdleMode.kCoast;
            try {
                RIGHT.setNeutralMode(mode);
                LEFT.setNeutralMode(mode);
            } catch (SparkMaxException e) {
                e.printStackTrace();
            }
        }
    }

    private enum DriveMode {
        OPEN_LOOP(ControlType.kDutyCycle),
        SMART_MOTION(ControlType.kSmartMotion),
        VELOCITY(ControlType.kVelocity);

        ControlType controlType;

        DriveMode(ControlType controlType) {
            this.controlType = controlType;
        }
    }
}
