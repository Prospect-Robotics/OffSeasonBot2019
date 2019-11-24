package com.team2813.frc2019.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ControlType;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Axis;
import com.team2813.lib.controls.Button;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * The Drive subsystem is the main subsystem for
 * the drive train, and handles both driver control
 * and autonomous path control.
 *
 * @author Grady Whelan
 * @author Samuel Li
 */
public class Drive extends Subsystem {

    // Physical Constants
    private static final double WHEEL_DIAMETER_INCHES = 1.0; // TODO: 10/05/2019 correct number
    private static final double WHEEL_CIRCUMFERENCE_INCHES = Math.PI * WHEEL_DIAMETER_INCHES;

    // Motor Controllers
    private static final CANSparkMaxWrapper LEFT = MotorConfigs.sparks.get("driveLeft");
    private static final CANSparkMaxWrapper RIGHT = MotorConfigs.sparks.get("driveRight");
    private double right_demand;
    private double left_demand;
    private boolean isBrakeMode;

    // Encoders
    private static final double ENCODER_TICKS_PER_REVOLUTION = 0.0; // TODO: 10/05/2019 replace with correct value
    private static final double ENCODER_TICKS_PER_INCH = ENCODER_TICKS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE_INCHES;
    private static final double ENCODER_TICKS_PER_FOOT = ENCODER_TICKS_PER_INCH / 12;

    // Controls
    private static final double TELEOP_DEAD_ZONE = 0.01;
    private static final Axis ARCADE_X_AXIS = SubsystemControlsConfig.driveX;
    private static final Axis ARCADE_Y_AXIS = SubsystemControlsConfig.driveY;
    private static final Axis CURVATURE_STEER = SubsystemControlsConfig.driveSteer;
    private static final Axis CURVATURE_FORWARD = SubsystemControlsConfig.driveForward;
    private static final Axis CURVATURE_REVERSE = SubsystemControlsConfig.driveReverse;
    private static final Button PIVOT_BUTTON = SubsystemControlsConfig.pivotButton;
    private static final TeleopDriveType TELEOP_DRIVE_TYPE = TeleopDriveType.CURVATURE;
    private static final Button AUTO_BUTTON = SubsystemControlsConfig.autoButton;

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

    public enum TeleopDriveType {
        ARCADE, CURVATURE
    }

    private static final double CORRECTION_MAX_STEER_SPEED = 0.5;
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tv = table.getEntry("tv");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry camtranEntry = table.getEntry("camtran");

    private void teleopDrive(TeleopDriveType driveType) {
        // PATH CORRECTION
        double[] camtran = camtranEntry.getDoubleArray(new double[]{0, 0, 0, 0, 0, 0});
        double x = camtran[0];
        double y = camtran[1];
        double pitch = camtran[4];

        double correctionSteer = 0;
        double difference = pitch - Math.atan(y / (x - 10.5));
        if (Math.abs(difference) > 0.5) {
            double rawSteer = difference / 27;
            correctionSteer = CORRECTION_MAX_STEER_SPEED * (Math.pow(rawSteer, 2) * (Math.abs(rawSteer) / rawSteer));
        }

        if (AUTO_BUTTON.get() && Subsystems.MAIN_INTAKE.periodicIO.demand != MainIntake.Position.FRONT_HATCH.getPos() && Subsystems.MAIN_INTAKE.periodicIO.demand != MainIntake.Position.REAR_HATCH.getPos()) {
            System.out.println("Correction Steer " + correctionSteer);
            curvatureDrive(CURVATURE_FORWARD.get(), CURVATURE_REVERSE.get(), correctionSteer, true);
        } else if (driveType == TeleopDriveType.ARCADE) {
            arcadeDrive(ARCADE_Y_AXIS.get(), ARCADE_X_AXIS.get());
        } else if (driveType == TeleopDriveType.CURVATURE) {
            curvatureDrive(CURVATURE_FORWARD.get(), CURVATURE_REVERSE.get(), CURVATURE_STEER.get(), PIVOT_BUTTON.get());
        }
    }

    private void curvatureDrive(double throttleForward, double throttleBackward, double steerX, boolean pivot) {
//		double throttle = Math.pow(throttleForward, 2) - Math.pow(throttleBackward, 2);
        double throttle =  2 * Math.asin(throttleForward - throttleBackward) / Math.PI;
//        double steer = Math.sin((Math.PI / 2) * steerX );
        double steer = 2 * Math.asin(steerX) / Math.PI;

        steer = -steer;
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
    protected void teleopControls_() throws CTREException, SparkMaxException {
        if (!isAuto) {
            driveMode = DriveMode.OPEN_LOOP;
            teleopDrive(TELEOP_DRIVE_TYPE);
        } else {
            driveMode = DriveMode.SMART_MOTION;
            AUTO_BUTTON.whenPressed(() -> {
                while (Math.abs(limelightDegrees) > ALLOWABLE_LIMELIGHT_ERROR)
                    autoDrive(limelightDegrees);
            });
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
