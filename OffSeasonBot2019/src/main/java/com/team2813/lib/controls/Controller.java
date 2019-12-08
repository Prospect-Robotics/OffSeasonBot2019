package com.team2813.lib.controls;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A wrapper for WPILib Joystick to initialize
 * buttons and axes.
 *
 * @author Grady Whelan
 */
public class Controller extends Joystick {
	/**
	 * Construct an instance of a joystick. The joystick index is the USB port on the drivers
	 * station.
	 *
	 * @param port The port on the Driver Station that the joystick is plugged into.
	 */
	public Controller(int port) {
		super(port);
	}

	/**
	 * Add an axis to the controller
	 * @param axisNumber on controller
	 * @return the button
	 */
	public Axis axis(int axisNumber) {
		return new Axis(this, axisNumber);
	}

	/**
	 * Add a button to the controller
	 * @param buttonNumber on controller
	 * @return the button
	 */
	public Button button(int buttonNumber) {
		return new Button(this, buttonNumber);
	}
}
