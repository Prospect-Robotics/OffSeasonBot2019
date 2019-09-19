package com.team2813.lib.sparkMax.options;

import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Sets up the Spark's hard limit switch config
 * @author Grady Whelan
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface HardLimitSwitch {

	LimitDirection direction();
	LimitSwitchPolarity limitSwitchNormal() default LimitSwitchPolarity.kNormallyOpen;
	boolean enabled();
	boolean clearOnLimit();

	double paramValue() default 0;
	int paramSubValue() default 0;
	int paramOrdinal() default 0;

}