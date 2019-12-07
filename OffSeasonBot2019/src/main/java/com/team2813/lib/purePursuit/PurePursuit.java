package com.team2813.lib.purePursuit;


public class PurePursuit {
    public void getStarterValue(double rightEncoderVal, double leftEncoderVal){
        double currentRightVal = 0.0;
        double currentLeftVal = 0.0;
        double robot_angle = 0.0; //radians
        double deltaRightVal = currentRightVal - rightEncoderVal;
        double deltaLeftVal = currentLeftVal - leftEncoderVal;
        double dist = (deltaRightVal + deltaLeftVal)/2;
        double xVal = dist * Math.cos(robot_angle);
        double yVal = dist * Math.sin(robot_angle);
    }
}
