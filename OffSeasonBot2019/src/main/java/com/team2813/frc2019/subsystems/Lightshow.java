package com.team2813.frc2019.subsystems;

import com.ctre.phoenix.CANifier;

public class Lightshow {
    private CANifier caNifier;
    private int pwmChannel = CANifier.PWMChannel.PWMChannel1.value;

    public Lightshow(CANifier caNifier) {
        this.caNifier = caNifier;
        caNifier.enablePWMOutput(pwmChannel, true);
    }

    public Lightshow test() {
        caNifier.setPWMOutput(pwmChannel, 1);
        return this;
    }

//    public CANifier setR(int red) {
//        caNifier.x
//    }
}


// 14