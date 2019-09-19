package com.team2813.lib.talon.options;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables dealing with 0, 1 or 2 limit switches
 * (no prevention of more than that right now, or 
 * 	prevention of two limits in the same direction)
 * @author Grady Whelan
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface HardLimitSwitches {
	HardLimitSwitch[] value();
}