package org.frc1923.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.claw.ClawControllerCommand;
import org.frc1923.robot.utilities.notifier.NamedNotifier;

public class ClawSubsystem extends Subsystem {

    private static ClawSubsystem instance;

    private CANSparkMax left;
    private CANSparkMax right;

    private double leftDemand;
    private double rightDemand;

    private DoubleSolenoid solenoid;
    private boolean open;

    private ClawSubsystem() {
        this.left = RobotMap.Claw.LEFT_SPARK.createSpark();
        this.right = RobotMap.Claw.RIGHT_SPARK.createSpark();

        this.solenoid = new DoubleSolenoid(RobotMap.Claw.FWD_PORT, RobotMap.Claw.REV_PORT);
        this.open = false;

        new NamedNotifier(() -> {
            this.solenoid.set(this.open ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);

            this.left.set(this.leftDemand);
            this.right.set(this.rightDemand);
        }, "ClawSubsystem.Set0", 0.1).start();
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void set(double leftDemand, double rightDemand) {
        this.leftDemand = leftDemand;
        this.rightDemand = rightDemand;
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