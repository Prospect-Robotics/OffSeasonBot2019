package com.team2813.frc2019.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import edu.wpi.first.wpilibj.Timer;

import static com.team2813.lib.logging.LogLevel.DEBUG;

// TODO NEEDS TO BE DELETED
// ONLY FOR REFERENCE
abstract class Subsystem1dOld<P extends Subsystem1dOld.Position<P>> extends Subsystem {

	public CANSparkMaxWrapper motor;

	PeriodicIO periodicIO = new PeriodicIO();

	private boolean hasBeenZeroed = false;

	private ZeroingMode zeroingMode;

	Subsystem1dOld(CANSparkMaxWrapper motor) {
		try {
			this.motor = motor;
			motor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 10);
			// TODO check if we need something for the Smart Motion
			motor.set(0, ControlType.kDutyCycle);
			motor.setNeutralMode(CANSparkMax.IdleMode.kBrake);
		} catch (SparkMaxException e) {
			new SparkMaxException("Subsystem construction failed", e).printStackTrace();
			constructorFailed();
		}
	}

	private synchronized void setPosition(int encoderPosition) {
		periodicIO.demand = encoderPosition;
	}


	public synchronized void writePeriodicOutputs_() {
		try {
			double demand = periodicIO.demand;
			motor.set(demand, ControlType.kSmartMotion);
		} catch (SparkMaxException e) {
			e.printStackTrace();
		} try {
			resetIfAtLimit();
		} catch (SparkMaxException e) {
			e.printStackTrace();
		}
	}

	// TODO 2813 Rewrite more logically
	@Override
	public synchronized void readPeriodicInputs_() {
		final double t = Timer.getFPGATimestamp();
		periodicIO.position_ticks = motor.getEncoderPosition();

//			periodicIO.velocity_ticks_per_100ms = motor.gete getSelectedSensorVelocity(PidIdx.PRIMARY_CLOSED_LOOP);
//			if (motor.getControl() == ControlMode.MotionMagic) {
//				mPeriodicIO.active_trajectory_position = master.getActiveTrajectoryPosition();
//				// TODO check sign of elevator accel
//				mPeriodicIO.active_trajectory_velocity = master.getActiveTrajectoryVelocity();
//			} else {
//				mPeriodicIO.active_trajectory_position = Integer.MIN_VALUE;
//				mPeriodicIO.active_trajectory_velocity = 0;
//			}
		periodicIO.output_percent = motor.getAppliedOutput(); // TODO check this is what I think it is

		if (zeroingMode.forward)
			periodicIO.limit_switch = motor.isForwardLimitSwitchClosed();
		if (zeroingMode.reverse)
			periodicIO.limit_switch = motor.isReverseLimitSwitchClosed();
		periodicIO.t = t;
	}

	public synchronized void resetIfAtLimit() throws SparkMaxException {
		if (periodicIO.limit_switch) {
			zeroSensors_();
		}
	}

	@Override
	public synchronized void zeroSensors_() {

		try {
			motor.setEncoderPosition(0);
			DEBUG.log(motor.subsystemName, "zeroed 1", motor.getEncoderPosition());
		} catch (SparkMaxException e) {
			e.printStackTrace();
		}
		hasBeenZeroed = true;
	}

	public boolean isHasBeenZeroed() {
		return hasBeenZeroed;
	}

	public static class PeriodicIO {

		// INPUTS
		double position_ticks;

		int velocity_ticks_per_100ms;

		double active_trajectory_accel_g;

		int active_trajectory_velocity;

		int active_trajectory_position;

		double output_percent;

		boolean limit_switch;

		public double t;

		// OUTPUTS
		double demand;
	}

	// TODO document
	protected interface Position<E> {
		/** int encoder ticks */
		int getPos();

		E getNextClockwise();

		E getNextCounter();

		E getMin();

		E getMax();

		default E getClock(boolean clockwise) {
			return clockwise ? getNextClockwise() : getNextCounter();
		}
	}

	public enum ZeroingMode {
		FORWARD(true, false),
		REVERSE(false, true),
		BOTH(true, true),
		NEITHER(false, false);

		final boolean forward, reverse;

		ZeroingMode(boolean forward, boolean reverse) {
			this.forward = forward;
			this.reverse = reverse;
		}
	}


}
