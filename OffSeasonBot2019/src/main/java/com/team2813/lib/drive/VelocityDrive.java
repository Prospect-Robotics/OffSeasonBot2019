package com.team2813.lib.drive;

import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;

public class VelocityDrive {
    private CANSparkMaxWrapper spark;
    private double maxVelocity;
    private double maxAccel;

    public VelocityDrive(double maxVelocity, double maxAccel) {
        this.maxVelocity = maxVelocity;
        this.maxAccel = maxAccel;
    }

    public void configureMotor(CANSparkMaxWrapper spark) throws SparkMaxException {
        this.spark = spark;
        spark.setPIDF(0, 0.00013, 0, 0, 0);
        spark.getPIDController().setSmartMotionMaxVelocity(maxVelocity, 0);
        spark.getPIDController().setSmartMotionMaxAccel(maxAccel, 0);
        spark.getPIDController().setSmartMotionMinOutputVelocity(0, 0);
        spark.getPIDController().setSmartMotionAllowedClosedLoopError(0, 0);
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
