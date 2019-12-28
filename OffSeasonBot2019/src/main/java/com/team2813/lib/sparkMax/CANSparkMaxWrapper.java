package com.team2813.lib.sparkMax;

import com.revrobotics.*;
import com.team2813.lib.config.Inverted;
import com.team2813.lib.config.PeriodicFrame;
import com.team2813.lib.config.SparkConfig;
import com.team2813.lib.ctre.CTREException;
import com.team2813.lib.ctre.TalonWrapper;
import com.team2813.lib.ctre.VictorWrapper;

public class CANSparkMaxWrapper extends CANSparkMax {

	public String subsystemName;
	public SparkConfig config;

	/**
	 * Create a new SPARK MAX Controller
	 *
	 * @param deviceID The device ID.
	 * @param type     The motor type connected to the controller. Brushless motors
	 *                 must be connected to their matching color and the hall sensor
	 *                 plugged in. Brushed motors must be connected to the Red and
	 */
	public CANSparkMaxWrapper(int deviceID, String subsystemName, MotorType type) {
		super(deviceID, type);
		this.subsystemName = subsystemName;
	}

	/**
	 * This constructor only for followers
	 *
	 * @param deviceID The device ID.
	 * @param type     The motor type connected to the controller. Brushless motors
	 *                 must be connected to their matching color and the hall sensor
	 *                 plugged in. Brushed motors must be connected to the Red and
	 */
	public CANSparkMaxWrapper(int deviceID, MotorType type) {
		super(deviceID, type);
	}

	//#region Error

	/**
	 * Same as throwIfNotOk in {@link SparkMaxException} but uses Spark's
	 * subsystem name
	 *
	 * @param error
	 * @throws SparkMaxException
	 */
	protected void throwIfNotOk(CANError error) throws SparkMaxException {
		SparkMaxException.throwIfNotOk(subsystemName, error);
	}

	// #endregion

	//#region Smart Current Limit

	/**
	 * Sets the current limit in Amps.
	 *
	 * The motor controller will reduce the controller voltage output to avoid
	 * surpassing this limit. This limit is enabled by default and used for
	 * brushless only. This limit is highly recommended when using the NEO brushless
	 * motor.
	 *
	 * The NEO Brushless Motor has a low internal resistance, which can mean large
	 * current spikes that could be enough to cause damage to the motor and
	 * controller. This current limit provides a smarter strategy to deal with high
	 * current draws and keep the motor and controller operating in a safe region.
	 *
	 * @param limit The current limit in Amps.
	 *
	 */
	public void setCurrLimit(int limit) throws SparkMaxException {
		throwIfNotOk(setSmartCurrentLimit(limit));
	}

	/**
	 * Sets the current limit in Amps.
	 *
	 * The motor controller will reduce the controller voltage output to avoid
	 * surpassing this limit. This limit is enabled by default and used for
	 * brushless only. This limit is highly recommended when using the NEO brushless
	 * motor.
	 *
	 * The NEO Brushless Motor has a low internal resistance, which can mean large
	 * current spikes that could be enough to cause damage to the motor and
	 * controller. This current limit provides a smarter strategy to deal with high
	 * current draws and keep the motor and controller operating in a safe region.
	 *
	 * The controller can also limit the current based on the RPM of the motor in a
	 * linear fashion to help with controllability in closed loop control. For a
	 * response that is linear the entire RPM range leave limit RPM at 0.
	 *
	 * @param stallLimit The current limit in Amps at 0 RPM.
	 * @param freeLimit  The current limit at free speed (5700RPM for NEO).
	 *
	 */
	public void setCurrLimit(int stallLimit, int freeLimit) throws SparkMaxException {
		throwIfNotOk(setSmartCurrentLimit(stallLimit, freeLimit));
	}

