package com.team2813.lib.sparkMax.options;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Grady Whelan
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

	double integralZone();

	double maxOutput();

	double minOutput();

	// TODO figure out how many profiles a SPARK max can have (it's at least 2)
	// TODO document
	enum Profile {
		PRIMARY(0),
		SECONDARY(1);

		public final int id;

		private Profile(int id) {
			this.id = id;
		}
	}
}