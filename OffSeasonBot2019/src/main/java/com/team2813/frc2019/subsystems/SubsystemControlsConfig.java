package com.team2813.frc2019.subsystems;

import com.team2813.lib.controls.*;

class SubsystemControlsConfig {

	private static Controller driveJoystick = new Controller(0);
	static Button pivotButton = driveJoystick.button(1);
	static Button autoButton = driveJoystick.button(2);
	static Axis driveX = driveJoystick.axis(0);
	static Axis driveY = driveJoystick.axis(3);
	static Axis driveSteer = driveJoystick.axis(0);
	static Axis driveForward = driveJoystick.axis(3);
	static Axis driveReverse = driveJoystick.axis(2);
	static Button mainIntakeWheelIn = driveJoystick.button(3);
	static Button mainIntakeWheelOut = driveJoystick.button(4);

	private static Controller controlsJoystick = new Controller(1);
//	static Button groundIntakeTogglePosition = controlsJoystick.button(1); // TODO: 11/01/2019 replace with correct button id
//	static Button groundIntakeRollerIn = controlsJoystick.button(2); // TODO: 11/01/2019 replace with correct button id
//	static Button groundIntakeRollerOut = controlsJoystick.button(3); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeClock = controlsJoystick.button(4); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeCounter = controlsJoystick.button(5); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeWheelIn = controlsJoystick.button(6); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeWheelOut = controlsJoystick.button(7); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeToggleMode = controlsJoystick.button(8); // TODO: 11/02/2019 replace with correct button id

	static Button groundIntakeTogglePosition = controlsJoystick.button(6); // TODO: 11/01/2019 replace with correct button id
//	static Button groundIntakeRollerIn = controlsJoystick.button(8); // TODO: 11/01/2019 replace with correct button id
//	static Button groundIntakeRollerOut = controlsJoystick.button(5); // TODO: 11/01/2019 replace with correct button id
//	static Button mainIntakeClock = controlsJoystick.button(8);// FIXME: 11/06/2019
//	static Button mainIntakeCounter = controlsJoystick.button(7);

	static Button mainIntakeToggleMode = controlsJoystick.button(7);


	static Button mainIntakeCargoRocketHold = controlsJoystick.button(5);
	static Button mainIntakeHome = controlsJoystick.button(4);
	static Button mainIntakePlacePieceForward = controlsJoystick.button(3);
	static Button mainIntakePlacePieceReverse = controlsJoystick.button(1);
	static Button mainIntakeCargoPickup = controlsJoystick.button(2);

	static Axis mainIntakeArmFineControl = controlsJoystick.axis(3);
}
