package com.team2813.frc2019.config.motor;

import com.team2813.lib.sparkMax.options.PIDProfile;

public class PIDControllerConfig {
    private PIDProfile.Profile profile;
    private double p;
    private double i;
    private double d;
    private double f;
    private double maxIntegralAccumulator;
    private double integralZone;
    private double allowableClosedLoopError;

    public PIDProfile.Profile getProfile() {
        return profile;
    }

    public void setProfile(PIDProfile.Profile profile) {
        this.profile = profile;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getMaxIntegralAccumulator() {
        return maxIntegralAccumulator;
    }

    public void setMaxIntegralAccumulator(double maxIntegralAccumulator) {
        this.maxIntegralAccumulator = maxIntegralAccumulator;
    }

    public double getIntegralZone() {
        return integralZone;
    }

    public void setIntegralZone(double integralZone) {
        this.integralZone = integralZone;
    }

    public double getAllowableClosedLoopError() {
        return allowableClosedLoopError;
    }

    public void setAllowableClosedLoopError(double allowableClosedLoopError) {
        this.allowableClosedLoopError = allowableClosedLoopError;
    }
}
