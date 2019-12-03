package com.team2813.frc2019.subsystems;

import com.ctre.phoenix.CANifier;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PWM;

public class Lightshow {
    private DigitalOutput out;

    private double output = 0;

    Thread thread;

    public Lightshow(int channel) {
        this.out = new DigitalOutput(channel);
    }

    public void enable() {
        out.enablePWM(0);
        out.setPWMRate(4200);
        thread = new Thread(() -> {
            try {
                while (true) {
                    out.updateDutyCycle(0);
                    System.out.println();
                    Thread.sleep(1000);
                    out.updateDutyCycle(.2);
                    System.out.println(.2);
                    Thread.sleep(1000);
                    out.updateDutyCycle(.4);
                    System.out.println(.4);
                    Thread.sleep(1000);
                    out.updateDutyCycle(.6);
                    System.out.println(out.get());
                    Thread.sleep(1000);
                    out.updateDutyCycle(.8);
                    System.out.println(.8);
                    Thread.sleep(1000);
                    out.updateDutyCycle(1);
                    System.out.println(1);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    public Lightshow disable() {
        thread.stop();
        return this;
    }

//    public CANifier setR(int red) {
//        caNifier.x
//    }
}


// 14