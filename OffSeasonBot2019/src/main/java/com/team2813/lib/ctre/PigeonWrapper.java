package com.team2813.lib.ctre;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class PigeonWrapper {

	String subsystemName;
	PigeonIMU pigeon;

	public PigeonWrapper(int deviceNumber, String subsystemName) {
		pigeon = new PigeonIMU(deviceNumber);
		this.subsystemName = subsystemName;
	}

	public PigeonWrapper(TalonWrapper talon) {
		pigeon = new PigeonIMU(talon.motorController);
		subsystemName = talon.subsystemName;
	}

	protected void throwIfNotOk(ErrorCode error) throws CTREException {
		CTREException.throwIfNotOk(subsystemName, error);
	}

	public void DestroyObject() throws CTREException {
		throwIfNotOk(pigeon.DestroyObject());
	}

	public void setYaw(double angleDeg) throws CTREException {
		throwIfNotOk(pigeon.setYaw(angleDeg, TimeoutMode.RUNNING.value));
	}

	public void addYaw(double angleDeg) throws CTREException {
		throwIfNotOk(pigeon.addYaw(angleDeg, TimeoutMode.RUNNING.value));
	}

	public void setYawToCompass() throws CTREException {
		throwIfNotOk(pigeon.setYawToCompass(TimeoutMode.NO_TIMEOUT.value));
	}

	

	// TODO take documentation from BaseMotorControllerWrapper
	public enum TimeoutMode {
		/** Longer timeout, used for constructors */
		CONSTRUCTING(100),
		/** Shorter timeout, used for on the fly updates */
		RUNNING(20),
		NO_TIMEOUT(0);

		final int value;

		private TimeoutMode(int value) {
			this.value = value;
		}
	}



}
