package com.team2813.frc2019.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import edu.wpi.first.wpilibj.Joystick;

public class Drive extends Subsystem {

	// Motor Controllers
	private static final CANSparkMaxWrapper LEFT = SubsystemMotorConfig.driveLeft;
	private static final CANSparkMaxWrapper RIGHT = SubsystemMotorConfig.driveRight;
	private double right_demand;
	private double left_demand;
	private boolean isBrakeMode;

	// Controls
	private static final double TELEOP_DEAD_ZONE = 0.01;
	private static final Joystick JOYSTICK = SubsystemControlsConfig.driveJoystick;
	private static final int X_AXIS = 0; // steer
	private static final int Y_AXIS_POS = 3; // arcade drive y axis; curvature drive forward
	private static final int Y_AXIS_NEG = 2; // curvature drive reverse
	private static final int PIVOT_BUTTON_ID = 1;
	private static boolean pivot = false; // for curvature drive
	private static final TeleopDriveType TELEOP_DRIVE_TYPE = TeleopDriveType.CURVATURE;

	public enum TeleopDriveType {
		ARCADE, CURVATURE
	}

	private void teleopDrive(TeleopDriveType driveType) {

		if (driveType == TeleopDriveType.ARCADE) {
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

	@Override
	protected boolean checkSystem_() throws CTREException {
		return false;
	}

	@Override
	protected void outputTelemetry_() throws CTREException {

	}

	@Override
	protected void teleopControls_() throws CTREException, SparkMaxException {
//		System.out.println(JOYSTICK);
		teleopDrive(TELEOP_DRIVE_TYPE);
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

	protected synchronized void writePeriodicOutputs_() {
		RIGHT.set(right_demand);
		LEFT.set(left_demand);
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
}
