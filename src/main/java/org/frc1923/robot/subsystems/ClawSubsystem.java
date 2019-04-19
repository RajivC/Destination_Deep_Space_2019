package org.frc1923.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.claw.ClawControllerCommand;

public class ClawSubsystem extends Subsystem {

    private static ClawSubsystem instance;

    private CANSparkMax left;
    private CANSparkMax right;

    private double leftOutput;
    private double rightOutput;

    private DoubleSolenoid solenoid;
    private boolean open;

    private ClawSubsystem() {
        this.left = RobotMap.Claw.LEFT_SPARK.createSpark();
        this.right = RobotMap.Claw.RIGHT_SPARK.createSpark();

        this.solenoid = new DoubleSolenoid(6, 7);
        this.open = true;

        new Notifier(() -> {
            this.solenoid.set(this.open ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);

            this.left.set(this.leftOutput);
            this.right.set(this.rightOutput);
        }).startPeriodic(0.1);
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void set(double left, double right) {
        this.leftOutput = left;
        this.rightOutput = right;
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new ClawControllerCommand());
    }

    public static synchronized void initialize() {
        if (ClawSubsystem.instance == null) {
            ClawSubsystem.instance = new ClawSubsystem();
        }
    }

    public static ClawSubsystem getInstance() {
        return ClawSubsystem.instance;
    }

}