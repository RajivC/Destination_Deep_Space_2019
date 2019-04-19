package org.frc1923.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.InstantCommand;

import org.frc1923.robot.subsystems.ElevatorSubsystem;
import org.frc1923.robot.utilities.logger.Logger;

public class ElevatorBrakeToggleCommand extends InstantCommand {

    @Override
    protected void initialize() {
        Logger.logEvent(
                this, "Toggling brakes",
                new Logger.DataPair("currentState", ElevatorSubsystem.getInstance().isBrakeEngaged())
        );
        ElevatorSubsystem.getInstance().setBrakeEngaged(!ElevatorSubsystem.getInstance().isBrakeEngaged());
    }

}
