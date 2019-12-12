package com.team2813.lib.angleAdjustment;

public class AngleAdjustment {
    private double initialRight;
    private double finalRight;
    private double initialLeft;
    private double finalLeft;
    private double angleChange;
    private final double GEAR_RATIO = 1; //TODO ask Kyle for Gear Ratio
    private final double WHEEL_DIAMETER = 4;
    private final double WHEEL_CIRCUMFERENCE = 4 * Math.PI;

    public AngleAdjustment(double initialRight, double initialLeft, double angleChange){
        this.initialRight = initialRight;
        this.initialLeft = initialLeft;
        this.angleChange = angleChange;
        calculate();
        //TODO pulse / pulse per rev * 360 % 360
        // 42 counts per rev
        //TODO convert degrees to encoder values which the left and right must travel

    }

    public double getFinalRight() {
        return finalRight;
    }

    public double getFinalLeft() {
        return finalLeft;
    }

    private double calculate() {
        double counts = angleChange / 360 * 42;
        double wheelDegree = counts * GEAR_RATIO;
        this.finalRight = initialRight + wheelDegree;
        this.finalLeft = initialLeft + wheelDegree;
    }
}