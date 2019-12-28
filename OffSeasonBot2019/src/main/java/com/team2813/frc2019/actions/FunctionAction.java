package com.team2813.frc2019.actions;

/**
 * Takes a Runnable and executes it
 * @author Adrian Guerra
 */
public class FunctionAction implements Action {

	private final Runnable function;
	private final boolean removeOnDisabled;

	/**
	 * Creates a new action from a function
	 * @param function
	 * @param removeOnDisabled
	 */
	public FunctionAction(Runnable function, boolean removeOnDisabled) {
		this.function = function;
		this.removeOnDisabled = removeOnDisabled;
	}

	@Override
	public void start(double timestamp) {
		function.run();
	}

	@Override
	public boolean update(double timestamp) {
		return true;
	}

	@Override
	public void end(double timestamp) {

	}

	@Override
	public boolean getRemoveOnDisabled() {
		return removeOnDisabled;
	}
}
