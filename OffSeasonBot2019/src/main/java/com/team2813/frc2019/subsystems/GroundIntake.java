package com.team2813.frc2019.subsystems;

import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;

public class GroundIntake extends Subsystem1d<GroundIntake.Position> {
	GroundIntake() {
		super(SubsystemMotorConfig.groundIntakeArm);
	}

	@Override
	protected boolean checkSystem_() throws CTREException, SparkMaxException {
		return false;
	}

	@Override
	protected void outputTelemetry_() throws CTREException, SparkMaxException {

	}

	@Override
	protected void teleopControls_() throws CTREException, SparkMaxException {

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

	enum Position implements Subsystem1d.Position<GroundIntake.Position> {
		;

		@Override
		public int getPos() {
			return 0;
		}

		@Override
		public Position getNextClockwise() {
			return null;
		}

		@Override
		public Position getNextCounter() {
			return null;
		}

		@Override
		public Position getMin() {
			return null;
		}

		@Override
		public Position getMax() {
			return null;
		}
	}
}
