package com.team2813.frc2019.subsystems;

import com.team2813.lib.controls.Button;
import com.team2813.lib.solenoid.PistonSolenoid;
import com.team2813.lib.solenoid.PistonSolenoid.PistonState;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2019.subsystems.Subsystems.GROUND_INTAKE;
import static com.team2813.lib.solenoid.PistonSolenoid.PistonState.EXTENDED;
import static com.team2813.lib.solenoid.PistonSolenoid.PistonState.RETRACTED;

public class MainIntake extends Subsystem1d<MainIntake.Position> {

	static Position currentPosition = Position.HOME;

	private static PistonSolenoid solenoidDefaultOn = new PistonSolenoid(0);
	private static PistonSolenoid solenoidDefaultOff = new PistonSolenoid(1);

	static GamePiece mode = GamePiece.HATCH_PANEL;

	private static CANSparkMaxWrapper wheelMotor = SubsystemMotorConfig.mainIntakeWheel;

	private static final Button HOME = SubsystemControlsConfig.mainIntakeHome;
	private static final Button PLACE_FORWARD = SubsystemControlsConfig.mainIntakePlacePieceForward;
	private static final Button PLACE_REVERSE = SubsystemControlsConfig.mainIntakePlacePieceReverse;
	private static final Button CARGO_PICKUP = SubsystemControlsConfig.mainIntakeCargoPickup;
	private static final Button CARGO_ROCKET_HOLD = SubsystemControlsConfig.mainIntakeCargoRocketHold;

	private static final Button WHEEL_IN = SubsystemControlsConfig.mainIntakeWheelIn;
	private static final Button WHEEL_OUT = SubsystemControlsConfig.mainIntakeWheelOut;
	private static final Button TOGGLE_MODE = SubsystemControlsConfig.mainIntakeToggleMode;

	private static final double WHEEL_PERCENT = 1.0;

	MainIntake() {
		super(SubsystemMotorConfig.mainIntakeWrist);
		setNextPosition(Position.HOME);
	}

	@Override
	protected boolean checkSystem_() throws CTREException, SparkMaxException {
		return false;
	}

	@Override
	protected void outputTelemetry_() throws CTREException, SparkMaxException {
		SmartDashboard.putNumber("MainIntake Demand", periodicIO.demand);
		SmartDashboard.putNumber("MainIntake Position", periodicIO.positionTicks);
		SmartDashboard.putBoolean("Mode", mode == GamePiece.HATCH_PANEL);
	}

