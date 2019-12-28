package com.team2813.lib.ctre;

/**
 * @author Adrian Guerra
 */
public class PIDProfile {
	
	// TODO document
	public enum Profile {
		PRIMARY(0),
		SECONDARY(1);

		public final int id;

		private Profile(int id) {
			this.id = id;
		}
	}
}