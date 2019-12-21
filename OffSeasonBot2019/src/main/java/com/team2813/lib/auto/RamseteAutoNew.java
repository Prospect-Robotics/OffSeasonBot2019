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

public class RamseteAutoNew {
    private DifferentialDriveKinematics kinematics;
    private TrajectoryConfig config;
    private Trajectory trajectory;
    private RamseteController controller;
    private RamseteAutoNew next;
    private NextConfig nextConfig;

    private double timeStart = -1;

    public RamseteAutoNew(DifferentialDriveKinematics kinematics, Pose2d startVector, List<Translation2d> translations, Pose2d endVector, boolean reversed) {
        this.kinematics = kinematics;
        config = new TrajectoryConfig(2, 2)
                .setReversed(reversed);
        trajectory = TrajectoryGenerator.generateTrajectory(startVector, translations, endVector, config);
        controller = new RamseteController();
    }

    public RamseteAutoNew(DifferentialDriveKinematics kinematics, Pose2d startVector, List<Translation2d> translations, Pose2d endVector) {
        this(kinematics, startVector, translations, endVector, false);
    }

    public void next(List<Translation2d> translations, Pose2d endVector, boolean reversed) {
        if (nextConfig == null)
            nextConfig = new NextConfig(translations, endVector, reversed);
        else
            nextConfig.setNext(new NextConfig(translations, endVector, reversed));
    }

    public void next(List<Translation2d> translations, Pose2d endVector) {
        next(translations, endVector, false);
    }

    public DriveDemand getDemand(Pose2d currentRobotPose) {
        if (timeStart == -1) {
            timeStart = System.currentTimeMillis();
        }


        if ((System.currentTimeMillis() - timeStart) / 1000 >= trajectory.getTotalTimeSeconds() && nextConfig != null) {
            if (next == null) {
                System.out.println(currentRobotPose);
                next = new RamseteAutoNew(kinematics, currentRobotPose, nextConfig.translations, nextConfig.endVector, nextConfig.reversed);
            }
            return next.getDemand(currentRobotPose);
        }
        Trajectory.State goal = trajectory.sample((System.currentTimeMillis() - timeStart) / 1000); // sample the trajectory
        ChassisSpeeds adjustedSpeeds = controller.calculate(currentRobotPose, goal);
        return new DriveDemand(kinematics.toWheelSpeeds(adjustedSpeeds));
    }

    public void reset() {
        timeStart = -1;
        if (next != null)
            next.reset();
    }

    private class NextConfig {
        List<Translation2d> translations;
        Pose2d endVector;
        boolean reversed;
        NextConfig next;

        NextConfig(List<Translation2d> translations, Pose2d endVector, boolean reversed) {
            this.translations = translations;
            this.endVector = endVector;
            this.reversed = reversed;
        }

        void setNext(NextConfig next) {
            if (this.next == null)
                this.next = next;
            else
                this.next.setNext(next);
        }
    }
}
