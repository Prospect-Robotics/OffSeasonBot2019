package com.team2813.frc2019.actions;

public class WaitAction implements Action {

	private double duration;
	private double startTime;

	/**
	 * Create a new action to wait an amount of time in seconds
	 * @param durationInSeconds
	 */
	public WaitAction(double durationInSeconds) {
		this.duration = durationInSeconds;
	}

	@Override
	public boolean update(double timestamp) {
		return timestamp - startTime >= duration;
	}

	@Override
	public void start(double timestamp) {
		startTime = timestamp;
	}

	@Override
	public void end(double timestamp) {

	}

	@Override
	public boolean getRemoveOnDisabled() {
		return false;
	}
}