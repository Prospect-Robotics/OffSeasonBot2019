package com.team2813.lib.controls;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The Axis object is a Joystick axis
 * that has method to get the value of
 * the axis
 *
 * @author Grady Whelan
 */
public class Axis {
	private Joystick joystick;
	private int axisNumber;

	/**
	 * Create a joystick axis for getting the value
	 * of the axis
	 *
	 * @param joystick   The Joystick object that has the axis
	 * @param axisNumber The qxis number
	 */
	public Axis(Joystick joystick, int axisNumber) {
		this.joystick = joystick;
		this.axisNumber = axisNumber;
	}

	public double get()  {
		return joystick.getRawAxis(axisNumber);
	}
}