	@Override
	protected void teleopControls_() throws CTREException, SparkMaxException {
		WHEEL_OUT.whenPressed(() -> startWheelOut(true));
		WHEEL_IN.whenPressed(() -> startWheelOut(false));
		WHEEL_OUT.whenReleased(this::stopWheel);
		WHEEL_IN.whenReleased(this::stopWheel);
		TOGGLE_MODE.whenPressed(this::toggleMode);


		HOME.whenPressed(() -> setNextPosition(Position.HOME));
		PLACE_FORWARD.whenPressed(() -> setNextPosition(getPlacePosition(mode, true)));
		PLACE_REVERSE.whenPressed(() -> setNextPosition(getPlacePosition(mode, false)));
		if (mode == GamePiece.CARGO) CARGO_PICKUP.whenPressed(() -> setNextPosition(Position.PICKUP_CARGO));
//		INTAKE_CLOCK.whenPressed(() -> setNextPosition(true));
//		INTAKE_CLOCK.whenPressed(() -> System.out.println("Main intake button Setting test"));
//		INTAKE_COUNTER.whenPressed(() -> setNextPosition(false));
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

	private void startWheelOut(boolean out) {
		double wheelPercent = out ? WHEEL_PERCENT : -WHEEL_PERCENT;
		if (mode == GamePiece.CARGO) wheelPercent = -wheelPercent;
		wheelMotor.set(wheelPercent);
		try {
			Subsystems.GROUND_INTAKE.setRollerOut(out);
		} catch (CTREException e) {
			e.printStackTrace();
		}
	}

	private void stopWheel() {
		wheelMotor.set(0.0);
		try {
			Subsystems.GROUND_INTAKE.stopRoller();
		} catch (CTREException e) {
			e.printStackTrace();
		}
	}

	private Position getPlacePosition(GamePiece mode, boolean forward) {
		if (mode == GamePiece.CARGO) {
			if (forward) {
				if (CARGO_ROCKET_HOLD.get()) return Position.FRONT_CARGO_ROCKET;
				else return Position.FRONT_CARGO_SHIP;
			} else {
				if (CARGO_ROCKET_HOLD.get()) return Position.REAR_CARGO_ROCKET;
				else return Position.REAR_CARGO_SHIP;
			}
		} else {
			if (forward) return Position.FRONT_HATCH;
			else return Position.REAR_HATCH;
		}
	}

	@Override
	void setNextPosition(boolean clockwise) {
		currentPosition = currentPosition.getClock(clockwise);
		setPosition(currentPosition);
	}

	@Override
	void setNextPosition(Position position) {
		currentPosition = position;
		setPosition(currentPosition);
	}

	private void toggleMode() {
		if (GROUND_INTAKE.targetPosition == GroundIntake.Position.EXTENDED && mode == GamePiece.CARGO) return;
		setMode(mode.getOpposite());
	}

	public void setMode(GamePiece gamePiece) {
		mode = gamePiece;
		solenoidDefaultOn.set(mode.state == EXTENDED ? RETRACTED : EXTENDED);
		solenoidDefaultOff.set(mode.state);
	}
	enum Position implements Subsystem1d.Position<MainIntake.Position> {
		REAR_HATCH (-13.8) { // TODO: 11/01/2019 find correct value
			@Override
			public Position getNextClockwise() {
				return REAR_CARGO_SHIP;
			}
			@Override
			public Position getNextCounter() {
				return PICKUP_CARGO;
			}
		},
		REAR_CARGO_ROCKET(-9.0),
		REAR_CARGO_SHIP(-3.5) {
			@Override
			public Position getNextClockwise() {
				return HOME;
			}
			@Override
			public Position getNextCounter() {
				return REAR_HATCH;
			}
		},
		HOME(0.0) {
			@Override
			public Position getNextClockwise() {
				return FRONT_CARGO_SHIP;
			}

			@Override
			public Position getNextCounter() {
				return REAR_CARGO_SHIP;
			}
		},
		FRONT_CARGO_SHIP(3.5) {
			/*
			 * 8.3 is too far forward for the cargo ship
			 * 8.3 is too far back for the rocket
			 * 4.8 is too far back for the cargo ship
			 * 4.8 is too far back for the rocket
			 */
			@Override
			public Position getNextClockwise() {
				return FRONT_HATCH;
			}
			public Position getNextCounter() {
				return HOME;
			}
		},
		FRONT_CARGO_ROCKET(9.0),
		FRONT_HATCH(13.8) { // TODO: 11/01/2019 find correct value
			@Override
			public Position getNextClockwise() {
				return PICKUP_CARGO;
			}

			@Override
			public Position getNextCounter() {
				return FRONT_CARGO_SHIP;
			}
		},
		PICKUP_CARGO(18.8) {
			@Override
			public Position getNextClockwise() {
				return REAR_HATCH;
			}
			public Position getNextCounter() {
				return FRONT_HATCH;
			}
		};

		private final double position;

		Position(double encoderPosition) {
			this.position = encoderPosition;
		}

		@Override
		public double getPos() {
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
			return REAR_HATCH;
		}

		@Override
		public Position getMax() {
			return FRONT_HATCH;
		}
	}

	public enum GamePiece {
		HATCH_PANEL(RETRACTED) {
			@Override
			GamePiece getOpposite() {
				return GamePiece.CARGO;
			}
		}, CARGO(EXTENDED) {
			@Override
			GamePiece getOpposite() {
				return GamePiece.HATCH_PANEL;
			}
		};

		PistonState state;

		GamePiece(PistonState state) {
			this.state = state;
		}

		GamePiece getOpposite() {
			return this;
		}
	}
}
