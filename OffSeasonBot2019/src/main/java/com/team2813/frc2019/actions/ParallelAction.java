package com.team2813.frc2019.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.team2813.frc2019.Robot.RobotMode;
import static com.team2813.frc2019.subsystems.Subsystems.LOOPER;

/**
 * @author Adrian Guerra
 * @author Grady Whelan
 */
public class ParallelAction implements Action {

	private List<Action> actions;

	/**
	 * Creates a new action that runs a list of actions to be run simultaneously
	 * @param actions
	 */
	public ParallelAction(List<? extends Action> actions) {
		this.actions = new ArrayList<>(actions);
	}

	/**
	 * Creates a new action that runs a list of actions to be run simultaneously
	 * @param actions
	 */
	public ParallelAction(Action...actions) {
		this(Arrays.asList(actions));
	}

	@Override
	public void start(double timestamp) {
		for(Action action : actions) action.start(timestamp);
	}

	@Override
	public boolean update(double timestamp) {

		actions.removeIf(action -> {
			if(action.update(timestamp) || (LOOPER.mode == RobotMode.DISABLED && action.getRemoveOnDisabled())){
				action.end(timestamp);
				return true;
			}
			else return false;
		});

		// actions.removeIf(action -> action.update(timestamp));
		return actions.size() == 0; // done if no actions left
	}

	@Override
	public void end(double timestamp) {
		for(Action action : actions){
			action.end(timestamp);
		}
	}

	@Override
	public boolean getRemoveOnDisabled() {
		return false;
	}
}
