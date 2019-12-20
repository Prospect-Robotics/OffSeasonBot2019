/*----------------------------------------------------------------------------*/
/* Copyright (c) 2016-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj2.vision;

import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import edu.wpi.first.wpilibj.vision.VisionRunner;

/**
 * A vision thread is a special thread that runs a vision pipeline. It is a <i>daemon</i> thread;
 * it does not prevent the program from exiting when all other non-daemon threads
 * have finished running.
 *
 * @see VisionPipeline
 * @see edu.wpi.first.wpilibj.vision.VisionRunner
 * @see Thread#setDaemon(boolean)
 *
 * @deprecated Replaced with edu.wpi.first.vision.VisionThread
 */
@Deprecated
public class VisionThread extends Thread {
  /**
   * Creates a vision thread that continuously runs a {@link VisionPipeline}.
   *
   * @param visionRunner the runner for a vision pipeline
   */
  public VisionThread(edu.wpi.first.wpilibj.vision.VisionRunner<?> visionRunner) {
    super(visionRunner::runForever, "WPILib Vision Thread");
    setDaemon(true);
  }

  /**
   * Creates a new vision thread that continuously runs the given vision pipeline. This is
   * equivalent to {@code new VisionThread(new VisionRunner<>(videoSource, pipeline, listener))}.
   *
   * @param videoSource the source for images the pipeline should process
   * @param pipeline    the pipeline to run
   * @param listener    the listener to copy outputs from the pipeline after it runs
   * @param <P>         the type of the pipeline
   */
  public <P extends VisionPipeline> VisionThread(VideoSource videoSource,
                                                 P pipeline,
                                                 edu.wpi.first.wpilibj.vision.VisionRunner.Listener<? super P> listener) {
    this(new VisionRunner<>(videoSource, pipeline, listener));
  }

}
