package com.team2813.frc2019.config.motor;

import com.revrobotics.CANSparkMaxLowLevel;

public class FollowerConfig {
    private int id;
    private CANSparkMaxLowLevel.MotorType type;
    private Inverted inverted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CANSparkMaxLowLevel.MotorType getType() {
        return type;
    }

    public void setType(CANSparkMaxLowLevel.MotorType type) {
        this.type = type;
    }

    public Inverted getInverted() {
        return inverted;
    }

    public void setInverted(Inverted inverted) {
        this.inverted = inverted;
    }
}
