package com.team2813.lib.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import com.team2813.lib.talon.TalonWrapper;
import com.team2813.lib.talon.VictorWrapper;
import com.team2813.lib.talon.options.InvertType;
import com.team2813.lib.talon.options.PIDProfile;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Talon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// singleton for motor configs
public class MotorConfigs {
    public static Map<String, CANSparkMaxWrapper> sparks = new HashMap<>();
    public static Map<String, TalonConfig> talons = new HashMap<>();
    public static Map<String, VictorWrapper> victors = new HashMap<>();

    private static List<Integer> ids = new ArrayList<>();

    public static void read() throws IOException {
        File deployDirectory = Filesystem.getDeployDirectory();
        File configFile = new File(deployDirectory.getAbsolutePath() + "/motorConfig.yaml");

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        RootConfigs motorConfigs = mapper.readValue(configFile, RootConfigs.class);

        motorConfigs.getSparks().forEach(((s, sparkConfig) -> sparks.put(s, initializeSpark(sparkConfig))));
        motorConfigs.getVictors().forEach(((s, victorConfig) -> victors.put(s, initializeVictor(victorConfig))));

        System.out.println("Successful!");
    }

    private static <TalonException extends Throwable> TalonWrapper initializeTalon(TalonConfig config) throws TalonException, CTREException, SparkMaxException {
        for (Integer id : ids)
            if (id == config.getDeviceNumber()){
                System.err.println("Tried to register talon with already used id");
            }
        ids.add(config.getDeviceNumber());

        System.out.println("Configuring" + config.getSubsystemName());

        TalonWrapper talon = new TalonWrapper(config.getDeviceNumber(), config.getSubsystemName(), config.getMotorType().getValue());

        talon.setFactoryDefaults();

//                talon.setPeakCurrentDuration(config.getPeakCurrentDuration());
        talon.setCurrLimit(config.getPeakCurrentLimit());
        talon.enableVoltageCompensation();


        talon.setOpenLoopRamp(config.getOpenLoopRampRate());
        talon.setClosedLoopRamp(config.getClosedLoopRampRate());

        talon.setPeriodicFrame(config.getStatusFrame(), config.getStatusFramePeriod());
//					talon.setSmartMotionMaxVelocity(config.motionCruiseVelocity()); // FIXME: 09/20/2019 need to change parameters/types
//					talon.setSmartMotionMaxAccel(config.motionAcceleration()); // FIXME: 09/20/2019 need to change parameters/types

        talon.setSecondaryCurrLimit(config.getContinuousCurrentLimitAmps());// TODO check this is actually continuous limit

//			for (com.team2813.lib.talon.options.HardLimitSwitch hardLimitSwitch : field.getAnnotationsByType(com.team2813.lib.talon.options.HardLimitSwitch.class)) {
//				System.out.println("\tconfiguring hard limit switch " + hardLimitSwitch.direction());
//				// FIXME remake limit switch stuff differently since it is called differently -- Grady 10/30 I'm not sure this is how it works for Spark Maxs
//			}
//
//			for (com.team2813.lib.talon.options.SoftLimit softLimit : field.getAnnotationsByType(com.team2813co.lib.talon.options.SoftLimit.class)) {
//				System.out.println("\tconfiguring soft limit " + softLimit.direction());
//
//				//FIXME remake limit switch stuff differently
//			}


        for (PIDControllerConfig pidController :  config.getPidControllers()) {
            PIDProfile.Profile slotID = config.getPidControllers().indexOf(pidController) == 0 ?
                    PIDProfile.Profile.PRIMARY : PIDProfile.Profile.SECONDARY;
            talon.setPIDF(slotID, pidController.getP(), pidController.getI(),
                    pidController.getD(), pidController.getF());
            talon.getPIDController().setSmartMotionMaxVelocity(pidController.getMaxVelocity(), slotID);
            talon.getPIDController().setSmartMotionMaxAccel(pidController.getMaxAcceleration(), slotID);
            talon.getPIDController().setSmartMotionMinOutputVelocity(pidController.getMinVelocity(), slotID);
        }


        Inverted inverted = config.getInverted();
        if (inverted != null)
            talon.setInverted(inverted == Inverted.INVERTED);

        for (FollowerConfig followerConfig : config.getFollowers()) {
            System.out.println(
                    "\tCreating follower w/ id of " + followerConfig.getId() + " on " + config.getSubsystemName()
            );
            CANSparkMaxWrapper talonFollower = new CANSparkMaxWrapper(followerConfig.getId(), followerConfig.getType().getValue());
            talonFollower.follow(talon, followerConfig.getInverted().inverted);
        }

        return talon;
        }

