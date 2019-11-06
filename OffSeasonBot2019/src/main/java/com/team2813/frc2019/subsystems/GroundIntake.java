package com.team2813.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.team2813.lib.controls.Button;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import com.team2813.lib.talon.VictorWrapper;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GroundIntake extends Subsystem {

    private Position targetPosition = Position.RETRACTED;
    private double encoderPosition;
    private final double ALLOWABLE_ERROR = 0.15;
    private final double PERCENT = 0.5;
    private final CANSparkMaxWrapper MOTOR = SubsystemMotorConfig.groundIntakeArm;

    private static final Button ROLLER_OUT = SubsystemControlsConfig.groundIntakeRollerOut;
    private static final Button ROLLER_IN = SubsystemControlsConfig.groundIntakeRollerIn;
    private static final Button TOGGLE_POSITION = SubsystemControlsConfig.groundIntakeTogglePosition;

    private static final VictorWrapper ROLLER = SubsystemMotorConfig.groundIntakeRoller;
	private static final double ROLLER_SPEED = 0.7;

	GroundIntake() {
	    
    }

    @Override
    protected boolean checkSystem_() throws CTREException, SparkMaxException {
        return false;
    }

    @Override
    protected void outputTelemetry_() {
        SmartDashboard.putNumber("MainIntake Target", targetPosition.getPos());
        SmartDashboard.putNumber("MainIntake Position", encoderPosition);
    }

    @Override
    protected void writePeriodicOutputs_() throws SparkMaxException {
        if (targetPosition.getPos() - encoderPosition > ALLOWABLE_ERROR) {
            MOTOR.set(PERCENT, ControlType.kDutyCycle);
        } else {
            MOTOR.setNeutralMode(CANSparkMax.IdleMode.kBrake);
            MOTOR.set(0.0, ControlType.kDutyCycle);
        }
    }

    @Override
    protected void readPeriodicInputs_() {
        encoderPosition = MOTOR.getEncoderPosition();
    }

    @Override
    protected void teleopControls_() throws CTREException {
        TOGGLE_POSITION.whenPressed(this::togglePosition);
        ROLLER_OUT.whenPressed(() -> {
            try {
                setRollerOut(true);
            } catch (CTREException e) {
                e.printStackTrace();
            }
        });
        ROLLER_IN.whenPressed(() -> {
            try {
                setRollerOut(false);
            } catch (CTREException e) {
                e.printStackTrace();
            }
        });
        if (!ROLLER_OUT.get() && !ROLLER_IN.get()) {
			ROLLER.set(ControlMode.PercentOutput, 0);
        }
    }

    @Override
    protected void onEnabledStart_(double timestamp) throws CTREException, SparkMaxException {

    }

    @Override
    protected void onEnabledLoop_(double timestamp) throws CTREException, SparkMaxException {

    }

    @Override
    protected void onEnabledStop_(double timestamp) throws CTREException, SparkMaxException {

    }

    private void togglePosition() {
        targetPosition = targetPosition.getNextClockwise();
    }

    private void setRollerOut(boolean out) throws CTREException {
        double percent = out ? ROLLER_SPEED : -ROLLER_SPEED;
        ROLLER.set(ControlMode.PercentOutput, percent);
    }

    enum Position implements Subsystem1d.Position<GroundIntake.Position> {
        RETRACTED(0) {
            @Override
            public Position getNextClockwise() {
                return EXTENDED;
            }

            @Override
            public Position getNextCounter() {
                return EXTENDED;
            }
        }, EXTENDED(2.5) { // TODO: 11/01/2019 find correct value

            @Override
            public Position getNextClockwise() {
                return RETRACTED;
            }

            @Override
            public Position getNextCounter() {
                return RETRACTED;
            }
        }, LIMIT (3.0); // TODO: 11/05/2019 find correct value

        private final double position;

        Position(double encoderPosition) {
            this.position = encoderPosition;
        }

        @Override
        public double getPos() {
            return position;
        }

        @Override
        public Position getNextClockwise() {
            return this;
        }

        @Override
        public Position getNextCounter() {
            return this;
        }

        @Override
        public Position getMin() {
            return RETRACTED;
        }

        @Override
        public Position getMax() {
            return EXTENDED;
        }
    }
}
