package com.team2813.frc2019.actions;

/**
 * @author Adrian Guerra
 * @author Grady Whelan
 */
public interface Action {

	/**
	 * Returns whether or not the code has finished execution.
	 *
	 * @return true if finished, false otherwise
	 */
	boolean update(double timestamp);

	/**
	 * Run code once when the action is started, for set up
	 */
	void start(double timestamp);

	/**
	 * Run after update returns true
	 */
	void end(double timestamp);

	default boolean getRemoveOnDisabled() {return false;}
}