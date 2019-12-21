package com.team2813.lib.drive;

import edu.wpi.first.wpilibj2.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.util.Units;

public class DriveDemand {
    public static double circumference = 1; // default should be set on init
    private final double left;
    private final double right;

    public DriveDemand(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public DriveDemand(DifferentialDriveWheelSpeeds wheelSpeeds) { // to rpm
        left = Units.metersToInches(wheelSpeeds.leftMetersPerSecond) / circumference / 60;
        right = Units.metersToInches(wheelSpeeds.rightMetersPerSecond) / circumference / 60;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }
}
