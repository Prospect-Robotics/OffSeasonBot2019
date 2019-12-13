package com.team2813.lib.angleAdjustment;

public class AngleAdjustment {
    private double initialRight;
    private double finalRight;
    private double initialLeft;
    private double finalLeft;
    private double angleChange;
    private final double GEAR_RATIO = 9/60; // ask Kyle for Gear Ratio
    private final double WHEEL_DIAMETER = 4;
    private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
    private final int PULSES;
    private final int PULSES_PER_REVOLUTION;
    //TODO add a parameter, either WHEEL_DIAMETER or WHEEL_Circumference for later use
    public AngleAdjustment(double initialRight, double initialLeft, double angleChange, int pulses, int pulsesPerRevolution){
        this.initialRight = initialRight;
        this.initialLeft = initialLeft;
        this.angleChange = angleChange;
        this.PULSES_PER_REVOLUTION = pulsesPerRevolution;
        this.PULSES = pulses;
        calculate();
        //FIX pulse / pulse per rev * 360 % 360
        this.angleChange = PULSES/PULSES_PER_REVOLUTION*360%360;
        // 42 counts per rev
        //TODO convert degrees to encoder values which the left and right must travel
    }

    public double getFinalRight() {
        return finalRight;
    }

    public double getFinalLeft() {
        return finalLeft;
    }

    private void calculate() {
        double counts = angleChange / 360 * 42;
        double wheelDegree = counts * GEAR_RATIO;
        this.finalRight = initialRight + wheelDegree;
        this.finalLeft = initialLeft + wheelDegree;
    }
}