	/**
	 * Sets the current limit in Amps.
	 *
	 * The motor controller will reduce the controller voltage output to avoid
	 * surpassing this limit. This limit is enabled by default and used for
	 * brushless only. This limit is highly recommended when using the NEO brushless
	 * motor.
	 *
	 * The NEO Brushless Motor has a low internal resistance, which can mean large
	 * current spikes that could be enough to cause damage to the motor and
	 * controller. This current limit provides a smarter strategy to deal with high
	 * current draws and keep the motor and controller operating in a safe region.
	 *
	 * The controller can also limit the current based on the RPM of the motor in a
	 * linear fashion to help with controllability in closed loop control. For a
	 * response that is linear the entire RPM range leave limit RPM at 0.
	 *
	 * @param stallLimit The current limit in Amps at 0 RPM.
	 * @param freeLimit  The current limit at free speed (5700RPM for NEO).
	 * @param limitRPM   RPM less than this value will be set to the stallLimit, RPM
	 *                   values greater than limitRPM will scale linearly to
	 *                   freeLimit
	 *
	 */
	public void setCurrLimit(int stallLimit, int freeLimit, int limitRPM) throws SparkMaxException {
		throwIfNotOk(setSmartCurrentLimit(stallLimit, freeLimit, limitRPM));
	}

	//#endregion

	//#region Secondary Current Limit

	/**
	 * Sets the secondary current limit in Amps.
	 *
	 * The motor controller will disable the output of the controller briefly if the
	 * current limit is exceeded to reduce the current. This limit is a simplified
	 * 'on/off' controller. This limit is enabled by default but is set higher than
	 * the default Smart Current Limit.
	 *
	 * The time the controller is off after the current limit is reached is
	 * determined by the parameter limitCycles, which is the number of PWM cycles
	 * (20kHz). The recommended value is the default of 0 which is the minimum time
	 * and is part of a PWM cycle from when the over current is detected. This
	 * allows the controller to regulate the current close to the limit value.
	 *
	 * The total time is set by the equation
	 *
	 * <code>
	 * t = (50us - t0) + 50us * limitCycles
	 * t = total off time after over current
	 * t0 = time from the start of the PWM cycle until over current is detected
	 * </code>
	 *
	 *
	 * @param limit The current limit in Amps.
	 *
	 */
	public void setSecondaryCurrLimit(double limit) throws SparkMaxException {
		throwIfNotOk(setSecondaryCurrentLimit(limit));
	}


	/**
	 * Sets the secondary current limit in Amps.
	 *
	 * The motor controller will disable the output of the controller briefly if the
	 * current limit is exceeded to reduce the current. This limit is a simplified
	 * 'on/off' controller. This limit is enabled by default but is set higher than
	 * the default Smart Current Limit.
	 *
	 * The time the controller is off after the current limit is reached is
	 * determined by the parameter limitCycles, which is the number of PWM cycles
	 * (20kHz). The recommended value is the default of 0 which is the minimum time
	 * and is part of a PWM cycle from when the over current is detected. This
	 * allows the controller to regulate the current close to the limit value.
	 *
	 * The total time is set by the equation
	 *
	 * <code>
	 * t = (50us - t0) + 50us * limitCycles
	 * t = total off time after over current
	 * t0 = time from the start of the PWM cycle until over current is detected
	 * </code>
	 *
	 *
	 * @param limit      The current limit in Amps.
	 * @param chopCycles The number of additional PWM cycles to turn the driver off
	 *                   after overcurrent is detected.
	 */
	public void setSecondaryCurrLimit(double limit, int chopCycles) throws SparkMaxException {
		throwIfNotOk(setSecondaryCurrentLimit(limit, chopCycles));
	}

	//#endregion

	//#region Neutral Mode

	/**
	 * Sets the Neutral/Brake/Idle mode setting for the SPARK MAX.
	 *
	 * @param idleMode Idle mode (coast or brake).
	 *
	 */
	public void setNeutralMode(IdleMode idleMode) throws SparkMaxException {
		throwIfNotOk(setIdleMode(idleMode));
	}

	/**
	 * Gets the idle mode setting for the SPARK MAX.
	 *
	 * This uses the Get Parameter API and should be used infrequently. This
	 * function uses a non-blocking call and will return a cached value if the
	 * parameter is not returned by the timeout. The timeout can be changed by
	 * calling SetCANTimeout(int milliseconds)
	 *
	 * @return IdleMode Idle mode setting
	 */
	public IdleMode getNeutralMode() {
		return getIdleMode();
	}

	//#endregion

	//#region Voltage Compensation

