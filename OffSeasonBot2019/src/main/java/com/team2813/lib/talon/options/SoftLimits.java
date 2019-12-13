package com.team2813.lib.talon.options;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Grady Whelan
 * Enables dealing with 0, 1 or 2 soft limits
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface SoftLimits {
	SoftLimit[] value();
}