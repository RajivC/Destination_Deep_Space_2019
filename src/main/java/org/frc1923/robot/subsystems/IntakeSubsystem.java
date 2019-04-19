package org.frc1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.intake.IntakeControllerCommand;

public class IntakeSubsystem extends Subsystem {

    private static IntakeSubsystem instance;

    private TalonSRX talon;

    private DoubleSolenoid solenoid;

    private boolean extended;

    private IntakeSubsystem() {
        this.talon = RobotMap.Intake.TALON.createTalon();
        this.solenoid = new DoubleSolenoid(RobotMap.Intake.FWD_PORT, RobotMap.Intake.REV_PORT);
        this.extended = false;

        new Notifier(() -> {
            this.solenoid.set(this.extended ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
        }).startPeriodic(0.1);
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public void set(double power) {
        this.talon.set(ControlMode.PercentOutput, power);
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
