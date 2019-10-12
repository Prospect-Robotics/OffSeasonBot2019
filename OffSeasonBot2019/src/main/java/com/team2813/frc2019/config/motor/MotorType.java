package com.team2813.frc2019.config.motor;

import com.revrobotics.CANSparkMaxLowLevel;

public enum MotorType {
    BRUSHLESS, BRUSHED;

    public CANSparkMaxLowLevel.MotorType getValue() {
        switch(this) {
            case BRUSHLESS:
                return CANSparkMaxLowLevel.MotorType.kBrushless;
            case BRUSHED:
                return CANSparkMaxLowLevel.MotorType.kBrushed;
        }
        return null;
    }
}
