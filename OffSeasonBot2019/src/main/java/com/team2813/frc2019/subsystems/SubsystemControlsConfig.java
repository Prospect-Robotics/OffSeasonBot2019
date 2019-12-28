package com.team2813.frc2019.subsystems;

import com.team2813.lib.controls.*;

/**
 * Stores all of the controllers and their buttons and axes.
 * Controllers are private as they should never be accessed,
 * to make it easier to move a Button or Axis to a different
 * controller.
 *
 * To add a button or axis: construct a private Button or Axis
 * using controller.button(id). Then add a package-private
 * getter.
 */
class SubsystemControlsConfig {

	private static Controller driveJoystick = new Controller(0);
	private static Button pivotButton = driveJoystick.button(1);
	private static Button autoButton = driveJoystick.button(2);
	private static Axis driveX = driveJoystick.axis(0);
	private static Axis driveY = driveJoystick.axis(3);
	private static Axis driveSteer = driveJoystick.axis(0);
	private static Axis driveForward = driveJoystick.axis(3);
	private static Axis driveReverse = driveJoystick.axis(2);
	private static Button mainIntakeWheelIn = driveJoystick.button(3);
	private static Button mainIntakeWheelOut = driveJoystick.button(4);

	private static Controller controlsJoystick = new Controller(1);
//	static Button groundIntakeTogglePosition = controlsJoystick.button(1); // TODO: 11/01/2019 replace with correct button id
//	static Button groundIntakeRollerIn = controlsJoystick.button(2); // TODO: 11/01/2019 replace with correct button id
//	static Button groundIntakeRollerOut = controlsJoystick.button(3); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeClock = controlsJoystick.button(4); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeCounter = controlsJoystick.button(5); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeWheelIn = controlsJoystick.button(6); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeWheelOut = controlsJoystick.button(7); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeToggleMode = controlsJoystick.button(8); // TODO: 11/02/2019 replace with correct button id
	private static Button groundIntakeTogglePosition = controlsJoystick.button(6); // TODO: 11/01/2019 replace with correct button id
//	static Button groundIntakeRollerIn = controlsJoystick.button(8); // TODO: 11/01/2019 replace with correct button id
//	static Button groundIntakeRollerOut = controlsJoystick.button(5); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeClock = controlsJoystick.button(8);// FIXME: 11/06/2019
//	static Button mainIntakeCounter = controlsJoystick.button(7);
	private static Button mainIntakeToggleMode = controlsJoystick.button(7);
	private static Button mainIntakeCargoRocketHold = controlsJoystick.button(5);
	private static Button mainIntakeHome = controlsJoystick.button(4);
	private static Button mainIntakePlacePieceForward = controlsJoystick.button(3);
	private static Button mainIntakePlacePieceReverse = controlsJoystick.button(1);
	private static Button mainIntakeCargoPickup = controlsJoystick.button(2);
	private static Axis mainIntakeArmFineControl = controlsJoystick.axis(3);

	static Button getPivotButton() {
		return pivotButton;
	}

	static Button getAutoButton() {
		return autoButton;
	}

	static Axis getDriveX() {
		return driveX;
	}

	static Axis getDriveY() {
		return driveY;
	}

	static Axis getDriveSteer() {
		return driveSteer;
	}

	static Axis getDriveForward() {
		return driveForward;
	}

	static Axis getDriveReverse() {
		return driveReverse;
	}

	static Button getMainIntakeWheelIn() {
		return mainIntakeWheelIn;
	}

	static Button getMainIntakeWheelOut() {
		return mainIntakeWheelOut;
	}

	static Button getGroundIntakeTogglePosition() {
		return groundIntakeTogglePosition;
	}

	static Button getMainIntakeToggleMode() {
		return mainIntakeToggleMode;
	}

	static Button getMainIntakeCargoRocketHold() {
		return mainIntakeCargoRocketHold;
	}

	static Button getMainIntakeHome() {
		return mainIntakeHome;
	}

	static Button getMainIntakePlacePieceForward() {
		return mainIntakePlacePieceForward;
	}

	static Button getMainIntakePlacePieceReverse() {
		return mainIntakePlacePieceReverse;
	}

	static Button getMainIntakeCargoPickup() {
		return mainIntakeCargoPickup;
	}

	static Axis getMainIntakeArmFineControl() {
		return mainIntakeArmFineControl;
	}
}
