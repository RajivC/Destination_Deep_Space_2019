package org.frc1923.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.ClawSubsystem;

public class ClawOutputCommand extends Command {

    private double power;

    public ClawOutputCommand(double power) {
        this.requires(ClawSubsystem.getInstance());

        this.power = power;
    }

    @Override
    protected void execute() {
        ClawSubsystem.getInstance().set(this.power, this.power);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        ClawSubsystem.getInstance().set(0, 0);
    }

}
