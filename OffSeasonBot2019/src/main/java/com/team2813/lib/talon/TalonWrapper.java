package com.team2813.lib.talon;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonWrapper extends BaseMotorControllerWrapper<TalonSRX> {

	protected SensorCollection sensorCollection;

	public TalonWrapper(int deviceNumber, String subsystemName) {
		motorController = new TalonSRX(deviceNumber);
		this.subsystemName = subsystemName;
		sensorCollection = motorController.getSensorCollection();
	}
	
	public TalonWrapper(int deviceNumber) {
		this(deviceNumber, "");
	}

	public SensorCollection getSensorCollection() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getSensorCollection());
	}
	
	public void setCurrentLimit(boolean enable) throws CTREException {
		motorController.enableCurrentLimit(enable);
		throwLastError();
	}
	
	public void enableCurrentLimit() throws CTREException {
		setCurrentLimit(true);
	}
	
	public void disableCurrentLimit() throws CTREException {
		setCurrentLimit(false);
	}

	public void setStatusFramePeriod(StatusFrameEnhanced frame, int periodMs) throws CTREException {
		throwIfNotOk(motorController.setStatusFramePeriod(frame, periodMs, timeoutMode.value));
	}

	/**
	 * @param limit Amperes to limit
	 */
	public void setContinuousCurrentLimit(int limit) throws CTREException {
		throwIfNotOk(motorController.configContinuousCurrentLimit(limit, timeoutMode.value));
	}

	/**
	 * @param limit Amperes to limit
	 */
	public void setPeakCurrentLimit(int limit) throws CTREException {
		throwIfNotOk(motorController.configPeakCurrentLimit(limit, timeoutMode.value));
	}

	/**
	 * @param duration How long to allow current-draw past peak limit. (in milliseconds)
	 */
	public void setPeakCurrentDuration(int duration) throws CTREException {
		throwIfNotOk(motorController.configPeakCurrentDuration(duration, timeoutMode.value));
	}
	
	//#region Sensor collection wrappers

	public boolean isForwardLimitSwitchClosed() throws CTREException {
		return throwIfNotOkElseReturn(sensorCollection.isFwdLimitSwitchClosed());
	}
	
	public boolean isReverseLimitSwitchClosed() throws CTREException {
		return throwIfNotOkElseReturn(sensorCollection.isRevLimitSwitchClosed());
	}

	// TODO remaining ones

	//#endregion

	public void setReverseLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose) throws CTREException {
		throwIfNotOk(motorController.configReverseLimitSwitchSource(type, normalOpenOrClose, timeoutMode.value));
	}

	public void setLimitSwitchSource(LimitDirection direction, LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose) throws CTREException {
		if (direction == LimitDirection.FORWARD) {
			setForwardLimitSwitchSource(type, normalOpenOrClose);
		} else if (direction == LimitDirection.REVERSE) {
			setReverseLimitSwitchSource(type, normalOpenOrClose);
		}
	}
}
