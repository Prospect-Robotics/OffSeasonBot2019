package com.team2813.lib.talon.options;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Adrian Guerra
 */
@Retention(RUNTIME)
@Target(FIELD)
@Documented
@Repeatable(PIDProfiles.class)
public @interface PIDProfile {
	Profile profile();
	
	double p();
	
	double i();
	
	double d();
	
	double f();
	
	double maxIntegralAccumulator(); //TODO default value
	
	int integralZone();
	
	int allowableClosedLoopError();
	
	
	
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