package com.team2813.lib.auto;

import com.team2813.lib.drive.DriveDemand;
import edu.wpi.first.wpilibj2.controller.RamseteController;
import edu.wpi.first.wpilibj2.geometry.Pose2d;
import edu.wpi.first.wpilibj2.geometry.Translation2d;
import edu.wpi.first.wpilibj2.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj2.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj2.trajectory.TrajectoryGenerator;

import java.util.List;

public class RamseteAuto {
    private DifferentialDriveKinematics kinematics;
    private TrajectoryConfig config;
    private Trajectory trajectory;
    private RamseteController controller;

    private double timeStart = -1;

    public RamseteAuto(DifferentialDriveKinematics kinematics, Pose2d startVector, List<Translation2d> translations, Pose2d endVector) {
        this.kinematics = kinematics;
        config = new TrajectoryConfig(3, 4);
        trajectory = TrajectoryGenerator.generateTrajectory(startVector, translations, endVector, config);
        controller = new RamseteController();
    }

    public DriveDemand getDemand(Pose2d currentRobotPose) {
        if (timeStart == -1) {
            timeStart = System.currentTimeMillis();
        }
        Trajectory.State goal = trajectory.sample((System.currentTimeMillis() - timeStart) / 1000); // sample the trajectory
        ChassisSpeeds adjustedSpeeds = controller.calculate(currentRobotPose, goal);
        return new DriveDemand(kinematics.toWheelSpeeds(adjustedSpeeds));
    }

    public void reset() {
        timeStart = -1;
    }
}
