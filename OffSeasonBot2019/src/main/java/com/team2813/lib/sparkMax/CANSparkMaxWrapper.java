package com.team2813.lib.sparkMax;

import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.team2813.lib.sparkMax.options.InvertType;
import com.team2813.lib.talon.CTREException;
import com.team2813.lib.talon.TalonWrapper;
import com.team2813.lib.talon.VictorWrapper;

public class CANSparkMaxWrapper extends CANSparkMax {

	public String subsystemName;

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

	protected void throwIfNotOk(CANError error) throws SparkMaxException {
		SparkMaxException.throwIfNotOk(subsystemName, error);
	}


	//TODO better name for this
//	protected <T> T throwIfNotOkElseReturn(T value) throws SparkMaxException {
//		throwLastError();
//		return value;
//	}

//	public CANError getLastError() {
//		return this.getLastError();
//	}

//	FIXME temporary fix
//	public void throwLastError() throws SparkMaxException {
//		CANError code = getLastError();
//		throwIfNotOk(code);
//	}

	// #endregion

	//#region Smart Current Limit

	public void setCurrLimit(int limit) throws SparkMaxException {
		throwIfNotOk(setSmartCurrentLimit(limit));
	}

	public void setCurrLimit(int stallLimit, int freeLimit) throws SparkMaxException {
		throwIfNotOk(setSmartCurrentLimit(stallLimit, freeLimit));
	}

	public void setCurrLimit(int stallLimit, int freeLimit, int limitRPM) throws SparkMaxException {
		throwIfNotOk(setSmartCurrentLimit(stallLimit, freeLimit, limitRPM));
	}

	//#endregion

	//#region Secondary Current Limit

	public void setSecondaryCurrLimit(double limit) throws SparkMaxException {
		throwIfNotOk(setSecondaryCurrentLimit(limit));
	}

	public void setSecondaryCurrLimit(double limit, int chopCycles) throws SparkMaxException {
		throwIfNotOk(setSecondaryCurrentLimit(limit, chopCycles));
	}

	//#endregion

	//#region Neutral Mode

	public void setNeutralMode(IdleMode idleMode) throws SparkMaxException {
		throwIfNotOk(setIdleMode(idleMode));
	}

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

	public void setOpenLoopRamp(double rate) throws SparkMaxException {
		throwIfNotOk(setOpenLoopRampRate(rate));
	}

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

	public void setPeriodicFrame(PeriodicFrame frameID, int periodMs) throws SparkMaxException {
		throwIfNotOk(setPeriodicFramePeriod(frameID, periodMs));
	}

	public void setEncoderPosition(double position) throws SparkMaxException {
		throwIfNotOk(setEncPosition(position));
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

	public void setRef(double value, ControlType ctrl) throws SparkMaxException {
		throwIfNotOk(getPIDController().setReference(value, ctrl));
	}

	public void setRef(double value, ControlType ctrl, int pidSlot) throws SparkMaxException {
		throwIfNotOk(getPIDController().setReference(value, ctrl, pidSlot));
	}

	public void setRef(double value, ControlType ctrl, int pidSlot, double arbFeedForward) throws SparkMaxException {
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



	//#endregion

}
