package com.team2813.frc2019.subsystems;

import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import edu.wpi.first.wpilibj.Timer;

public class MainIntake extends Subsystem1d<MainIntake.Position> {

	private static CANSparkMaxWrapper wheelMotor = SubsystemMotorConfig.mainIntakeWheel;

	MainIntake() {
		super(SubsystemMotorConfig.mainIntakeWrist);
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

	private void startWheel(boolean forward) {
		wheelMotor.set(forward ? 1.0 : -1.0);
	}

	private void stopWheel() {
		wheelMotor.set(0.0);
	}

	private void moveWheelForTime(double seconds, boolean forward) {
		// TODO change this to use WaitAction
		final double END_TIME = Timer.getFPGATimestamp() + seconds;
		while (Timer.getFPGATimestamp() < END_TIME) {
			startWheel(forward);
		}
		stopWheel();
	}

	enum Position implements Subsystem1d.Position<MainIntake.Position> {
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
