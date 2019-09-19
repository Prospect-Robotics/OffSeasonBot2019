package com.team2813.lib.talon.options;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Sets up the Talon's soft limit config
 * @author Grady Whelan
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface SoftLimit {

    LimitDirection direction();
    int threshold();
    boolean enabled();
    boolean clearOnLimit();

    double paramValue() default 0;
    int paramSubValue() default 0;
    int paramOrdinal() default 0;
}