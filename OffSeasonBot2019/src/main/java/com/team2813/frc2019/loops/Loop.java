package com.team2813.frc2019.loops;


/**
 * Interface for loops, which are routine that run periodically in the robot
 * code (such as periodic gyroscope calibration, etc.)
 */
public interface Loop {

	/**
	 * Code to run once when robot is enabled
	 *
	 * @param timestamp timestamp in seconds
	 */
	default void onEnabledStart(double timestamp) {}

	/**
	 * Code to run while robot is enabled
	 *
	 * @param timestamp timestamp in seconds
	 */
	default void onEnabledLoop(double timestamp) {}

	/**
	 * Code to run once when robot is disabled
	 *
	 * @param timestamp timestamp in seconds
	 */
	default void onEnabledStop(double timestamp) {}

	/**
	 * Code to run once when robot is disabled
	 *
	 * @param timestamp timestamp in seconds
	 */
	default void onDisabledStart(double timestamp) {}

	/**
	 * Code to run while robot is disabled
	 *
	 * @param timestamp timestamp in seconds
	 */
	default void onDisabledLoop(double timestamp) {}

	/**
	 * Code to run once when robot is enabled
	 *
	 * @param timestamp timestamp in seconds
	 */
	default void onDisabledStop(double timestamp) {}

	/**
	 * Code runs all the time
	 *
	 * @param timestamp timestamp in seconds
	 */
	default void onAnyLoop(double timestamp) {}
}
