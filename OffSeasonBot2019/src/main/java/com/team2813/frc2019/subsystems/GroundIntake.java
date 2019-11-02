package com.team2813.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2813.lib.controls.Button;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import com.team2813.lib.talon.VictorWrapper;

public class GroundIntake extends Subsystem1d<GroundIntake.Position> {

	private Position currentPosition = Position.RETRACTED;

	private static final Button ROLLER_OUT = SubsystemControlsConfig.groundIntakeRollerOut;
	private static final Button ROLLER_IN = SubsystemControlsConfig.groundIntakeRollerIn;
	private static final Button TOGGLE_POSITION = SubsystemControlsConfig.groundIntakeTogglePosition;

	private static final VictorWrapper ROLLER = SubsystemMotorConfig.groundIntakeRoller;

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
		TOGGLE_POSITION.whenPressed(this::togglePosition);
		ROLLER_OUT.whenPressed(() -> {
			try {
				setRollerOut(true);
			} catch (CTREException e) {
				e.printStackTrace();
			}
		});
		ROLLER_IN.whenPressed(() -> {
			try {
				setRollerOut(false);
			} catch (CTREException e) {
				e.printStackTrace();
			}
		});
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

	private void setRollerOut(boolean out) throws CTREException {
		double percent = out ? 1.0 : -1.0;
		ROLLER.set(ControlMode.PercentOutput, percent);
	}

	private void togglePosition() {
		setNextPosition(true);
	}

	@Override
	void setNextPosition(boolean clockwise) {
		currentPosition = currentPosition.getClock(clockwise);
		setPosition(currentPosition);
	}

	void setNextPosition(Position position) {
		currentPosition = position;
		setPosition(position);
	}

	enum Position implements Subsystem1d.Position<GroundIntake.Position> {
		RETRACTED(0) {
			@Override
			public Position getNextClockwise() {
				return EXTENDED;
			}

			@Override
			public Position getNextCounter() {
				return EXTENDED;
			}
		}, EXTENDED(1000) { // TODO: 11/01/2019 find correct value
			@Override
			public Position getNextClockwise() {
				return RETRACTED;
			}

			@Override
			public Position getNextCounter() {
				return RETRACTED;
			}
		};

		private final int position;

		Position(int encoderPosition) {
			this.position = encoderPosition;
		}

		@Override
		public int getPos() {
			return position;
		}

		@Override
		public Position getNextClockwise() {
			return this;
		}

		@Override
		public Position getNextCounter() {
			return this;
		}

		@Override
		public Position getMin() {
			return RETRACTED;
		}

		@Override
		public Position getMax() {
			return EXTENDED;
		}
	}
}
