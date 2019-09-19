package com.team2813.lib.talon.options;

import com.ctre.phoenix.motorcontrol.*;
import com.team2813.lib.talon.BaseMotorControllerWrapper.VelocityMeasurementWindow;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//TODO document

/**
 * @author Adrian Guerra
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface VictorOptions {

	int deviceNumber();

	String subsystemName();

	FeedbackDevice feedbackSensor();

	boolean reverseLimitSwitchEnable();

	/** in encoder ticks */
	int reverseSoftLimit();

	boolean reverseSoftLimitEnable();

	boolean enableVoltageCompensation();

	int compSaturationVoltage();

	int motionAcceleration();

	int motionCruiseVelocity();

	/** in seconds */
	double closedLoopRampRate();

	/** in seconds */
	double openLoopRampRate();

	boolean invertSensorPhase();

	StatusFrame statusFrame();

	int statusFramePeriod() default 5;
	
	boolean clearOnLimitFwd() default false;

	LimitSwitchSource forwardLimitSwitchSource() default LimitSwitchSource.FeedbackConnector;

	LimitSwitchNormal forwardLimitSwitchNormal() default LimitSwitchNormal.NormallyOpen;

	VelocityMeasPeriod velocityMeasurementPeriod() default VelocityMeasPeriod.Period_50Ms;
	
	VelocityMeasurementWindow velocityMeasurementWindow() default VelocityMeasurementWindow.SAMPLES_1;
}