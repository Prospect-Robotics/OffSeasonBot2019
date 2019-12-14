package com.team2813.lib.angleAdjustment;

public class AngleAdjustment {
    private double initialRight;
    private double finalRight;
    private double initialLeft;
    private double finalLeft;
    private double angleChange;
    private final double GEAR_RATIO;
    private final double WHEEL_DIAMETER;
    private final double WHEEL_CIRCUMFERENCE;
    private final int PULSES_PER_REVOLUTION;
    //TODO add a parameter, either WHEEL_DIAMETER
    public AngleAdjustment(double initialRight, double initialLeft, double angleChange,  int pulsesPerRevolution, double wheelDiameter, double gearRatio){
        this.initialRight = initialRight;
        this.initialLeft = initialLeft;
        this.angleChange = angleChange;
        PULSES_PER_REVOLUTION = pulsesPerRevolution;
        WHEEL_DIAMETER = wheelDiameter;
        WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
        GEAR_RATIO = gearRatio;
        calculate();
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
