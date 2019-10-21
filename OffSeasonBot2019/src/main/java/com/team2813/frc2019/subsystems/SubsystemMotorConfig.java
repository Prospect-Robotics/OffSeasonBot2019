package com.team2813.frc2019.subsystems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.team2813.frc2019.config.motor.FollowerConfig;
import com.team2813.frc2019.config.motor.Inverted;
import com.team2813.frc2019.config.motor.MotorConfig;
import com.team2813.lib.logging.LogLevel;
import com.team2813.lib.logging.Logger;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import edu.wpi.first.wpilibj.Filesystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SubsystemMotorConfig {

//	@com.team2813.lib.sparkMax.options.Inverted
//	@SparkMaxOptions(
//		deviceNumber = 3,
//		type = MotorType.kBrushless,
//		subsystemName = "Drive",
//		peakCurrentDuration = 0,
//		peakCurrentLimit = 0,
//		enableVoltageCompensation = true,
//		compSaturationVoltage = 12,
//		continuousCurrentLimitAmps = 30,
//		motionAcceleration = 0,
//		motionCruiseVelocity = 0,
//		closedLoopRampRate = 0,
//		openLoopRampRate = 0,
//		invertSensorPhase = true,
//		statusFrame = PeriodicFrame.kStatus2
//	)
//	@Follower(
//		id = 4,
//		type = MotorType.kBrushless
//	)
//	static CANSparkMaxWrapper driveLeft;
//
//	@SparkMaxOptions(
//		deviceNumber = 1,
//		type = MotorType.kBrushless,
//		subsystemName = "Drive",
//		peakCurrentDuration = 0,
//		peakCurrentLimit = 0,
//		enableVoltageCompensation = true,
//		compSaturationVoltage = 12,
//		continuousCurrentLimitAmps = 30,
//		motionAcceleration = 0,
//		motionCruiseVelocity = 0,
//		closedLoopRampRate = 0,
//		openLoopRampRate = 0,
//		invertSensorPhase = true,
//		statusFrame = PeriodicFrame.kStatus2
//	)
//	@Follower(
//		id = 2,
//		type = MotorType.kBrushless
//	)
//	static CANSparkMaxWrapper driveRight;

    //#region Spark Max Initialization

    static Map<String, MotorConfig> motorConfigs;

    static CANSparkMaxWrapper driveLeft;
    static CANSparkMaxWrapper driveRight;

    static List<Integer> sparkIds = new ArrayList<>();

    static {
        try {
            File deployDirectory = Filesystem.getDeployDirectory();
            File configFile = new File(deployDirectory.getAbsolutePath() + "/motorConfig.yaml");

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            motorConfigs = mapper.readValue(configFile, MotorConfigs.class).getMotors();

            driveLeft = initializeSpark("driveLeft");
            driveRight = initializeSpark("driveRight");
        } catch (IOException e) {
            Logger.log(LogLevel.ERROR, "Unable to read config");
            e.printStackTrace();
        }
    }

    private static CANSparkMaxWrapper initializeSpark(String name) {
        MotorConfig options = motorConfigs.get(name);
        System.out.println("Configuring " + options.getSubsystemName());
        // TODO exception if no options present

        for (Integer id : sparkIds)
            if (id == options.getDeviceNumber()) {
                System.err.println("Tried to register spark max with already used id");
            }

        sparkIds.add(options.getDeviceNumber());


        CANSparkMaxWrapper spark = new CANSparkMaxWrapper(options.getDeviceNumber(), options.getSubsystemName(), options.getType().getValue());

        try {
            spark.factoryDefault();

//					spark.setPeakCurrentDuration(options.peakCurrentDuration()); // FIXME: 09/20/2019 for the spark
            spark.setCurrLimit(options.getPeakCurrentLimit());

            spark.enableVoltageCompensation(options.getCompSaturationVoltage());

            spark.setOpenLoopRamp(options.getOpenLoopRampRate());
            spark.setClosedLoopRamp(options.getClosedLoopRampRate());

            spark.setPeriodicFrame(options.getStatusFrame().getValue(), options.getStatusFramePeriod());

//					spark.setSmartMotionMaxVelocity(options.motionCruiseVelocity()); // FIXME: 09/20/2019 need to change parameters/types
//					spark.setSmartMotionMaxAccel(options.motionAcceleration()); // FIXME: 09/20/2019 need to change parameters/types


            spark.setSecondaryCurrLimit(options.getContinuousCurrentLimitAmps());// TODO check this is actually continuous limit

//			for (com.team2813.lib.sparkMax.options.HardLimitSwitch hardLimitSwitch : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.HardLimitSwitch.class)) {
//				System.out.println("\tconfiguring hard limit switch " + hardLimitSwitch.direction());
//				// FIXME remake limit switch stuff differently since it is called differently
//			}
//
//			for (com.team2813.lib.sparkMax.options.SoftLimit softLimit : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.SoftLimit.class)) {
//				System.out.println("\tconfiguring soft limit " + softLimit.direction());
//
//				//FIXME remake limit switch stuff differently
//			}

            // TODO Implement PID Controllers
//			for (com.team2813.lib.sparkMax.options.PIDProfile profile : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.PIDProfile.class)) {
//				System.out.println("\tConfiguring pid profile " + profile.profile());
//				spark.setPIDF(profile.profile().id, profile.p(), profile.i(), profile.d(), profile.f());
//			}

            Inverted inverted = options.getInverted();
            if (inverted != null)
                spark.setInverted(inverted == Inverted.INVERTED);
            else
                spark.setInverted(com.team2813.lib.sparkMax.options.InvertType.NORMAL.inverted);

            for (FollowerConfig followerConfig : options.getFollowers()) {
                System.out.println(
                        "\tCreating follower w/ id of " + followerConfig.getId() + " on " + options.getSubsystemName()
                );
                CANSparkMaxWrapper sparkMaxFollower = new CANSparkMaxWrapper(followerConfig.getId(), followerConfig.getType().getValue());
                sparkMaxFollower.follow(spark, followerConfig.getInverted().inverted);
            }
        } catch (SparkMaxException e) {
            e.printStackTrace();
        }

        return spark;
    }

    //#endregion

    //#region Talon Initialization
//	static {
//		initializeTalons(); // TODO call from somewhere else
//	}
//
//	private static void initializeTalons() {
//		List<Integer> talonIds = new ArrayList<>();
//		for (Field field : SubsystemMotorConfig.class.getDeclaredFields()) {
//			if (field.getType() == TalonWrapper.class) {
//				try {
//					TalonOptions options = field.getAnnotation(TalonOptions.class);
//					System.out.println("Configuring " + options.subsystemName());
//					// TODO exception if no options present
//
//					for (Integer id : talonIds)
//						if (id == options.deviceNumber()) {
//							System.err.println("Tried to register talon with already used id");
//						}
//
//					talonIds.add(options.deviceNumber());
//
//					TalonWrapper talon = new TalonWrapper(options.deviceNumber(), options.subsystemName());
//
//					talon.setFactoryDefaults();
//
//					talon.setPeakCurrentDuration(options.peakCurrentDuration());
//					talon.setPeakCurrentLimit(options.peakCurrentLimit());
//
//					talon.setSelectedFeedbackSensor(options.feedbackSensor(), BaseMotorControllerWrapper.PidIdx.PRIMARY_CLOSED_LOOP);
//
//					talon.setVoltageCompensationSaturation(options.compSaturationVoltage());
//					talon.setVoltageCompensationEnabled(options.enableVoltageCompensation());
//
//					talon.setOpenLoopRamp(options.openLoopRampRate());
//					talon.setClosedLoopRamp(options.closedLoopRampRate());
//
//					talon.setSensorPhaseInverted(options.invertSensorPhase());
//
//					talon.setStatusFramePeriod(options.statusFrame(), options.statusFramePeriod());
//
//					talon.setMotionMagicCruiseVelocity(options.motionCruiseVelocity());
//					talon.setMotionMagicAcceleration(options.motionAcceleration());
//
//					talon.setContinuousCurrentLimit(options.continuousCurrentLimitAmps());
//
//					talon.setVelocityMeasurementPeriod(options.velocityMeasurementPeriod());
//					talon.setVelocityMeasurementWindow(options.velocityMeasurementWindow());
//
//					for (HardLimitSwitch hardLimitSwitch : field.getAnnotationsByType(HardLimitSwitch.class)) {
//						System.out.println("\tconfiguring hard limit switch " + hardLimitSwitch.direction());
//						talon.setLimitSwitchSource(hardLimitSwitch.direction(), hardLimitSwitch.limitSwitchSource(),
//								hardLimitSwitch.limitSwitchNormal());
//						talon.setDirectionParameterForLimit(hardLimitSwitch.direction(), hardLimitSwitch.paramValue(),
//								hardLimitSwitch.paramSubValue(), hardLimitSwitch.paramOrdinal(),
//								hardLimitSwitch.clearOnLimit());
//					}
//
//					for (SoftLimit softLimit : field.getAnnotationsByType(SoftLimit.class)) {
//						System.out.println("\tconfiguring soft limit " + softLimit.direction());
//						talon.setSoftLimit(softLimit.direction(), softLimit.threshold(), softLimit.enabled());
//						talon.setDirectionParameterForLimit(softLimit.direction(), softLimit.paramValue(),
//								softLimit.paramSubValue(), softLimit.paramOrdinal(), softLimit.clearOnLimit());
//						talon.setClearPositionOnLimit(softLimit.direction(), softLimit.clearOnLimit());
//					}
//
//					for (PIDProfile profile : field.getAnnotationsByType(PIDProfile.class)) {
//						System.out.println("\tconfiguring pid profile " + profile.profile());
//						talon.setPIDF(profile.profile(), profile.p(), profile.i(), profile.d(), profile.f());
//					}
//
//					Inverted inverted = field.getAnnotation(Inverted.class);
//					if (inverted != null)
//						talon.setInverted(inverted.type());
//					else
//						talon.setInverted(InvertType.None);
//
//					for (Slave follower : field.getAnnotationsByType(Slave.class)) {
//						System.out.println(
//								"\tCreating follower w/ id of " + follower.id() + " of type " + follower.motorControllerType()
//										+ " with follow mode " + follower.followMode() + " on " + options.subsystemName());
//						switch (follower.motorControllerType()) {
//							case VICTOR:
//								VictorSPX victorFollower = new VictorSPX(follower.id());
//								victorFollower.follow(talon.motorController);
//								victorFollower.setInverted(follower.followMode());
//								break;
//							case TALON:
//								TalonWrapper talonFollower = new TalonWrapper(follower.id());
//								talonFollower.set(ControlMode.Follower, talon.getDeviceID());
//								talonFollower.setInverted(follower.followMode());
//								break;
//						}
//					}
//
//					talon.timeoutMode = BaseMotorControllerWrapper.TimeoutMode.RUNNING;
//
//					field.set(null, talon);
//
//				} catch (CTREException e) {
//					e.printStackTrace();
//				} catch (IllegalArgumentException | IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			} else if (field.getType() == VictorWrapper.class) {
//				try {
//					VictorOptions options = field.getAnnotation(VictorOptions.class);
//					System.out.println("Configuring " + options.subsystemName());
//					// TODO exception if no options present
//
//					VictorWrapper victor = new VictorWrapper(options.deviceNumber(), options.subsystemName());
//
//					victor.setSelectedFeedbackSensor(options.feedbackSensor(), BaseMotorControllerWrapper.PidIdx.PRIMARY_CLOSED_LOOP);
//
//					victor.setLimitSwitches(options.reverseLimitSwitchEnable());
//
//					victor.setReverseSoftLimit(options.reverseSoftLimit(), options.reverseSoftLimitEnable());
//
//					victor.setVoltageCompensationSaturation(options.compSaturationVoltage());
//					victor.setVoltageCompensationEnabled(options.enableVoltageCompensation());
//
//					victor.setOpenLoopRamp(options.openLoopRampRate());
//					victor.setClosedLoopRamp(options.closedLoopRampRate());
//
//					victor.setSensorPhaseInverted(options.invertSensorPhase());
//
//					victor.setStatusFramePeriod(options.statusFrame(), options.statusFramePeriod());
//
//					victor.setMotionMagicCruiseVelocity(options.motionCruiseVelocity());
//					victor.setMotionMagicAcceleration(options.motionAcceleration());
//
//					victor.setForwardLimitSwitchSource(options.forwardLimitSwitchSource(),
//							options.forwardLimitSwitchNormal());
//
//					victor.setVelocityMeasurementPeriod(options.velocityMeasurementPeriod());
//					victor.setVelocityMeasurementWindow(options.velocityMeasurementWindow());
//
//					victor.setClearPositionOnLimitF(options.clearOnLimitFwd());
//
//					for (PIDProfile profile : field.getAnnotationsByType(PIDProfile.class)) {
//						System.out.println("\tconfiguring pid profile " + profile.profile());
//						victor.setPIDF(profile.profile(), profile.p(), profile.i(), profile.d(), profile.f());
//					}
//
//					Inverted inverted = field.getAnnotation(Inverted.class);
//					if (inverted != null)
//						victor.setInverted(inverted.type());
//					else
//						victor.setInverted(InvertType.None);
//
//					for (Slave follower : field.getAnnotationsByType(Slave.class)) {
//						System.out.println(
//								"\tCreating follower w/ id of " + follower.id() + " of type " + follower.motorControllerType()
//										+ " with follow mode " + follower.followMode() + " on " + options.subsystemName());
//						switch (follower.motorControllerType()) {
//							case VICTOR:
//								VictorSPX victorFollower = new VictorSPX(follower.id());
//								victorFollower.follow(victor.motorController);
//								victorFollower.setInverted(follower.followMode());
//								break;
//							case TALON:
//								TalonWrapper talonFollower = new TalonWrapper(follower.id());
//								talonFollower.set(ControlMode.Follower, victor.getDeviceID());
//								talonFollower.setInverted(follower.followMode());
//								break;
//						}
//					}
//
//					victor.timeoutMode = BaseMotorControllerWrapper.TimeoutMode.RUNNING;
//
//					field.set(null, victor);
//
//				} catch (CTREException e) {
//					e.printStackTrace();
//				} catch (IllegalArgumentException | IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
    // #endregion

    private static class MotorConfigs {
        private Map<String, MotorConfig> motors;

        public Map<String, MotorConfig> getMotors() {
            return motors;
        }

        public void setMotors(Map<String, MotorConfig> motors) {
            this.motors = motors;
        }
    }

}
