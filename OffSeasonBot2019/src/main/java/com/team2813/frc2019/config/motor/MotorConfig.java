package com.team2813.frc2019.config.motor;

import java.util.ArrayList;
import java.util.List;

public class MotorConfig {

    private int deviceNumber;
    private MotorType type;
    private String subsystemName;
    private int peakCurrentDuration;
    private int peakCurrentLimit;
    private boolean enableVoltageCompensation;
    private int compSaturationVoltage;
    private int continuousCurrentLimitAmps;
    private int motionAcceleration;
    private int motionCruiseVelocity;
    private int closedLoopRampRate;
    private int openLoopRampRate;
    private boolean invertSensorPhase;
    private PeriodicFrame statusFrame; // cannot serialize into PeriodicFrame (see getStatusFrame)
    private int statusFramePeriod = 5;
    private List<FollowerConfig> followers = new ArrayList<>();
    private Inverted inverted;
    private List<PIDControllerConfig> pidControllers;

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public MotorType getType() {
        return type;
    }

    public void setType(MotorType type) {
        this.type = type;
    }

    public String getSubsystemName() {
        return subsystemName;
    }

    public void setSubsystemName(String subsystemName) {
        this.subsystemName = subsystemName;
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

    public int getClosedLoopRampRate() {
        return closedLoopRampRate;
    }

    public void setClosedLoopRampRate(int closedLoopRampRate) {
        this.closedLoopRampRate = closedLoopRampRate;
    }

    public int getOpenLoopRampRate() {
        return openLoopRampRate;
    }

    public void setOpenLoopRampRate(int openLoopRampRate) {
        this.openLoopRampRate = openLoopRampRate;
    }

    public boolean isInvertSensorPhase() {
        return invertSensorPhase;
    }

    public void setInvertSensorPhase(boolean invertSensorPhase) {
        this.invertSensorPhase = invertSensorPhase;
    }

    public PeriodicFrame getStatusFrame() {
        return this.statusFrame;
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

    public List<FollowerConfig> getFollowers() {
        return followers;
    }

    public void setFollowers(List<FollowerConfig> followers) {
        this.followers = followers;
    }

    public Inverted getInverted() {
        return inverted;
    }

    public void setInverted(Inverted inverted) {
        this.inverted = inverted;
    }

    public List<PIDControllerConfig> getPidControllers() {
        return pidControllers;
    }

    public void setPidControllers(List<PIDControllerConfig> pidControllers) {
        this.pidControllers = pidControllers;
    }
}
