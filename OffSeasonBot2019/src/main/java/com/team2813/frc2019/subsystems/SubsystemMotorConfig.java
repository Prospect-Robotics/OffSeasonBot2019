package com.team2813.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.sparkMax.options.Follower;
import com.team2813.lib.sparkMax.options.SparkMaxOptions;
import com.team2813.lib.talon.BaseMotorControllerWrapper;
import com.team2813.lib.talon.CTREException;
import com.team2813.lib.talon.TalonWrapper;
import com.team2813.lib.talon.VictorWrapper;
import com.team2813.lib.talon.options.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class SubsystemMotorConfig {


	@SparkMaxOptions(
		deviceNumber = 1,
		type = MotorType.kBrushless,
		subsystemName = "Drive",
		peakCurrentDuration = 0,
		peakCurrentLimit = 0,
		enableVoltageCompensation = true,
		compSaturationVoltage = 12,
		continuousCurrentLimitAmps = 30,
		motionAcceleration = 0,
		motionCruiseVelocity = 0,
		closedLoopRampRate = 0,
		openLoopRampRate = 0,
		invertSensorPhase = true,
		statusFrame = PeriodicFrame.kStatus2
	)
	@Follower(
		id = 2,
		type = MotorType.kBrushless
	)
	static CANSparkMaxWrapper driveLeft;

	@SparkMaxOptions(
		deviceNumber = 3,
		type = MotorType.kBrushless,
		subsystemName = "Drive",
		peakCurrentDuration = 0,
		peakCurrentLimit = 0,
		enableVoltageCompensation = true,
		compSaturationVoltage = 12,
		continuousCurrentLimitAmps = 30,
		motionAcceleration = 0,
		motionCruiseVelocity = 0,
		closedLoopRampRate = 0,
		openLoopRampRate = 0,
		invertSensorPhase = true,
		statusFrame = PeriodicFrame.kStatus2
	)
	@Follower(
		id = 4,
		type = MotorType.kBrushless
	)
	static CANSparkMaxWrapper driveRight;

	//#region Spark Max Initialization

	static {
		initializeSparks();
	}

	private static void initializeSparks() {
		List<Integer> sparkIds = new ArrayList<>();
		for (Field field : SubsystemMotorConfig.class.getDeclaredFields()) {
			if (field.getType() == CANSparkMaxWrapper.class) {
				try {
					SparkMaxOptions options = field.getAnnotation(SparkMaxOptions.class);
					System.out.println("Configuring " + options.subsystemName());
					// TODO exception if no options present

					for (Integer id : sparkIds)
						if (id == options.deviceNumber()) {
							System.err.println("Tried to register spark max with already used id");
						}

					sparkIds.add(options.deviceNumber());

					CANSparkMaxWrapper spark = new CANSparkMaxWrapper(options.deviceNumber(), options.subsystemName(), options.type());

					spark.factoryDefault();

//					spark.setPeakCurrentDuration(options.peakCurrentDuration()); // FIXME: 09/20/2019 for the spark
					spark.setCurrLimit(options.peakCurrentLimit());

					spark.enableVoltageCompensation(options.compSaturationVoltage());

					spark.setOpenLoopRamp(options.openLoopRampRate());
					spark.setClosedLoopRamp(options.closedLoopRampRate());

					spark.setPeriodicFrame(options.statusFrame(), options.statusFramePeriod());

//					spark.setSmartMotionMaxVelocity(options.motionCruiseVelocity()); // FIXME: 09/20/2019 need to change parameters/types
//					spark.setSmartMotionMaxAccel(options.motionAcceleration()); // FIXME: 09/20/2019 need to change parameters/types



					spark.setSecondaryCurrLimit(options.continuousCurrentLimitAmps());// TODO check this is actually continuous limit

					for (com.team2813.lib.sparkMax.options.HardLimitSwitch hardLimitSwitch : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.HardLimitSwitch.class)) {
						System.out.println("\tconfiguring hard limit switch " + hardLimitSwitch.direction());
						// FIXME remake limit switch stuff differently since it is called differently
					}

					for (com.team2813.lib.sparkMax.options.SoftLimit softLimit : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.SoftLimit.class)) {
						System.out.println("\tconfiguring soft limit " + softLimit.direction());

						//FIXME remake limit switch stuff differently
					}

					for (com.team2813.lib.sparkMax.options.PIDProfile profile : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.PIDProfile.class)) {
						System.out.println("\tConfiguring pid profile " + profile.profile());
						spark.setPIDF(profile.profile().id, profile.p(), profile.i(), profile.d(), profile.f());
					}

					com.team2813.lib.sparkMax.options.Inverted inverted = field.getAnnotation(com.team2813.lib.sparkMax.options.Inverted.class);
					if (inverted != null)
						spark.setInverted(inverted.type().inverted);
					else
						spark.setInverted(com.team2813.lib.sparkMax.options.InvertType.NORMAL.inverted);

					for (Follower follower : field.getAnnotationsByType(Follower.class)) {
						System.out.println(
								"\tCreating follower w/ id of " + follower.id() + " of type " + follower.motorControllerType()
										+ " with follow mode " + follower.followMode() + " on " + options.subsystemName()
						);
						CANSparkMaxWrapper sparkMaxFollower = new CANSparkMaxWrapper(follower.id(), follower.type());
						sparkMaxFollower.follow(spark, follower.followMode().inverted);
					}

					field.set(null, spark);

				} catch (SparkMaxException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//#endregion

	//#region Talon Initialization
	static {
		initializeTalons(); // TODO call from somewhere else
	}

	private static void initializeTalons() {
		List<Integer> talonIds = new ArrayList<>();
		for (Field field : SubsystemMotorConfig.class.getDeclaredFields()) {
			if (field.getType() == TalonWrapper.class) {
				try {
					TalonOptions options = field.getAnnotation(TalonOptions.class);
					System.out.println("Configuring " + options.subsystemName());
					// TODO exception if no options present

					for (Integer id : talonIds)
						if (id == options.deviceNumber()) {
							System.err.println("Tried to register talon with already used id");
						}

					talonIds.add(options.deviceNumber());

					TalonWrapper talon = new TalonWrapper(options.deviceNumber(), options.subsystemName());

					talon.setFactoryDefaults();

					talon.setPeakCurrentDuration(options.peakCurrentDuration());
					talon.setPeakCurrentLimit(options.peakCurrentLimit());

					talon.setSelectedFeedbackSensor(options.feedbackSensor(), BaseMotorControllerWrapper.PidIdx.PRIMARY_CLOSED_LOOP);

					talon.setVoltageCompensationSaturation(options.compSaturationVoltage());
					talon.setVoltageCompensationEnabled(options.enableVoltageCompensation());

					talon.setOpenLoopRamp(options.openLoopRampRate());
					talon.setClosedLoopRamp(options.closedLoopRampRate());

					talon.setSensorPhaseInverted(options.invertSensorPhase());

					talon.setStatusFramePeriod(options.statusFrame(), options.statusFramePeriod());

					talon.setMotionMagicCruiseVelocity(options.motionCruiseVelocity());
					talon.setMotionMagicAcceleration(options.motionAcceleration());

					talon.setContinuousCurrentLimit(options.continuousCurrentLimitAmps());

					talon.setVelocityMeasurementPeriod(options.velocityMeasurementPeriod());
					talon.setVelocityMeasurementWindow(options.velocityMeasurementWindow());

					for (HardLimitSwitch hardLimitSwitch : field.getAnnotationsByType(HardLimitSwitch.class)) {
						System.out.println("\tconfiguring hard limit switch " + hardLimitSwitch.direction());
						talon.setLimitSwitchSource(hardLimitSwitch.direction(), hardLimitSwitch.limitSwitchSource(),
								hardLimitSwitch.limitSwitchNormal());
						talon.setDirectionParameterForLimit(hardLimitSwitch.direction(), hardLimitSwitch.paramValue(),
								hardLimitSwitch.paramSubValue(), hardLimitSwitch.paramOrdinal(),
								hardLimitSwitch.clearOnLimit());
					}

					for (SoftLimit softLimit : field.getAnnotationsByType(SoftLimit.class)) {
						System.out.println("\tconfiguring soft limit " + softLimit.direction());
						talon.setSoftLimit(softLimit.direction(), softLimit.threshold(), softLimit.enabled());
						talon.setDirectionParameterForLimit(softLimit.direction(), softLimit.paramValue(),
								softLimit.paramSubValue(), softLimit.paramOrdinal(), softLimit.clearOnLimit());
						talon.setClearPositionOnLimit(softLimit.direction(), softLimit.clearOnLimit());
					}

					for (PIDProfile profile : field.getAnnotationsByType(PIDProfile.class)) {
						System.out.println("\tconfiguring pid profile " + profile.profile());
						talon.setPIDF(profile.profile(), profile.p(), profile.i(), profile.d(), profile.f());
					}

					Inverted inverted = field.getAnnotation(Inverted.class);
					if (inverted != null)
						talon.setInverted(inverted.type());
					else
						talon.setInverted(InvertType.None);

					for (Slave follower : field.getAnnotationsByType(Slave.class)) {
						System.out.println(
								"\tCreating follower w/ id of " + follower.id() + " of type " + follower.motorControllerType()
										+ " with follow mode " + follower.followMode() + " on " + options.subsystemName());
						switch (follower.motorControllerType()) {
							case VICTOR:
								VictorSPX victorFollower = new VictorSPX(follower.id());
								victorFollower.follow(talon.motorController);
								victorFollower.setInverted(follower.followMode());
								break;
							case TALON:
								TalonWrapper talonFollower = new TalonWrapper(follower.id());
								talonFollower.set(ControlMode.Follower, talon.getDeviceID());
								talonFollower.setInverted(follower.followMode());
								break;
						}
					}

					talon.timeoutMode = BaseMotorControllerWrapper.TimeoutMode.RUNNING;

					field.set(null, talon);

				} catch (CTREException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} else if (field.getType() == VictorWrapper.class) {
				try {
					VictorOptions options = field.getAnnotation(VictorOptions.class);
					System.out.println("Configuring " + options.subsystemName());
					// TODO exception if no options present

					VictorWrapper victor = new VictorWrapper(options.deviceNumber(), options.subsystemName());

					victor.setSelectedFeedbackSensor(options.feedbackSensor(), BaseMotorControllerWrapper.PidIdx.PRIMARY_CLOSED_LOOP);

					victor.setLimitSwitches(options.reverseLimitSwitchEnable());

					victor.setReverseSoftLimit(options.reverseSoftLimit(), options.reverseSoftLimitEnable());

					victor.setVoltageCompensationSaturation(options.compSaturationVoltage());
					victor.setVoltageCompensationEnabled(options.enableVoltageCompensation());

					victor.setOpenLoopRamp(options.openLoopRampRate());
					victor.setClosedLoopRamp(options.closedLoopRampRate());

					victor.setSensorPhaseInverted(options.invertSensorPhase());

					victor.setStatusFramePeriod(options.statusFrame(), options.statusFramePeriod());

					victor.setMotionMagicCruiseVelocity(options.motionCruiseVelocity());
					victor.setMotionMagicAcceleration(options.motionAcceleration());

					victor.setForwardLimitSwitchSource(options.forwardLimitSwitchSource(),
							options.forwardLimitSwitchNormal());

					victor.setVelocityMeasurementPeriod(options.velocityMeasurementPeriod());
					victor.setVelocityMeasurementWindow(options.velocityMeasurementWindow());

					victor.setClearPositionOnLimitF(options.clearOnLimitFwd());

					for (PIDProfile profile : field.getAnnotationsByType(PIDProfile.class)) {
						System.out.println("\tconfiguring pid profile " + profile.profile());
						victor.setPIDF(profile.profile(), profile.p(), profile.i(), profile.d(), profile.f());
					}

					Inverted inverted = field.getAnnotation(Inverted.class);
					if (inverted != null)
						victor.setInverted(inverted.type());
					else
						victor.setInverted(InvertType.None);

					for (Slave follower : field.getAnnotationsByType(Slave.class)) {
						System.out.println(
								"\tCreating follower w/ id of " + follower.id() + " of type " + follower.motorControllerType()
										+ " with follow mode " + follower.followMode() + " on " + options.subsystemName());
						switch (follower.motorControllerType()) {
							case VICTOR:
								VictorSPX victorFollower = new VictorSPX(follower.id());
								victorFollower.follow(victor.motorController);
								victorFollower.setInverted(follower.followMode());
								break;
							case TALON:
								TalonWrapper talonFollower = new TalonWrapper(follower.id());
								talonFollower.set(ControlMode.Follower, victor.getDeviceID());
								talonFollower.setInverted(follower.followMode());
								break;
						}
					}

					victor.timeoutMode = BaseMotorControllerWrapper.TimeoutMode.RUNNING;

					field.set(null, victor);

				} catch (CTREException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	// #endregion

}
