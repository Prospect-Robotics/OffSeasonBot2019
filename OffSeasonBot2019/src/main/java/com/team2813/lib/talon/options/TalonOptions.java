package com.team2813.lib.talon.options;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
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
public @interface TalonOptions {

	int deviceNumber();

	String subsystemName();

	/** in ms */
	int peakCurrentDuration();

	/** in amps */
	int peakCurrentLimit();

	FeedbackDevice feedbackSensor();

	boolean enableVoltageCompensation();

	int compSaturationVoltage();

	int continuousCurrentLimitAmps();

	int motionAcceleration();

	int motionCruiseVelocity();

	/** in seconds */
	double closedLoopRampRate();

	/** in seconds */
	double openLoopRampRate();

	boolean invertSensorPhase();

	StatusFrameEnhanced statusFrame();

	int statusFramePeriod() default 5;

	VelocityMeasPeriod velocityMeasurementPeriod() default VelocityMeasPeriod.Period_50Ms;
	
	VelocityMeasurementWindow velocityMeasurementWindow() default VelocityMeasurementWindow.SAMPLES_1;

}