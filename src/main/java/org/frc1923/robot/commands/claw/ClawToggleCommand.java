package org.frc1923.robot.commands.claw;

import edu.wpi.first.wpilibj.command.InstantCommand;

import org.frc1923.robot.subsystems.ClawSubsystem;

public class ClawToggleCommand extends InstantCommand {

    @Override
    protected void initialize() {
        ClawSubsystem.getInstance().setOpen(!ClawSubsystem.getInstance().isOpen());
    }

}
