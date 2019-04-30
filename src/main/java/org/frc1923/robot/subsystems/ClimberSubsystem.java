package org.frc1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.climber.PumpCommand;
import org.frc1923.robot.utilities.notifier.NamedNotifier;

public class ClimberSubsystem extends Subsystem {

    private static ClimberSubsystem instance;

    private TalonSRX[] pumps;

    private double demand;

    private ClimberSubsystem() {
        this.pumps = new TalonSRX[RobotMap.Climber.TALONS.length];

        for (int i = 0; i < this.pumps.length; i++) {
            this.pumps[i] = RobotMap.Climber.TALONS[i].createTalon();

            if (i > 0) {
                this.pumps[i].follow(this.pumps[0]);
            }
        }

        new NamedNotifier(() -> {
            this.pumps[0].set(ControlMode.PercentOutput, this.demand);
        }, "ClimberSubsystem.Set0", 0.1).start();
    }

    public void set(double demand) {
        this.demand = demand;
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
