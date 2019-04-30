package org.frc1923.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.DrivetrainSubsystem;

public class DriveOutputCommand extends Command {

    private double demand;

    public DriveOutputCommand(double demand) {
        this.demand = demand;
    }

    @Override
    protected void execute() {
        DrivetrainSubsystem.getInstance().set(this.demand, this.demand);
    }

    @Override
    protected void end() {
        DrivetrainSubsystem.getInstance().set(0, 0);
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
