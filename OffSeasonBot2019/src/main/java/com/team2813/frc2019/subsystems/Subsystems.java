package com.team2813.frc2019.subsystems;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.team2813.frc2019.Robot.RobotMode;
import com.team2813.frc2019.loops.Looper;
import com.team2813.frc2019.loops.Loop;

public class Subsystems {

	public static List<Subsystem> allSubsystems;

	public static Drive DRIVE;
	public static GroundIntake GROUND_INTAKE;
	public static MainIntake MAIN_INTAKE;
	public static final Looper LOOPER = new Looper(RobotMode.DISABLED); //FIXME put looper somewhere else

	private static class SmartDashboardLoop implements Loop{
		int currentSubsystem = 0;

		@Override
		public void onAnyLoop(double timestamp){
			if(allSubsystems.size() == 0) return;
			if(currentSubsystem >= allSubsystems.size()) currentSubsystem = 0;
			allSubsystems.get(currentSubsystem).outputTelemetry();
			currentSubsystem++;
		}
	};

	public static void initializeSubsystems() {
		DRIVE = new Drive();
		GROUND_INTAKE = new GroundIntake();
		MAIN_INTAKE = new MainIntake();
		allSubsystems = Collections.unmodifiableList(Arrays.asList(
				DRIVE, GROUND_INTAKE, MAIN_INTAKE
		));
		LOOPER.addLoop(new SmartDashboardLoop());
	}
}