	public void enableVoltageComp(double nominalVoltage) throws SparkMaxException {
		throwIfNotOk(enableVoltageCompensation(nominalVoltage));
	}

	public void disableVoltageComp() throws SparkMaxException {
		throwIfNotOk(disableVoltageCompensation());
	}

	//#endregion

	//#region Ramp Rate

	/**
	 * Sets the ramp rate for open loop control modes.
	 * This is the maximum rate at which the motor controller's output is allowed to
	 * change.
	 * @param rate Time in seconds to go from 0 to full throttle.
	 * @throws SparkMaxException
	 */
	public void setOpenLoopRamp(double rate) throws SparkMaxException {
		throwIfNotOk(setOpenLoopRampRate(rate));
	}

	/**
	 * Sets the ramp rate for closed loop control modes.
	 *
	 * This is the maximum rate at which the motor controller's output is allowed to
	 * change.
	 *
	 * @param rate Time in seconds to go from 0 to full throttle.
	 *
	 */
	public void setClosedLoopRamp(double rate) throws SparkMaxException {
		throwIfNotOk(setClosedLoopRampRate(rate));
	}

	//#endregion

	//#region Follow

	public void setFollower(final CANSparkMaxWrapper leader) throws SparkMaxException {
		throwIfNotOk(follow(leader));
	}

	public void setFollower(final CANSparkMaxWrapper leader, InvertType invertType) throws SparkMaxException {
		throwIfNotOk(follow(leader, invertType.inverted));
	}

	public void setFollower(int deviceID) throws SparkMaxException {
		throwIfNotOk(follow(ExternalFollower.kFollowerSparkMax, deviceID));
	}

	public void setFollower(ExternalFollower leader, int deviceID) throws SparkMaxException {
		throwIfNotOk(follow(leader, deviceID));
	}

	public void setFollower(TalonWrapper leader) throws SparkMaxException, CTREException {
		throwIfNotOk(follow(ExternalFollower.kFollowerPhoenix, leader.getDeviceID()));
	}

	public void setFollower(VictorWrapper leader) throws SparkMaxException, CTREException {
		throwIfNotOk(follow(ExternalFollower.kFollowerPhoenix, leader.getDeviceID()));
	}

	public void setFollower(ExternalFollower leader, int deviceID, InvertType invertType) throws SparkMaxException {
		throwIfNotOk(follow(leader, deviceID, invertType.inverted));
	}

	//#endregion

	/** Clear Faults */
	public void clrFaults() throws SparkMaxException {
		throwIfNotOk(clearFaults());
	}

	public void burnSettingsToFlash() throws SparkMaxException {
		throwIfNotOk(burnFlash());
	}

	/** set CAN timeout */
	public void setTimeout(int timeoutMs) throws SparkMaxException {
		throwIfNotOk(setCANTimeout(timeoutMs));
	}

	public void setTimeout() throws SparkMaxException {
		setTimeout(TimeoutMode.RUNNING.valueMs);
	}

	//#region Motor Type

	public void setTypeOfMotor(MotorType type) throws SparkMaxException {
		throwIfNotOk(setMotorType(type));
	}

	public void setMotorBrushed() throws SparkMaxException {
		setTypeOfMotor(MotorType.kBrushed);
	}

	public void setMotorBrushless() throws SparkMaxException {
		setTypeOfMotor(MotorType.kBrushless);
	}

	//#endregion

	/**
	 * Set the rate of transmission for periodic frames from the SPARK MAX
	 *
	 * Each motor controller sends back three status frames with different data at
	 * set rates. Use this function to change the default rates.
	 *
	 * Defaults: Status0 - 10ms Status1 - 20ms Status2 - 50ms
	 *
	 * This value is not stored in the FLASH after calling burnFlash() and is reset
	 * on powerup.
	 *
	 * Refer to the SPARK MAX reference manual on details for how and when to
	 * configure this parameter.
	 *
	 * @param frameID  The frame ID can be one of PeriodicFrame type
	 * @param periodMs The rate the controller sends the frame to the controller.
	 *
	 */
	public void setPeriodicFrame(PeriodicFrame frameID, int periodMs) throws SparkMaxException {
		throwIfNotOk(setPeriodicFramePeriod(frameID, periodMs));
	}

