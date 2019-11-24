package com.team2813.lib.controls;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The Button object is a Joystick button
 * that has methods to trigger functions while
 * held or when pressed.
 *
 * @author Grady Whelan
 */
public class Button {
	private Joystick joystick;
	private int buttonNumber;

	/**
	 * Create a joystick button for triggering functions.
	 *
	 * @param joystick     The Joystick object that has the button
	 * @param buttonNumber The button number
	 */
	public Button(Joystick joystick, int buttonNumber) {
		this.joystick = joystick;
		this.buttonNumber = buttonNumber;
	}

	public void whileHeld(Runnable function) {
		if (get()) function.run();
	}

	public void whenPressed(Runnable function) {
		if (getPressed()) function.run();
	}

	public void whenReleased(Runnable function) {
		if (getReleased()) function.run();
	}

	public boolean get()  {
		return joystick.getRawButton(buttonNumber);
	}

	public boolean getPressed() {
		return joystick.getRawButtonPressed(buttonNumber);
	}

	public boolean getReleased() {
		return joystick.getRawButtonReleased(buttonNumber);
	}
}
