package com.team2813.lib.sparkMax.options;

import java.lang.annotation.Documented;
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
public @interface PIDProfiles {
	PIDProfile[] value();
}
