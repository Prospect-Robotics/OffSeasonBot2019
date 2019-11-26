package com.team2813.lib.config;


import com.team2813.lib.sparkMax.CANSparkMaxWrapper.InvertType;

public class FollowerConfig {
    private int id;
    private MotorType type;
    private InvertType inverted = InvertType.FOLLOW_LEADER;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MotorType getType() {
        return type;
    }

    public void setType(MotorType type) {
        this.type = type;
    }

    public InvertType getInverted() {
        return inverted;
    }

    public void setInverted(InvertType inverted) {
        this.inverted = inverted;
    }
}
