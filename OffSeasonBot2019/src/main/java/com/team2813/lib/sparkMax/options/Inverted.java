package com.team2813.lib.sparkMax.options;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Adrian Guerra
 * Sets the invert type of a motor controller.
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Inverted {
	InvertType type() default InvertType.INVERTED;
}