    private static CANSparkMaxWrapper initializeSpark(SparkConfig config) {
        for (Integer id : ids)
            if (id == config.getDeviceNumber()) {
                System.err.println("Tried to register spark max with already used id");
            }

        ids.add(config.getDeviceNumber());

        System.out.println("Configuring " + config.getSubsystemName());

        CANSparkMaxWrapper spark = new CANSparkMaxWrapper(config.getDeviceNumber(), config.getSubsystemName(), config.getType().getValue());

        try {
            spark.factoryDefault();

//					spark.setPeakCurrentDuration(config.peakCurrentDuration()); // FIXME: 09/20/2019 for the spark
            spark.setCurrLimit(config.getPeakCurrentLimit());

            spark.enableVoltageCompensation(config.getCompSaturationVoltage());

            spark.setOpenLoopRamp(config.getOpenLoopRampRate());
            spark.setClosedLoopRamp(config.getClosedLoopRampRate());

            spark.setPeriodicFrame(config.getStatusFrame().getValue(), config.getStatusFramePeriod());

//					spark.setSmartMotionMaxVelocity(config.motionCruiseVelocity()); // FIXME: 09/20/2019 need to change parameters/types
//					spark.setSmartMotionMaxAccel(config.motionAcceleration()); // FIXME: 09/20/2019 need to change parameters/types

            spark.setSecondaryCurrLimit(config.getContinuousCurrentLimitAmps());// TODO check this is actually continuous limit

//			for (com.team2813.lib.sparkMax.options.HardLimitSwitch hardLimitSwitch : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.HardLimitSwitch.class)) {
//				System.out.println("\tconfiguring hard limit switch " + hardLimitSwitch.direction());
//				// FIXME remake limit switch stuff differently since it is called differently -- Grady 10/30 I'm not sure this is how it works for Spark Maxs
//			}
//
//			for (com.team2813.lib.sparkMax.options.SoftLimit softLimit : field.getAnnotationsByType(com.team2813.lib.sparkMax.options.SoftLimit.class)) {
//				System.out.println("\tconfiguring soft limit " + softLimit.direction());
//
//				//FIXME remake limit switch stuff differently
//			}


            for (PIDControllerConfig pidController : config.getPidControllers()) {
                int slotID = config.getPidControllers().indexOf(pidController);
                spark.setPIDF(slotID, pidController.getP(), pidController.getI(),
                        pidController.getD(), pidController.getF());
                spark.getPIDController().setSmartMotionMaxVelocity(pidController.getMaxVelocity(), slotID);
                spark.getPIDController().setSmartMotionMaxAccel(pidController.getMaxAcceleration(), slotID);
                spark.getPIDController().setSmartMotionMinOutputVelocity(pidController.getMinVelocity(), slotID);
            }


            Inverted inverted = config.getInverted();
            if (inverted != null)
                spark.setInverted(inverted == Inverted.INVERTED);
            else
                spark.setInverted(com.team2813.lib.sparkMax.options.InvertType.NORMAL.inverted);

            for (FollowerConfig followerConfig : config.getFollowers()) {
                System.out.println(
                        "\tCreating follower w/ id of " + followerConfig.getId() + " on " + config.getSubsystemName()
                );
                CANSparkMaxWrapper sparkMaxFollower = new CANSparkMaxWrapper(followerConfig.getId(), followerConfig.getType().getValue());
                sparkMaxFollower.follow(spark, followerConfig.getInverted().inverted);
            }
        } catch (SparkMaxException e) {
            e.printStackTrace();
        }

        return spark;
    }

    private static VictorWrapper initializeVictor(VictorConfig config) {

        for (Integer id : ids)
            if (id == config.getDeviceNumber()) {
                System.err.println("Tried to register VICTOR SPX with already used id");
            }

        ids.add(config.getDeviceNumber());

        System.out.println("Configuring " + config.getSubsystemName());
        // TODO IMPLEMENT OPTIONS
        return new VictorWrapper(config.getDeviceNumber(), config.getSubsystemName());
    }
}

@SuppressWarnings({"unused", "WeakerAccess"})
class RootConfigs {
    private Map<String, SparkConfig> sparks;
    private Map<String, TalonConfig> talons;
    private Map<String, VictorConfig> victors;

    public Map<String, SparkConfig> getSparks() {
        return sparks;
    }

    public void setSparks(Map<String, SparkConfig> sparks) {
        this.sparks = sparks;
    }

    public Map<String, TalonConfig> getTalons() {
        return talons;
    }

    public void setTalons(Map<String, TalonConfig> talons) {
        this.talons = talons;
    }

    public Map<String, VictorConfig> getVictors() {
        return victors;
    }

    public void setVictors(Map<String, VictorConfig> victors) {
        this.victors = victors;
    }
}
