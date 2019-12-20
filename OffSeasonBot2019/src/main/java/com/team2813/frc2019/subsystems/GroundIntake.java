package com.team2813.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.talon.CTREException;
import com.team2813.lib.talon.VictorWrapper;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GroundIntake extends Subsystem {

    Position targetPosition = Position.RETRACTED;
    private double encoderPosition;
    private final double ALLOWABLE_ERROR = 0.3;
    private final double SLOW_RANGE = 0.5;
    private final double HIGH_PERCENT = 0.3;
    private final double LOW_PERCENT = 0.1;
    private final CANSparkMaxWrapper MOTOR = MotorConfigs.sparks.get("groundIntakeArm");

//    private static final Button ROLLER_OUT = SubsystemControlsConfig.groundIntakeRollerOut;
//    private static final Button ROLLER_IN = SubsystemControlsConfig.groundIntakeRollerIn;
    private static final Button TOGGLE_POSITION = SubsystemControlsConfig.groundIntakeTogglePosition;

    private static final VictorWrapper ROLLER = MotorConfigs.victors.get("groundIntakeRoller");
	private static final double ROLLER_SPEED = .8;

	GroundIntake() {
        try {
            MOTOR.enableForwardSoftLimit(true);
            MOTOR.setForwardSoftLimit(Position.LIMIT_OUT.getPos());
            MOTOR.enableReverseSoftLimit(true);
            MOTOR.setReverseSoftLimit(Position.LIMIT_IN.getPos());
        } catch (SparkMaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean checkSystem_() throws CTREException, SparkMaxException {
        return false;
    }

    @Override
    protected void zeroSensors_() throws SparkMaxException {
	    MOTOR.setEncoderPosition(0.0);
    }

    @Override
    protected void outputTelemetry_() {
        SmartDashboard.putNumber("GroundIntake Target", targetPosition.getPos());
        SmartDashboard.putNumber("GroundIntake Position", encoderPosition);
    }

    @Override
    protected void writePeriodicOutputs_() throws SparkMaxException, CTREException {
	    ROLLER.set(ControlMode.PercentOutput, -1.0);// TODO: 12/13/2019 remove for testing prototype shooter only
	    if (targetPosition == Position.RETRACTED && MainIntake.currentPosition == MainIntake.Position.PICKUP_CARGO) {
	        targetPosition = Position.EXTENDED;
        }
	    if (Math.abs(targetPosition.getPos() - encoderPosition) > ALLOWABLE_ERROR) {

	        double percent = Math.abs(targetPosition.getPos() - encoderPosition) > SLOW_RANGE ? HIGH_PERCENT : LOW_PERCENT;
            boolean forward = targetPosition.getPos() > encoderPosition;
            MOTOR.set(forward ? percent : -percent, ControlType.kDutyCycle);
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
    protected void teleopControls_() {
	    if (Subsystems.MAIN_INTAKE.mode == MainIntake.GamePiece.HATCH_PANEL) return;
        TOGGLE_POSITION.whenPressed(this::togglePosition);
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

    void setRollerOut(boolean out) throws CTREException {
	    if (targetPosition != Position.EXTENDED) return;
        double percent = out ? ROLLER_SPEED : -ROLLER_SPEED;
        ROLLER.set(ControlMode.PercentOutput, percent);
    }

    void stopRoller() throws CTREException {
	    ROLLER.set(ControlMode.PercentOutput, 0.0);
    }

    enum Position implements Subsystem1d.Position<GroundIntake.Position> {
        LIMIT_IN(-0.1),
	    RETRACTED(0.0) {
            @Override
            public Position getNextClockwise() {
                return EXTENDED;
            }

            @Override
            public Position getNextCounter() {
                return EXTENDED;
            }
        }, EXTENDED(2.2) { // TODO: 11/01/2019 find correct value

            @Override
            public Position getNextClockwise() {
                return RETRACTED;
            }

            @Override
            public Position getNextCounter() {
                return RETRACTED;
            }
        }, LIMIT_OUT (2.4); // TODO: 11/05/2019 find correct value

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
