package com.team2813.lib.drive;

public class CurvatureDrive {
    private ArcadeDrive arcadeDrive;

    public CurvatureDrive(double deadzone) {
        arcadeDrive = new ArcadeDrive(deadzone);
    }

    public DriveDemand getDemand(double throttleForward, double throttleBackward, double steerX, boolean pivot) {
        double throttle =  2 * Math.asin(throttleForward - throttleBackward) / Math.PI;
        double steer = 2 * Math.asin(steerX) / Math.PI;

        steer = -steer;
        return arcadeDrive.getDemand(pivot ? steer * .3 : throttle * steer, throttle);
    }

    public ArcadeDrive getArcadeDrive() {
        return arcadeDrive;
    }
}
