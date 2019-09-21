package com.team2813.lib.sparkMax.options;

import com.revrobotics.CANSparkMaxLowLevel.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Adrian Guerra
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(Followers.class)
public @interface Follower {

	int id();

	MotorType type() default MotorType.kBrushless;

	InvertType followMode() default InvertType.FOLLOW_LEADER;
	
	MotorControllerType motorControllerType() default MotorControllerType.SPARK_MAX;
	
	enum MotorControllerType {
		SPARK_MAX;
	}
}
