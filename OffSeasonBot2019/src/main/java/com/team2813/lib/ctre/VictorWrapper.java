package com.team2813.lib.ctre;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
/*
 * getX() - general getter
 * setX() - general setter
 * booleans should also have enableX() and disableX()
 */


/**
 * @author Adrian Guerra
 */
public class VictorWrapper extends BaseMotorControllerWrapper<VictorSPX> {
	public VictorWrapper(int deviceNumber, String subsystemName) {
		motorController = new VictorSPX(deviceNumber);
		this.subsystemName = subsystemName;
	}
	
	public VictorWrapper(int deviceNumber) {
		this(deviceNumber, "");
	}
}
