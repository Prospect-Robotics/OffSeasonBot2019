package com.team2813.frc2019.config.motor;

import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration;

public class VictorMotorConfig {
    int deviceNumber;
    String subsystemName;

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getSubsystemName() {
        return subsystemName;
    }

    public void setSubsystemName(String subsystemName) {
        this.subsystemName = subsystemName;
    }
}
