package org.frc1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.intake.IntakeControllerCommand;
import org.frc1923.robot.utilities.notifier.NamedNotifier;

public class IntakeSubsystem extends Subsystem {

    private static IntakeSubsystem instance;

    private TalonSRX talon;

    private DoubleSolenoid solenoid;

    private boolean extended;
    private double demand;

    private IntakeSubsystem() {
        this.talon = RobotMap.Intake.TALON.createTalon();
        this.solenoid = new DoubleSolenoid(RobotMap.Intake.FWD_PORT, RobotMap.Intake.REV_PORT);
        this.extended = false;

        new NamedNotifier(() -> {
            this.solenoid.set(this.extended ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
            this.talon.set(ControlMode.PercentOutput, this.demand);
        }, "IntakeSubsystem.Set0", 0.1).start();
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public void set(double demand) {
        this.demand = demand;
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new IntakeControllerCommand());
    }

    public static synchronized void initialize() {
        if (IntakeSubsystem.instance == null) {
            IntakeSubsystem.instance = new IntakeSubsystem();
        }
    }

    public static IntakeSubsystem getInstance() {
        return IntakeSubsystem.instance;
    }

}
