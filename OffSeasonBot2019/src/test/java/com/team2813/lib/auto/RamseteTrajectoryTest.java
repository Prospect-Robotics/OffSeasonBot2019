package com.team2813.lib.auto;

import edu.wpi.first.wpilibj2.controller.RamseteController;
import edu.wpi.first.wpilibj2.geometry.Pose2d;
import edu.wpi.first.wpilibj2.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.geometry.Translation2d;
import edu.wpi.first.wpilibj2.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj2.trajectory.TrajectoryGenerator;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class RamseteTrajectoryTest {
    @Test
    public void testPathweaverToWPI() throws IOException {
        RamseteTrajectory trajectory = new RamseteTrajectory(List.of(new GeneratedTrajectory("Middle Blue to Top Left Rocket Outer", false)));

        for (Trajectory.State state : trajectory.getTrajectories().get(0).getStates()) {
            System.out.println(state.toString());
        }
    }
}
