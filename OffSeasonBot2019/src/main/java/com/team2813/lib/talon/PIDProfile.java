package com.team2813.lib.talon;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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