package com.team2813.lib.config;

import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.team2813.lib.talon.BaseMotorControllerWrapper;
import java.util.ArrayList;
import java.util.List;

public class TalonConfig extends MotorConfig {

    private int deviceNumber;
    private int peakCurrentDuration;
    private int peakCurrentLimit;
    private boolean enableVoltageCompensation;
    private int compSaturationVoltage;
    private int continuousCurrentLimitAmps;
    private int motionAcceleration;
    private int motionCruiseVelocity;
    private double closedLoopRampRate;
    private double openLoopRampRate;
    private boolean invertSensorPhase;
    private Inverted inverted;
    private List<FollowerConfig> followers = new ArrayList<>();
    private PeriodicFrame statusFrame; // cannot serialize into PeriodicFrame (see getStatusFrame)
    private int statusFramePeriod = 5;
    private VelocityMeasPeriod velocityMeasurementPeriod;
    private BaseMotorControllerWrapper.VelocityMeasurementWindow velocityMeasurementWindow;

    @Override
    public int getDeviceNumber() {
        return deviceNumber;
    }

    @Override
    public void setDeviceNumber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getPeakCurrentDuration() {
        return peakCurrentDuration;
    }

    public void setPeakCurrentDuration(int peakCurrentDuration) {
        this.peakCurrentDuration = peakCurrentDuration;
    }

    public int getPeakCurrentLimit() {
        return peakCurrentLimit;
    }

    public void setPeakCurrentLimit(int peakCurrentLimit) {
        this.peakCurrentLimit = peakCurrentLimit;
    }

    public boolean isEnableVoltageCompensation() {
        return enableVoltageCompensation;
    }

    public void setEnableVoltageCompensation(boolean enableVoltageCompensation) {
        this.enableVoltageCompensation = enableVoltageCompensation;
    }

    public int getCompSaturationVoltage() {
        return compSaturationVoltage;
    }

    public void setCompSaturationVoltage(int compSaturationVoltage) {
        this.compSaturationVoltage = compSaturationVoltage;
    }

    public int getContinuousCurrentLimitAmps() {
        return continuousCurrentLimitAmps;
    }

    public void setContinuousCurrentLimitAmps(int continuousCurrentLimitAmps) {
        this.continuousCurrentLimitAmps = continuousCurrentLimitAmps;
    }

    public int getMotionAcceleration() {
        return motionAcceleration;
    }

    public void setMotionAcceleration(int motionAcceleration) {
        this.motionAcceleration = motionAcceleration;
    }

    public int getMotionCruiseVelocity() {
        return motionCruiseVelocity;
    }

    public void setMotionCruiseVelocity(int motionCruiseVelocity) {
        this.motionCruiseVelocity = motionCruiseVelocity;
    }

    public double getClosedLoopRampRate() {
        return closedLoopRampRate;
    }

    public void setClosedLoopRampRate(double closedLoopRampRate) {
        this.closedLoopRampRate = closedLoopRampRate;
    }

    public double getOpenLoopRampRate() {
        return openLoopRampRate;
    }

    public void setOpenLoopRampRate(double openLoopRampRate) {
        this.openLoopRampRate = openLoopRampRate;
    }

    public boolean isInvertSensorPhase() {
        return invertSensorPhase;
    }

    public void setInvertSensorPhase(boolean invertSensorPhase) {
        this.invertSensorPhase = invertSensorPhase;
    }

    public PeriodicFrame getStatusFrame() {
        return statusFrame;
    }

    public void setStatusFrame(PeriodicFrame statusFrame) {
        this.statusFrame = statusFrame;
    }

    public int getStatusFramePeriod() {
        return statusFramePeriod;
    }

    public void setStatusFramePeriod(int statusFramePeriod) {
        this.statusFramePeriod = statusFramePeriod;
    }

    public VelocityMeasPeriod getVelocityMeasurementPeriod() {
        return velocityMeasurementPeriod;
    }

    public void setVelocityMeasurementPeriod(VelocityMeasPeriod velocityMeasurementPeriod) {
        this.velocityMeasurementPeriod = velocityMeasurementPeriod;
    }

    public BaseMotorControllerWrapper.VelocityMeasurementWindow getVelocityMeasurementWindow() {
        return velocityMeasurementWindow;
    }


    public void getPidControllers() {
    }

    public Inverted getInverted() {
        return inverted;
    }

    public List<FollowerConfig> getFollowers() {
        return followers;
    }
}
