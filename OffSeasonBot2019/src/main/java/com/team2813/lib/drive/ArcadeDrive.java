package com.team2813.lib.drive;

public class ArcadeDrive {
    private double deadzone;

    public ArcadeDrive(double deadzone) {
        this.deadzone = deadzone;
    }

    public DriveDemand getDemand(double x, double y) {
        double maxPercent = 1.0;
        double throttleLeft = 0;
        double throttleRight = 0;

        double steer = 0;

        if (Math.abs(y) > deadzone) { // dead zone
            throttleLeft = maxPercent * y;
            throttleRight = maxPercent * y;
        }

        if (Math.abs(x) > deadzone) {
            double xMax = 0.4;
            steer = 1.0 * x;
        }
//
//        System.out.println(throttleLeft + " " + throttleRight);

//        System.out.println((throttleLeft - steer) + " " + (throttleLeft + steer));

        return new DriveDemand(throttleLeft + steer, throttleRight - steer);
    }

}
