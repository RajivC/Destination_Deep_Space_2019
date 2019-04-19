package org.frc1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.climber.PumpCommand;

public class ClimberSubsystem extends Subsystem {

    private static ClimberSubsystem instance;

    private TalonSRX[] pumps;

    private ClimberSubsystem() {
        this.pumps = new TalonSRX[RobotMap.Climber.TALONS.length];

        for (int i = 0; i < this.pumps.length; i++) {
            this.pumps[i] = RobotMap.Climber.TALONS[i].createTalon();

            if (i > 0) {
                this.pumps[i].follow(this.pumps[0]);
            }
        }
    }

    public void set(double output) {
        this.pumps[0].set(ControlMode.PercentOutput, output);
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new PumpCommand());
    }

    public static synchronized void initialize() {
        if (ClimberSubsystem.instance == null) {
            ClimberSubsystem.instance = new ClimberSubsystem();
        }
    }

    public static ClimberSubsystem getInstance() {
        return ClimberSubsystem.instance;
    }

}
