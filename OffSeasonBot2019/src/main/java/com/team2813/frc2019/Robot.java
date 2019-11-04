/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2813.frc2019;

import com.team2813.frc2019.loops.Loop;
import com.team2813.frc2019.subsystems.Drive;
import com.team2813.frc2019.subsystems.MainIntake;
import com.team2813.frc2019.subsystems.Subsystem;
import com.team2813.lib.util.CrashTracker;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2019.subsystems.Subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	{
		for (Loop subsystem : allSubsystems) {
			LOOPER.addLoop(subsystem);
		}
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		try {
			CrashTracker.logRobotInit();
		} catch (Throwable t) {
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}

	/**
	 * This function is called every robot packet, no matter the mode. Use
	 * this for items like diagnostics that you want ran during disabled,
	 * autonomous, teleoperated and test.
	 *
	 * <p>This runs after the mode specific periodic functions, but before
	 * LiveWindow and SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic() {
	}

	@Override
	public void disabledInit() {
		try {
			CrashTracker.logDisabledInit();
			LOOPER.setMode(RobotMode.DISABLED);
			LOOPER.start();
		} catch (Throwable t) {
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		try {
			CrashTracker.logAutoInit();
			Compressor compressor = new Compressor(); // FIXME: 11/02/2019 this shouldn't need to be here
			compressor.start();
			MAIN_INTAKE.setMode(MainIntake.GamePiece.HATCH_PANEL);
			for (Subsystem subsystem : allSubsystems) {
				subsystem.zeroSensors();
			}
			teleopInit();
		} catch (Throwable t) {
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}

	@Override
	public void teleopInit() {
		try {
			CrashTracker.logTeleopInit();
			LOOPER.setMode(RobotMode.ENABLED);
			LOOPER.start();
		} catch (Throwable t) {
			CrashTracker.logThrowableCrash(t);
			try {
				throw t;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		teleopPeriodic();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		/*
		 * this calls every subsystem's controls method which
		 * should contain any code to invoke driver controls
		 */
		for (Subsystem subsystem : allSubsystems) {
			subsystem.teleopControls();
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public enum RobotMode {
		DISABLED, ENABLED
	}
}