	public void setPeriodicFrame(PeriodicFrame frameID) throws SparkMaxException {
		setPeriodicFrame(frameID, TimeoutMode.RUNNING.valueMs);
	}

	public void setEncoderPosition(double position) throws SparkMaxException {
		throwIfNotOk(setEncPosition(position));
	}

	public double getEncoderPosition() {
		return getEncoder().getPosition();
	}

	public void setIntegralAccumulator(double value) throws SparkMaxException {
		throwIfNotOk(setIAccum(value));
	}

	public void factoryDefault() throws SparkMaxException {
		throwIfNotOk(restoreFactoryDefaults());
	}

	public void factoryDefault(boolean persist) throws SparkMaxException {
		throwIfNotOk(restoreFactoryDefaults(persist));
	}

	//#region PID Controller

	public void set(double value, ControlType ctrl) throws SparkMaxException {
		throwIfNotOk(getPIDController().setReference(value, ctrl));
	}

	public void set(double value, ControlType ctrl, int pidSlot) throws SparkMaxException {
		throwIfNotOk(getPIDController().setReference(value, ctrl, pidSlot));
	}

	public void set(double value, ControlType ctrl, int pidSlot, double arbFeedForward) throws SparkMaxException {
		throwIfNotOk(getPIDController().setReference(value, ctrl, pidSlot, arbFeedForward));
	}

	public void setPIDF(int slotID, double p, double i, double d, double f) throws SparkMaxException {
		setP(p, slotID);
		setI(i, slotID);
		setD(d, slotID);
		setF(f, slotID);
	}

	public void setP(double gain) throws SparkMaxException {
		throwIfNotOk(getPIDController().setP(gain));
	}

