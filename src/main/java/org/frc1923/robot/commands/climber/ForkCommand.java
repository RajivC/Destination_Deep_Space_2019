package org.frc1923.robot.commands.climber;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;

import org.frc1923.robot.subsystems.ElevatorSubsystem;

public class ForkCommand extends InstantCommand {

    @Override
    protected void initialize() {
        ElevatorSubsystem.getInstance().forks.set(
                ElevatorSubsystem.getInstance().forks.get() == DoubleSolenoid.Value.kForward ?
                        DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward
        );
    }

}
