package com.team2813.lib.ctre;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

/**
 * @author Adrian Guerra
 */
public abstract class BaseMotorControllerWrapper<Controller extends BaseMotorController> {
	
	public TimeoutMode timeoutMode = TimeoutMode.CONSTRUCTING;

	public Controller motorController;
	
	public String subsystemName;
	
	protected InvertType invertType = InvertType.None;
	
	// TODO add methods to push and pop timeout modes?
	// TODO custom timeout modes?
	
	/**
	 * Helper function for handling talon methods that do not return an error code but still need to check for one
	 * 
	 * <p>from TalonSRX.getLastError():</p>
	 * <blockquote>
	 * Gets the last error generated by this object. Not all functions return an
	 * error code but can potentially report errors. This function can be used
	 * to retrieve those error codes.
	 * </blockquote>
	 * 
	 * @param value - value to return
	 * @return value
	 * @throws CTREException - if talon had error code
	 */
	protected <T> T throwIfNotOkElseReturn(T value) throws CTREException {
		throwLastError();
		return value;
	}
	
	protected void throwIfNotOk(ErrorCode error) throws CTREException {
		CTREException.throwIfNotOk(subsystemName, error);
	}
	
	// #region TalonSRX
	
	// #endregion
	

	public long getHandle() {
		return motorController.getHandle(); // doesn't need error check
	}


