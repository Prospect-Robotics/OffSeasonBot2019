package com.team2813.lib.sparkMax.options;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;
import com.team2813.lib.talon.BaseMotorControllerWrapper.VelocityMeasurementWindow;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//TODO document

/**
 * @author Grady Whelan
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface SparkMaxOptions {

	int deviceNumber();

	MotorType type();

	String subsystemName();

	/** in ms */
	int peakCurrentDuration();

	/** in amps */
	int peakCurrentLimit();

//	FeedbackDevice feedbackSensor(); // TODO: 09/20/2019 find for Spark max

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

	PeriodicFrame statusFrame();

	int statusFramePeriod() default 5;// FIXME: 09/20/2019 not sure what to default here

//	VelocityMeasPeriod velocityMeasurementPeriod() default VelocityMeasPeriod.Period_50Ms; // TODO: 09/20/2019 find for spark max

//	VelocityMeasurementWindow velocityMeasurementWindow() default VelocityMeasurementWindow.SAMPLES_1;// TODO: 09/20/2019 find for spark max

}