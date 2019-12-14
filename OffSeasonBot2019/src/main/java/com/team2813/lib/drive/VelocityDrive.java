package com.team2813.lib.drive;

import com.team2813.lib.config.PIDControllerConfig;
import com.team2813.lib.config.SparkConfig;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;

public class VelocityDrive {
    private CANSparkMaxWrapper spark;
    private double maxVelocity;

    public VelocityDrive(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public void configureMotor(CANSparkMaxWrapper spark, SparkConfig config) throws SparkMaxException {
        this.spark = spark;
        PIDControllerConfig pidConfig = config.getPidControllers().get(0);
        spark.setPIDF(0, pidConfig.getP(), pidConfig.getI(), pidConfig.getD(), pidConfig.getF());
        spark.getPIDController().setSmartMotionMaxVelocity(pidConfig.getMaxVelocity(), 0);
        spark.getPIDController().setSmartMotionMaxAccel(pidConfig.getMaxAcceleration(), 0);
        spark.getPIDController().setSmartMotionMinOutputVelocity(0, 0);
        spark.getPIDController().setSmartMotionAllowedClosedLoopError(pidConfig.getAllowableClosedLoopError(), 0);
    }

    public void setMaxVelocity(int maxVelocity) {
        if (maxVelocity != this.maxVelocity)
            spark.getPIDController().setSmartMotionMaxVelocity(maxVelocity, 0);
        this.maxVelocity = maxVelocity;
    }

//    public void setAccelerating(boolean accelerating) {
//        if (accelerating) {
//            spark.getPIDController().setSmartMotionMaxAccel(maxAccel, 0);
//        } else {
//            spark.getPIDController().setSmartMotionMaxAccel(maxAccel * .4, 0);
//        }
//    }

    public double getVelocityFromDemand(double demand) {
        return maxVelocity * demand;
    }
}