	public int getDeviceID() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getDeviceID());
	}

	// #region Set output routines

	public void set(ControlMode mode, double demand) throws CTREException {
		motorController.set(mode, demand);
		throwLastError();
	}

	public void set(ControlMode mode, double demand0, DemandType demand1Type, double demand1) throws CTREException {
		motorController.set(mode, demand0, demand1Type, demand1);
		throwLastError();
	}


	public void neutralOutput() throws CTREException {
		motorController.neutralOutput();
		throwLastError();
	}

	public void setNeutralMode(NeutralMode mode) throws CTREException {
		motorController.setNeutralMode(mode);
		throwLastError();
	}

	// didn't implement selectDemandType b/c it does nothing

	// #endregion

	// #region Invert behavior


	/**
	 * Invert the encoder
	 * @param inverted
	 * @throws CTREException
	 */
	public void setSensorPhaseInverted(boolean inverted) throws CTREException {
		motorController.setSensorPhase(inverted);
		throwLastError();
	}

	/**
	 * @throws CTREException
	 * @see BaseMotorController#setInverted(InvertType)
	 * @see InvertType
	 */
	public void setInverted(InvertType type) throws CTREException {
		invertType = type;
		motorController.setInverted(type);
		throwLastError();
	}

	public boolean getInverted() {
		return motorController.getInverted();
	}
	
	public InvertType getInvertType() {
		return invertType;
	}

	// #endregion

	// #region Factory Default Configuration

	public void setFactoryDefaults() throws CTREException {
		throwIfNotOk(motorController.configFactoryDefault(timeoutMode.valueMs));
	}

	// #endregion

	// #region general output shaping

	public void setOpenLoopRamp(double seconds) throws CTREException {
		throwIfNotOk(motorController.configOpenloopRamp(seconds, timeoutMode.valueMs));
	}

	public void setClosedLoopRamp(double seconds) throws CTREException {
		throwIfNotOk(motorController.configClosedloopRamp(seconds, timeoutMode.valueMs));
	}


	public void setPeakOutputForward(double percentOut) throws CTREException {
		throwIfNotOk(motorController.configPeakOutputForward(percentOut, timeoutMode.valueMs));
	}


	public void setPeakOutputReverse(double percentOut) throws CTREException {
		throwIfNotOk(motorController.configPeakOutputReverse(percentOut, timeoutMode.valueMs));
	}


	public void setNominalOutputForward(double percentOut) throws CTREException {
		throwIfNotOk(motorController.configNominalOutputForward(percentOut, timeoutMode.valueMs));
	}


	public void setNominalOutputReverse(double percentOut) throws CTREException {
		throwIfNotOk(motorController.configNominalOutputReverse(percentOut, timeoutMode.valueMs));
	}

	public void setNeutralDeadband(double percentDeadband) throws CTREException {
		throwIfNotOk(motorController.configNeutralDeadband(percentDeadband, timeoutMode.valueMs));
	}

	// #endregion

	// #region Voltage Compensation


	public void setVoltageCompensationSaturation(double voltage) throws CTREException {
		throwIfNotOk(motorController.configVoltageCompSaturation(voltage, timeoutMode.valueMs));
	}


	public void setVoltageMeasurementFilter(int filterWindowSamples) throws CTREException {
		throwIfNotOk(motorController.configVoltageMeasurementFilter(filterWindowSamples, timeoutMode.valueMs));
	}


	public void setVoltageCompensationEnabled(boolean enable) throws CTREException {
		motorController.enableVoltageCompensation(enable);
		throwLastError();
	}

	public void enableVoltageCompensation() throws CTREException {
		setVoltageCompensationEnabled(true);
	}

	public void disableVoltageCompensation() throws CTREException {
		setVoltageCompensationEnabled(false);
	}

	// #endregion

	// #region General Status
	

	public double getBusVoltage() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getBusVoltage());
	}
	

	public double getMotorOutputPercent() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getMotorOutputPercent());
	}
	

	public double getMotorOutputVoltage() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getMotorOutputVoltage());
	}
	

	public double getTemperature() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getTemperature());
	}
	
	// #endregion

	// #region Sensor Selection	

	public void setSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice, PidIdx pidIdx) throws CTREException {
		throwIfNotOk(motorController.configSelectedFeedbackSensor(feedbackDevice, pidIdx.value, timeoutMode.valueMs));
	}
	

	public void setSelectedFeedbackSensor(FeedbackDevice feedbackDevice, PidIdx pidIdx) throws CTREException {
		throwIfNotOk(motorController.configSelectedFeedbackSensor(feedbackDevice, pidIdx.value, timeoutMode.valueMs));
	}
	
	public void setSelectedFeedbackCoefficient(double coefficient, PidIdx pidIdx) throws CTREException {
		throwIfNotOk(motorController.configSelectedFeedbackCoefficient(coefficient, pidIdx.value, timeoutMode.valueMs));
	}
	

	public void setRemoteFeedbackFilter(int deviceID, RemoteSensorSource remoteSensorSource, int remoteOrdinal) throws CTREException {
		throwIfNotOk(motorController.configRemoteFeedbackFilter(deviceID, remoteSensorSource, remoteOrdinal, timeoutMode.valueMs));
	}


	/**
	 * Select what sensor term should be bound to switch feedback device.
	 * Sensor Sum = Sensor Sum Term 0 - Sensor Sum Term 1
	 * Sensor Difference = Sensor Diff Term 0 - Sensor Diff Term 1
	 * The four terms are specified with this routine.  Then Sensor Sum/Difference
	 * can be selected for closed-looping.
	 *
	 * @param sensorTerm Which sensor term to bind to a feedback source.
	 * @param feedbackDevice The sensor signal to attach to sensorTerm.
	 */
	public void setSensorTerm(SensorTerm sensorTerm, FeedbackDevice feedbackDevice) throws CTREException {
		throwIfNotOk(motorController.configSensorTerm(sensorTerm, feedbackDevice, timeoutMode.valueMs));
	}


	/**
	 * Select what sensor term should be bound to switch feedback device.
	 * Sensor Sum = Sensor Sum Term 0 - Sensor Sum Term 1
	 * Sensor Difference = Sensor Diff Term 0 - Sensor Diff Term 1
	 * The four terms are specified with this routine.  Then Sensor Sum/Difference
	 * can be selected for closed-looping.
	 *
	 * @param sensorTerm Which sensor term to bind to a feedback source.
	 * @param feedbackDevice The sensor signal to attach to sensorTerm.
	 */
	public void setSensorTerm(SensorTerm sensorTerm, RemoteFeedbackDevice feedbackDevice) throws CTREException {
		throwIfNotOk(motorController.configSensorTerm(sensorTerm, feedbackDevice, timeoutMode.valueMs));
	}
	
	// #endregion

	// #region Sensor Status

	public int getSelectedSensorPosition(PidIdx pidIdx) throws CTREException {
		return throwIfNotOkElseReturn(motorController.getSelectedSensorPosition(pidIdx.value));
	}
	
	public int getSelectedSensorPosition() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getSelectedSensorPosition());
	}
	
	public int getSelectedSensorVelocity(PidIdx pidIdx) throws CTREException {
		return throwIfNotOkElseReturn(motorController.getSelectedSensorVelocity(pidIdx.value));
	}
	
	public int getSelectedSensorVelocity() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getSelectedSensorVelocity());
	}
	
	public void setSelectedSensorPosition(PidIdx pidIdx, int sensorPos) throws CTREException {
		throwIfNotOk(motorController.setSelectedSensorPosition(sensorPos, pidIdx.value, timeoutMode.valueMs));
	}
	
	public void setSelectedSensorPosition(int sensorPos) throws CTREException {
		setSelectedSensorPosition(PidIdx.PRIMARY_CLOSED_LOOP, sensorPos);
	}
	
	// #endregion

	// #region Status Frame Period Changes
	

	public void setControlFramePeriod(ControlFrame frame, int periodMs) throws CTREException {
		throwIfNotOk(motorController.setControlFramePeriod(frame, periodMs));
	}
	

	public void setControlFramePeriod(int frame, int periodMs) throws CTREException {
		throwIfNotOk(motorController.setControlFramePeriod(frame, periodMs));
	}
	
	public void setStatusFramePeriod(int frameValue, int periodMs) throws CTREException {
		throwIfNotOk(motorController.setStatusFramePeriod(frameValue, periodMs, timeoutMode.valueMs));
	}
	
	public void setStatusFramePeriod(StatusFrame frame, int periodMs) throws CTREException {
		throwIfNotOk(motorController.setStatusFramePeriod(frame, periodMs, timeoutMode.valueMs));
	}
	
	public int getStatusFramePeriod(int frameValue) throws CTREException {
		return throwIfNotOkElseReturn(motorController.getStatusFramePeriod(frameValue, timeoutMode.valueMs));
	}
	
	public int getStatusFramePeriod(StatusFrame frame) throws CTREException {
		return throwIfNotOkElseReturn(motorController.getStatusFramePeriod(frame, timeoutMode.valueMs));
	}
	
	public int getStatusFramePeriod(StatusFrameEnhanced frame) throws CTREException {
		return throwIfNotOkElseReturn(motorController.getStatusFramePeriod(frame, timeoutMode.valueMs));
	}
	
	// #endregion

	// #region Velocity Signal Conditioning
	

	public void setVelocityMeasurementPeriod(VelocityMeasPeriod period) throws CTREException {
		throwIfNotOk(motorController.configVelocityMeasurementPeriod(period, timeoutMode.valueMs));
	}
	

	public void setVelocityMeasurementWindow(VelocityMeasurementWindow windowSize) throws CTREException {
		throwIfNotOk(motorController.configVelocityMeasurementWindow(windowSize.value, timeoutMode.valueMs));
	}
	
	// #endregion

	// #region Remote Limit Switch

	public void setForwardLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int deviceID) throws CTREException {
		throwIfNotOk(
			motorController.configForwardLimitSwitchSource(type, normalOpenOrClose, deviceID, timeoutMode.valueMs)
		);
	}
	
	public void setReverseLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int deviceID) throws CTREException {
		throwIfNotOk(
			motorController.configReverseLimitSwitchSource(type, normalOpenOrClose, deviceID, timeoutMode.valueMs)
		);
	}
	

	public void setForwardLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose) throws CTREException {
		throwIfNotOk(motorController.configForwardLimitSwitchSource(type, normalOpenOrClose, timeoutMode.valueMs));
	}
	
	// #endregion

	// #region Forward soft limit
	
	public void setForwardSoftLimitThreshold(int threshold) throws CTREException {
		throwIfNotOk(motorController.configForwardSoftLimitThreshold(threshold, timeoutMode.valueMs));
	}

	public void setForwardSoftLimitEnable(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configForwardSoftLimitEnable(enable, timeoutMode.valueMs));
	}

	public void disableForwardSoftLimit() throws CTREException {
		setForwardSoftLimitEnable(false);
	}

	public void enableForwardSoftLimit() throws CTREException {
		setForwardSoftLimitEnable(true);
	}

	public void setForwardSoftLimit(int threshold, boolean enable) throws CTREException {
		setForwardSoftLimitThreshold(threshold);
		setForwardSoftLimitEnable(enable);
	}

	public void setSoftLimit(LimitDirection direction, int threshold, boolean enable) throws CTREException {
		if (direction == LimitDirection.FORWARD) setForwardSoftLimit(threshold, enable);
		else if (direction == LimitDirection.REVERSE) setReverseSoftLimit(threshold, enable);
	}

	// #endregion
	
	// #region Reverse soft limit
	
	/**
	 * @param threshold Reverse Sensor Position Limit (in raw sensor units).
	 */
	public void setReverseSoftLimitThreshold(int threshold) throws CTREException {
		throwIfNotOk(motorController.configReverseSoftLimitThreshold(threshold, timeoutMode.valueMs));
	}


	public void setReverseSoftLimitEnable(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configReverseSoftLimitEnable(enable, timeoutMode.valueMs));
	}

	public void disableReverseSoftLimit() throws CTREException {
		setReverseSoftLimitEnable(false);
	}

	public void enableReverseSoftLimit() throws CTREException {
		setReverseSoftLimitEnable(true);
	}

	/**
	 * @param threshold Reverse Sensor Position Limit (in raw sensor units).
	 */
	public void setReverseSoftLimit(int threshold, boolean enable) throws CTREException {
		setReverseSoftLimitThreshold(threshold);
		setReverseSoftLimitEnable(enable);
	}

	// #endregion
	
	//
	// General Closed Loop
	//
	
	//TODO
	
	// #endregion

	// #region Motion Profile Settings used in Motion Magic and Motion Profile
	
	/**
	 * Set the peak velocity in Motion Magic mode
	 * @param velocity - sensor units / 100ms
	 * @throws CTREException
	 */
	public void setMotionMagicCruiseVelocity(int velocity) throws CTREException {
		throwIfNotOk(motorController.configMotionCruiseVelocity(velocity, timeoutMode.valueMs));
	}
	
	/**
	 * Set the acceleration of the Motion Magic controller
	 * @param acceleration - raw sensor units per 100 ms per second
	 * @throws CTREException
	 */
	public void setMotionMagicAcceleration(int acceleration) throws CTREException {
		throwIfNotOk(motorController.configMotionAcceleration(acceleration, timeoutMode.valueMs));
	}
	
	// #endregion

	// #region Motion Profile Buffer
	
	public void clearMotionProfileTrajectories() throws CTREException {
		throwIfNotOk(motorController.clearMotionProfileTrajectories()); // no timeout duration version of this apparently
	}
	
	public int getMotionProfileTopLevelBufferCount() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getMotionProfileTopLevelBufferCount());
	}
	
	public void pushMotionProfileTrajectory(TrajectoryPoint pt) throws CTREException {
		throwIfNotOk(motorController.pushMotionProfileTrajectory(pt));
	}
	
	public void startMotionProfile(BufferedTrajectoryPointStream stream, int minBufferedPts, ControlMode motionProfControlMode) throws CTREException {
		throwIfNotOk(motorController.startMotionProfile(stream, minBufferedPts, motionProfControlMode));
	}
	
	public boolean isMotionProfileFinished() throws CTREException {
		return throwIfNotOkElseReturn(motorController.isMotionProfileFinished());
	}
	
	public boolean isMotionProfileTopLevelBufferFull() throws CTREException {
		return throwIfNotOkElseReturn(motorController.isMotionProfileTopLevelBufferFull());
	}
	
	public void processMotionProfileBuffer() throws CTREException {
		motorController.processMotionProfileBuffer();
		throwLastError();
	}
	
	public MotionProfileStatus getMotionProfileStatus() throws CTREException {
		MotionProfileStatus status = new MotionProfileStatus();
		throwIfNotOk(motorController.getMotionProfileStatus(status));
		return status;
	}
	
	public void clearMotionProfileHasUnderrun() throws CTREException {
		throwIfNotOk(motorController.clearMotionProfileHasUnderrun(timeoutMode.valueMs));
	}
	
	public void setMotionControlFramePeriod(int periodMs) throws CTREException {
		throwIfNotOk(motorController.changeMotionControlFramePeriod(periodMs));
	}
	
	/**
	 * @param baseTrajectoryDuration - in ms
	 * @throws CTREException
	 */
	public void setMotionProfileTrajectoryPeriod(int baseTrajectoryDuration) throws CTREException {
		throwIfNotOk(motorController.configMotionProfileTrajectoryPeriod(baseTrajectoryDuration, timeoutMode.valueMs));
	}
	
	public void setMotionProfileTrajectoryInterpolation(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configMotionProfileTrajectoryInterpolationEnable(enable, timeoutMode.valueMs));
	}
	
	public void enableMotionProfileTrajectoryInterpolation() throws CTREException {
		setMotionProfileTrajectoryInterpolation(true);
	}
	
	public void disableMotionProfileTrajectoryInterpolation() throws CTREException {
		setMotionProfileTrajectoryInterpolation(false);
	}
	
	// #endregion

	// #region Feedback Device Integration Settings
	
	public void setFeedbackNotContinuous(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configFeedbackNotContinuous(enable, timeoutMode.valueMs));
	}
	
	public void enableFeedbackNotContinuous() throws CTREException {
		setFeedbackNotContinuous(true);
	}
	
	public void disableFeedbackNotContinuous() throws CTREException {
		setFeedbackNotContinuous(false);
	}
	
	
	public void setRemoteSensorClosedLoopDisableNeutralOnLOS(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configRemoteSensorClosedLoopDisableNeutralOnLOS(enable, timeoutMode.valueMs));
	}
	
	public void enableRemoteSensorClosedLoopDisableNeutralOnLOS() throws CTREException {
		setRemoteSensorClosedLoopDisableNeutralOnLOS(true);
	}
	
	public void disableRemoteSensorClosedLoopDisableNeutralOnLOS() throws CTREException {
		setRemoteSensorClosedLoopDisableNeutralOnLOS(false);
	}

	/**
	 * Reset selected feedback sensor (encoder) on a limit
	 * @param direction
	 * @param clearOnLimit
	 * @throws CTREException
	 */
	public void setClearPositionOnLimit(LimitDirection direction, boolean clearOnLimit) throws CTREException {
		if (direction == LimitDirection.FORWARD) setClearPositionOnLimitF(clearOnLimit);
		else if (direction == LimitDirection.REVERSE) setClearPositionOnLimitR(clearOnLimit);
	}
	
	public void setClearPositionOnLimitF(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configClearPositionOnLimitF(enable, timeoutMode.valueMs));
	}
	
	public void enableClearPositionOnLimitF() throws CTREException {
		setClearPositionOnLimitF(true);
	}
	
	public void disableClearPositionOnLimitF() throws CTREException {
		setClearPositionOnLimitF(false);
	}
	
	
	public void setClearPositionOnLimitR(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configClearPositionOnLimitR(enable, timeoutMode.valueMs));
	}
	
	public void enableClearPositionOnLimitR() throws CTREException {
		setClearPositionOnLimitR(true);
	}
	
	public void disableClearPositionOnLimitR() throws CTREException {
		setClearPositionOnLimitR(false);
	}
	
	public void setClearPositionOnQuadIdx(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configClearPositionOnQuadIdx(enable, timeoutMode.valueMs));
	}
	
	public void enableClearPositionOnQuadIdx() throws CTREException {
		setClearPositionOnQuadIdx(true);
	}
	
	public void disableClearPositionOnQuadIdx() throws CTREException {
		setClearPositionOnQuadIdx(false);
	}
	
	
	public void setLimitSwitchDisableNeutralOnLOS(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configLimitSwitchDisableNeutralOnLOS(enable, timeoutMode.valueMs));
	}
	
	public void enableLimitSwitchDisableNeutralOnLOS() throws CTREException {
		setLimitSwitchDisableNeutralOnLOS(true);
	}
	
	public void disableLimitSwitchDisableNeutralOnLOS() throws CTREException {
		setLimitSwitchDisableNeutralOnLOS(false);
	}
	
	
	public void setSoftLimitDisableNeutralOnLOS(boolean enable) throws CTREException {
		throwIfNotOk(motorController.configSoftLimitDisableNeutralOnLOS(enable, timeoutMode.valueMs));
	}
	
	public void enableSoftLimitDisableNeutralOnLOS() throws CTREException {
		setSoftLimitDisableNeutralOnLOS(true);
	}
	
	public void disableSoftLimitDisableNeutralOnLOS() throws CTREException {
		setSoftLimitDisableNeutralOnLOS(false);
	}
	
	
	public void setPulseWidthPeriodEdgesPerRotation(int edgesPerRotation) throws CTREException {
		throwIfNotOk(motorController.configPulseWidthPeriod_EdgesPerRot(edgesPerRotation, timeoutMode.valueMs));
	}
	
	
	public void setPulseWidthPeriodFilterWindowSamples(int samples) throws CTREException {
		throwIfNotOk(motorController.configPulseWidthPeriod_EdgesPerRot(samples, timeoutMode.valueMs));
	}
	
	// #endregion

	// #region Error

	/**
	 * Get most recent error from motor controller
	 * @return
	 */
	public ErrorCode getLastError() {
		return motorController.getLastError();
	}
	
	//FIXME temporary fix
	public void throwLastError() throws CTREException {
		ErrorCode code = getLastError();
		if(code.value == -3) return;
		throwIfNotOk(code);
	}
	
	// #endregion

	// #region unsorted

	// FIXME: 12/28/2019 Need to rewrite PIDProfile.Profile for Talons

	public void setP(PIDProfile.Profile slot, double p) throws CTREException {
		throwIfNotOk(motorController.config_kP(slot.id, p, timeoutMode.valueMs));
	}


	public void setI(PIDProfile.Profile slot, double i) throws CTREException {
		throwIfNotOk(motorController.config_kI(slot.id, i, timeoutMode.valueMs));
	}


	public void setD(PIDProfile.Profile slot, double d) throws CTREException {
		throwIfNotOk(motorController.config_kD(slot.id, d, timeoutMode.valueMs));
	}


	public void setF(PIDProfile.Profile slot, double f) throws CTREException {
		throwIfNotOk(motorController.config_kF(slot.id, f, timeoutMode.valueMs));
	}

	public void setPIDF(PIDProfile.Profile slot, double p, double i, double d, double f) throws CTREException {
		setP(slot, p);
		setI(slot, i);
		setD(slot, d);
		setF(slot, f);
	}


	public void setMaxIntegralAccumulator(PIDProfile.Profile profile, double iaccum) throws CTREException {
		throwIfNotOk(motorController.configMaxIntegralAccumulator(profile.id, iaccum, timeoutMode.valueMs));
	}

	public void setIntegralZone(PIDProfile.Profile profile, int izone) throws CTREException {
		throwIfNotOk(motorController.config_IntegralZone(profile.id, izone, timeoutMode.valueMs));
	}


	public void setAllowableClosedLoopError(PIDProfile.Profile profile, int allowableError) throws CTREException {
		throwIfNotOk(motorController.configAllowableClosedloopError(profile.id, allowableError, timeoutMode.valueMs));
	}


	


	public void clearStickyFaults() throws CTREException {
		throwIfNotOk(motorController.clearStickyFaults(timeoutMode.valueMs));
	}
	

	public ControlMode getControlMode() {
		return motorController.getControlMode();
	}
	

	public void selectProfileSlot(PIDProfile.Profile slot, PidIdx pidIdx) {
		motorController.selectProfileSlot(slot.id, pidIdx.value);
	}
	

	
	public int getActiveTrajectoryPosition() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getActiveTrajectoryPosition());
	}
	
	public int getActiveTrajectoryPosition(PidIdx pidIdx) throws CTREException {
		return throwIfNotOkElseReturn(motorController.getActiveTrajectoryPosition(pidIdx.value));
	}
	
	public int getActiveTrajectoryVelocity() throws CTREException {
		return throwIfNotOkElseReturn(motorController.getActiveTrajectoryVelocity());
	}
	
	public int getActiveTrajectoryVelocity(PidIdx pidIdx) throws CTREException {
		return throwIfNotOkElseReturn(motorController.getActiveTrajectoryVelocity(pidIdx.value));
	}
	
	public void setLimitSwitches(boolean enable) throws CTREException {
		motorController.overrideLimitSwitchesEnable(enable);
		throwLastError();
	}
	
	public void enableLimitSwitches() throws CTREException {
		setLimitSwitches(true);
	}
	
	public void disableLimitSwitches() throws CTREException {
		setLimitSwitches(false);
	}
	
	public void setOverrideSoftLimits(boolean enable) throws CTREException {
		motorController.overrideSoftLimitsEnable(enable);
		throwLastError();
	}
	
	public void enableOverrideSoftLimits() throws CTREException {
		setOverrideSoftLimits(true);
	}
	
	public void disableOverrideSoftLimits() throws CTREException {
		setOverrideSoftLimits(false);
	}

	/**
	 * Sets a parameter. Generally this is not used. This can be utilized in -
	 * Using new features without updating API installation. - Errata
	 * workarounds to circumvent API implementation. - Allows for rapid testing
	 * / unit testing of firmware.
	 *
	 * @param param
	 *            Parameter enumeration.
	 * @param value
	 *            Value of parameter.
	 * @param subValue
	 *            Subvalue for parameter. Maximum value of 255.
	 * @param ordinal
	 *            Ordinal of parameter.
	 */
	public void setParameter(ParamEnum param, double value, int subValue, int ordinal) throws CTREException {
		throwIfNotOk(motorController.configSetParameter(param, value, subValue, ordinal, timeoutMode.valueMs));
	}

	public void setDirectionParameterForLimit(LimitDirection direction, double value, int subValue, int ordinal, boolean clearOnLimit) throws CTREException {
		if (clearOnLimit) {
			if (direction == LimitDirection.FORWARD) setParameter(ParamEnum.eClearPositionOnLimitF, value, subValue, ordinal);
			else if (direction == LimitDirection.REVERSE) setParameter(ParamEnum.eClearPositionOnLimitR, value, subValue, ordinal);
		}
	}

	/**
	 * Enum storing different timeout values in ms for construction time
	 * or runtime updates.
	 */
	public enum TimeoutMode {
		/** Longer timeout, used for constructors */
		CONSTRUCTING(100),
		/** Shorter timeout, used for on the fly updates */
		RUNNING(20),
		NO_TIMEOUT(0);

		final int valueMs;

		private TimeoutMode(int valueMs) {
			this.valueMs = valueMs;
		}
	}

	/**
	 * PID ID Slots: Primary 0; Auxiliary 1
	 */
	public enum PidIdx { //FIXME give this a name that reflects w/e it actually does
		PRIMARY_CLOSED_LOOP(0),
		AUXILIARY_CLOSED_LOOP(1);
		
		final int value;
		
		private PidIdx(int value) {
			this.value = value;
		}
	}
	
	/**
	 * Number of samples in the rolling average of velocity measurement.
	 * @see BaseMotorControllerWrapper#setVelocityMeasurementWindow(VelocityMeasurementWindow window)
	 */
	public enum VelocityMeasurementWindow{
		SAMPLES_1(1),
		SAMPLES_2(2),
		SAMPLES_4(4),
		SAMPLES_8(8),
		SAMPLES_16(16),
		SAMPLES_32(32);
		
		final int value;
		private VelocityMeasurementWindow(int value) {
			this.value = value;
		}
	}

	// #endregion
}
