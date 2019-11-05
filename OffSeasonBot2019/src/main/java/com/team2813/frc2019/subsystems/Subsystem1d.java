package com.team2813.frc2019.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import edu.wpi.first.wpilibj.Timer;

import static com.team2813.lib.logging.LogLevel.DEBUG;

abstract class Subsystem1d<P extends Subsystem1d.Position> extends Subsystem {

	private CANSparkMaxWrapper motor;
	PeriodicIO periodicIO = new PeriodicIO();
	private boolean zeroed = false;

	Subsystem1d(CANSparkMaxWrapper motor) {
		try {
			this.motor = motor;
			motor.setPeriodicFrame(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 10);
			motor.set(0, ControlType.kDutyCycle);
			motor.setNeutralMode(CANSparkMax.IdleMode.kBrake);
		} catch (SparkMaxException e) {
			new SparkMaxException("Subsystem construction failed", e).printStackTrace();
			e.printStackTrace();
		}
	}

	@Override
	protected void writePeriodicOutputs_() {
		try {
			resetIfAtLimit();
			motor.set(periodicIO.demand, ControlType.kSmartMotion);
		} catch(SparkMaxException e) {
			new SparkMaxException("Subsystem initialization failed", e).printStackTrace();
		}
	}

	@Override
	public synchronized void readPeriodicInputs_() {
		final double t = Timer.getFPGATimestamp();
		periodicIO.positionTicks = motor.getEncoderPosition();
	}

	public synchronized void resetIfAtLimit() throws SparkMaxException {
		if (periodicIO.limitSwitch) {
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
		zeroed = true;
	}

	public boolean isZeroed() {
		return zeroed;
	}

	static class PeriodicIO { // FIXME: 11/02/2019 this probably shouldn't be static
		double demand;

		boolean limitSwitch;

		double positionTicks;
	}

	/*==========================
	* POSITION
	* ==========================*/

	protected interface Position<E> {
		/** int encoder ticks */
		double getPos();

		E getNextClockwise();

		E getNextCounter();

		E getMin();

		E getMax();

		default E getClock(boolean clockwise) {
			return clockwise ? getNextClockwise() : getNextCounter();
		}
	}

	private synchronized void setPosition(double encoderPosition) {
		periodicIO.demand = encoderPosition;
	}

	synchronized void setPosition(P position) {
		setPosition(position.getPos());
	}

	abstract void setNextPosition(boolean clockwise);

	abstract void setNextPosition(P position);
}
