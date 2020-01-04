package com.team2813.lib.solenoid;

import edu.wpi.first.wpilibj.Solenoid;

import java.util.ArrayList;

/**
 * For pistons keep track of solenoid state as extended/retracted
 * rather than true/false
 *
 * TODO we should change this to wrap Solenoid
 *
 * @author Grady Whelan
 */
public class PistonSolenoid {

	private ArrayList<Solenoid> solenoids = new ArrayList<>();

	/**
	 * Allows for multiple solenoids to be tied to the same piston in the code
	 * @param ids on PCM 0
	 */
	public PistonSolenoid(int... ids) {
		for (int id : ids) {
			solenoids.add(new Solenoid(0, id));
		}
	}

	public void set(PistonState state) {
		for (Solenoid solenoid : solenoids) {
			solenoid.set(state.value);
		}
	}

	public void retract(){
		set(PistonState.RETRACTED);
	}

	public void extend(){
		set(PistonState.EXTENDED);
	}

	public PistonState get() {
		// TODO fix could cause problems
		return PistonState.from(solenoids.get(0).get());
	}

	public enum PistonState {
		RETRACTED(false), EXTENDED(true);

		public final boolean value;

		PistonState(boolean value) {
			this.value = value;
		}

		private static PistonState from(boolean b) {
			return b ? EXTENDED : RETRACTED;
		}
	}
}