package com.team2813.frc2019.subsystems;

import com.revrobotics.ControlType;
import com.team2813.lib.controls.Button;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;

public class Shooter extends Subsystem {
	private static final CANSparkMaxWrapper LEFT = SubsystemMotorConfig.turnLeftWheel;
	private static final CANSparkMaxWrapper RIGHT = SubsystemMotorConfig.turnRightWheel;
	private static final Button SHOOT = SubsystemControlsConfig.shoot;
	private static double demand = 0.0;
	private static final double SPEED = 0.5;

	@Override
	protected boolean checkSystem_() throws CTREException, SparkMaxException {
		return false;
	}

	@Override
	protected void writePeriodicOutputs_() throws CTREException, SparkMaxException {
		LEFT.set(demand, ControlType.kDutyCycle);
		RIGHT.set(demand, ControlType.kDutyCycle);
	}

	@Override
	protected void outputTelemetry_() throws CTREException, SparkMaxException {

	}

	@Override
	protected void teleopControls_() throws CTREException, SparkMaxException {
		if (SHOOT.getPressed()) {
			turnWheels();
		}

}

	@Override
	protected void onEnabledStart_(double timestamp) throws CTREException, SparkMaxException {

	}

	@Override
	protected void onEnabledLoop_(double timestamp) throws CTREException, SparkMaxException {

	}

	@Override
	protected void onEnabledStop_(double timestamp) throws CTREException, SparkMaxException {

	}
	private static void turnWheels() {
		demand = SPEED;
	}
}