	public void setP(double gain, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setP(gain, slotID));
	}

	public void setI(double gain) throws SparkMaxException {
		throwIfNotOk(getPIDController().setI(gain));
	}

	public void setI(double gain, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setI(gain, slotID));
	}

	public void setD(double gain) throws SparkMaxException {
		throwIfNotOk(getPIDController().setD(gain));
	}

	public void setD(double gain, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setD(gain, slotID));
	}

	public void setDFilter(double gain) throws SparkMaxException {
		throwIfNotOk(getPIDController().setDFilter(gain));
	}

	public void setDFilter(double gain, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setDFilter(gain, slotID));
	}

	public void setF(double gain) throws SparkMaxException {
		throwIfNotOk(getPIDController().setFF(gain));
	}

	public void setF(double gain, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setFF(gain, slotID));
	}

	public void setIZone(double IZone) throws SparkMaxException {
		throwIfNotOk(getPIDController().setIZone(IZone));
	}

	public void setIZone(double IZone, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setIZone(IZone, slotID));
	}

	public void setOutputRange(double min, double max) throws SparkMaxException {
		throwIfNotOk(getPIDController().setOutputRange(min, max));
	}

	public void setOutputRange(double min, double max, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setOutputRange(min, max, slotID));
	}

	public double getP() {
		return getPIDController().getP();
	}

	public double getP(int slotID) {
		return getPIDController().getP(slotID);
	}

	public double getI() {
		return getPIDController().getI();
	}

	public double getI(int slotID) {
		return getPIDController().getI(slotID);
	}

	public double getD() {
		return getPIDController().getD();
	}

	public double getD(int slotID) {
		return getPIDController().getD(slotID);
	}

	public double getDFilter(int slotID) {
		return getPIDController().getDFilter(slotID);
	}

	public double getF() {
		return getPIDController().getFF();
	}

	public double getF(int slotID) {
		return getPIDController().getFF(slotID);
	}

	public double getIZone() {
		return getPIDController().getIZone();
	}

	public double getIZone(int slotID) {
		return getPIDController().getIZone(slotID);
	}

	public double getPIDOutputMin() {
		return getPIDController().getOutputMin();
	}

	public double getPIDOutputMin(int slotID) {
		return getPIDController().getOutputMin(slotID);
	}

	public double getPIDOutputMax() {
		return getPIDController().getOutputMax();
	}

	public double getPIDOutputMax(int slotID) {
		return getPIDController().getOutputMax(slotID);
	}

	public void setSmartMotionMaxVelocity(double maxVel, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setSmartMotionMaxVelocity(maxVel, slotID));
	}

	public void setSmartMotionMaxAccel(double maxAccel, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setSmartMotionMaxAccel(maxAccel, slotID));
	}

	public void setSmartMotionMinOutputVelocity(double minVel, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setSmartMotionMinOutputVelocity(minVel, slotID));
	}

	public void setSmartMotionAllowedClosedLoopError(double allowedErr, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setSmartMotionAllowedClosedLoopError(allowedErr, slotID));
	}

	public void setSmartMotionAccelStrategy(CANPIDController.AccelStrategy accelStrategy, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setSmartMotionAccelStrategy(accelStrategy, slotID));
	}

	public void setPIDIMaxAccum(double iMaxAccum, int slotID) throws SparkMaxException {
		throwIfNotOk(getPIDController().setIMaxAccum(iMaxAccum, slotID));
	}

	public void setPIDIAccum(double iAccum) throws SparkMaxException {
		throwIfNotOk(getPIDController().setIAccum(iAccum));
	}

	//#endregion

	//#region Limit Switch

	public boolean isForwardLimitSwitchClosed() {
		return getLimitSwitchPolarity(true, CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
	}

	public boolean isReverseLimitSwitchClosed() {
		return getLimitSwitchPolarity(false, CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
	}

	public boolean getLimitSwitchPolarity(boolean forward, CANDigitalInput.LimitSwitchPolarity normal) {
		return forward ? getForwardLimitSwitch(normal).get() : getReverseLimitSwitch(normal).get();
	}

	//#endregion

	//#region Soft Limit

	public void enableForwardSoftLimit(boolean enable) throws SparkMaxException {
		enableSoftLimit(true, enable);
	}

	public void enableReverseSoftLimit(boolean enable) throws SparkMaxException {
		enableSoftLimit(false, enable);
	}

	public void enableSoftLimit(boolean forward, boolean enable) throws SparkMaxException {
		throwIfNotOk(enableSoftLimit(forward ? SoftLimitDirection.kForward : SoftLimitDirection.kReverse, enable));
	}

	public void setSoftLimit(boolean forward, double position) throws SparkMaxException {
		throwIfNotOk(setSoftLimit(forward ? SoftLimitDirection.kForward : SoftLimitDirection.kReverse, (float) position));
	}

	public void setForwardSoftLimit(double position) throws SparkMaxException {
		setSoftLimit(true, position);
	}

	public void setReverseSoftLimit(double position) throws SparkMaxException {
		setSoftLimit(false, position);
	}

	public void setInverted(Inverted isInverted) {
		if (isInverted == Inverted.INVERTED)
			super.setInverted(true);
		else
			super.setInverted(false);
	}

	public SparkConfig getConfig() {
		return config;
	}

	public void setConfig(SparkConfig config) {
		this.config = config;
	}

	//#endregion

	//#region Inversion


	/**
	 * Enum to handle inversion of followers compared to leaders.
	 * NORMAL: forward = forward
	 * FOLLOW_LEADER: forward = leader
	 * OPPOSE_LEADER: forward = !leader
	 * INVERTED: forward = reverse
	 */
	public enum InvertType {
		NORMAL(false), FOLLOW_LEADER(false), OPPOSE_LEADER(true), INVERTED(true);

		public boolean inverted;

		InvertType(boolean inverted) {
			this.inverted = inverted;
		}
	}

	/**
	 * Set the invert type to NORMAL or INVERTED
	 * @param invertType
	 */
	public void setInverted(InvertType invertType) {
		setInverted(invertType.inverted);
	}

	//#endregion

	/**
	 * Enum storing different timeout values in ms for construction time
	 * or runtime updates.
	 */
	public enum TimeoutMode {
		/** Longer timeout, used for constructors */
		CONSTRUCTING(100),
		/** Shorter timeout, used for on the fly updates */
		RUNNING(10),
		NO_TIMEOUT(0);

		final int valueMs;

		private TimeoutMode(int valueMs) {
			this.valueMs = valueMs;
		}
	}

}
