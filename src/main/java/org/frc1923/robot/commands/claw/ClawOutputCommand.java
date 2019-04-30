package org.frc1923.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.ClawSubsystem;

public class ClawOutputCommand extends Command {

    private double demand;

    public ClawOutputCommand(double demand) {
        this.requires(ClawSubsystem.getInstance());

        this.demand = demand;
    }

    @Override
    protected void execute() {
        ClawSubsystem.getInstance().set(this.demand, this.demand);
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
