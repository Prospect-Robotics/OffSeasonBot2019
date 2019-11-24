package com.team2813.frc2019.actions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Adrian Guerra
 * @author Grady Whelan
 */
public class SeriesAction implements Action {

	private Queue<Action> actions;

	private Action currentAction;

	public SeriesAction(List<? extends Action> actions) {
		this.actions = new LinkedList<>(actions);
	}

	public SeriesAction(Action... actions) {
		this(Arrays.asList(actions));
	}

	@Override
	public void start(double timestamp) {
		currentAction = actions.poll();
		currentAction.start(timestamp);
	}

	@Override
	public boolean update(double timestamp) {
		if (currentAction == null) return true;
		if (currentAction.update(timestamp)) {
			currentAction.end(timestamp);
			currentAction = actions.poll();
			if (currentAction == null) return true;
			currentAction.start(timestamp);
		}
		return false;
	}

	@Override
	public void end(double timestamp) {
		if(currentAction != null) currentAction.end(timestamp);
	}

	@Override
	public boolean getRemoveOnDisabled() {
		if(currentAction == null) return false;
		else return currentAction.getRemoveOnDisabled();
	}
}
