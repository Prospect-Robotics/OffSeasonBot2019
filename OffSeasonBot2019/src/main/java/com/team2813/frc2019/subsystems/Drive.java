package com.team2813.frc2019.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import edu.wpi.first.wpilibj.Joystick;

public class Drive extends Subsystem {

	private static final CANSparkMaxWrapper LEFT = SubsystemMotorConfig.driveLeft;
	private static final CANSparkMaxWrapper RIGHT = SubsystemMotorConfig.driveRight;
	private double right_demand;
	private double left_demand;

	private static final double TELEOP_DEAD_ZONE = 0.01;
	private static final Joystick joystick = SubsystemControlsConfig.driveJoystick;
	private static final int X_AXIS = 0;
	private static final int Y_AXIS_POS = 1;
	private static final int Y_AXIS_NEG = 3;
	private static final boolean PIVOT = false;

	private static final TeleopDriveType TELEOP_DRIVE_TYPE = TeleopDriveType.CURVATURE;

	private boolean isBrakeMode;

	public enum TeleopDriveType {
		ARCADE, CURVATURE
	}

	public void teleopDrive(TeleopDriveType driveType) {
		if (driveType == TeleopDriveType.ARCADE) {
			arcadeDrive(joystick.getRawAxis(Y_AXIS_POS), joystick.getRawAxis(X_AXIS));
		} else if (driveType == TeleopDriveType.CURVATURE) {
			curvatureDrive(Y_AXIS_POS, Y_AXIS_NEG, X_AXIS, PIVOT);
		}
	}

	public void curvatureDrive(double throttleForward, double throttleBackward, double steerX, boolean pivot) {
		double throttle = throttleForward - throttleBackward;
		arcadeDrive(pivot ? steerX: throttle * steerX , throttle);
	}

	public void arcadeDrive(double x, double y) {
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

		System.out.println((throttleLeft - steer) + " " + (throttleLeft + steer));

		left_demand = throttleLeft - steer;
		right_demand = throttleRight + steer;
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
