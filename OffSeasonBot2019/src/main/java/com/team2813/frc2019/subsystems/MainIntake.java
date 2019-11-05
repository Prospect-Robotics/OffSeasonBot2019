package com.team2813.frc2019.subsystems;

import com.team2813.frc2019.actions.Action;
import com.team2813.frc2019.actions.FunctionAction;
import com.team2813.frc2019.actions.SeriesAction;
import com.team2813.frc2019.actions.WaitAction;
import com.team2813.lib.controls.Button;
import com.team2813.lib.solenoid.PistonSolenoid;
import com.team2813.lib.solenoid.PistonSolenoid.PistonState;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2019.subsystems.Subsystems.LOOPER;
import static com.team2813.lib.solenoid.PistonSolenoid.PistonState.EXTENDED;
import static com.team2813.lib.solenoid.PistonSolenoid.PistonState.RETRACTED;

public class MainIntake extends Subsystem1d<MainIntake.Position> {

	private static Position currentPosition = Position.HOME;

	private static PistonSolenoid solenoidDefaultOn = new PistonSolenoid(0);
	private static PistonSolenoid solenoidDefaultOff = new PistonSolenoid(1);

	private static GamePiece mode = GamePiece.HATCH_PANEL;

	private static CANSparkMaxWrapper wheelMotor = SubsystemMotorConfig.mainIntakeWheel;

	private static final Button INTAKE_CLOCK = SubsystemControlsConfig.mainIntakeClock;
	private static final Button INTAKE_COUNTER = SubsystemControlsConfig.mainIntakeCounter;
	private static final Button WHEEL_IN = SubsystemControlsConfig.mainIntakeWheelIn;
	private static final Button WHEEL_OUT = SubsystemControlsConfig.mainIntakeWheelOut;
	private static final Button TOGGLE_MODE = SubsystemControlsConfig.mainIntakeToggleMode;

	MainIntake() {
		super(SubsystemMotorConfig.mainIntakeWrist);
	}

	@Override
	protected boolean checkSystem_() throws CTREException, SparkMaxException {
		return false;
	}

	@Override
	protected void outputTelemetry_() throws CTREException, SparkMaxException {
		SmartDashboard.putNumber("MainIntake Demand", periodicIO.demand);
		SmartDashboard.putNumber("MainIntake Position", periodicIO.positionTicks);
	}

	@Override
	protected void teleopControls_() throws CTREException, SparkMaxException {
		WHEEL_OUT.whenPressed(() -> runWheelOut(true));
		WHEEL_IN.whenPressed(() -> runWheelOut(false));
		TOGGLE_MODE.whenPressed(this::toggleMode);
		INTAKE_CLOCK.whenPressed(() -> setNextPosition(true));
		INTAKE_COUNTER.whenPressed(() -> setNextPosition(false));
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

	private void runWheelOut(boolean out) {
		Action action = new SeriesAction(
			new FunctionAction(() -> startWheelOut(out), true),
			new WaitAction(1.0),// TODO: 11/01/2019 tune
			new FunctionAction(this::stopWheel, true)
		);
		LOOPER.addAction(action);
	}

	private void startWheelOut(boolean out) {
		wheelMotor.set(out ? 1.0 : -1.0);
	}

	private void stopWheel() {
		wheelMotor.set(0.0);
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
		setMode(mode.getOpposite());
	}

	public void setMode(GamePiece gamePiece) {
		mode = gamePiece;
		solenoidDefaultOn.set(mode.state == EXTENDED ? RETRACTED : EXTENDED);
		solenoidDefaultOff.set(mode.state);
	}
	enum Position implements Subsystem1d.Position<MainIntake.Position> {
		REAR (-1) { // TODO: 11/01/2019 find correct value
			@Override
			public Position getNextClockwise() {
				return HOME;
			}
			@Override
			public Position getNextCounter() {
				return FRONT;
			}
		},
		HOME(0) {
			@Override
			public Position getNextClockwise() {
				return FRONT;
			}

			@Override
			public Position getNextCounter() {
				return REAR;
			}
		}, FRONT(1) { // TODO: 11/01/2019 find correct value
			@Override
			public Position getNextClockwise() {
				return REAR;
			}

			@Override
			public Position getNextCounter() {
				return HOME;
			}
		};

		private final int position;

		Position(int encoderPosition) {
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
			return REAR;
		}

		@Override
		public Position getMax() {
			return FRONT;
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
