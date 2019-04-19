package org.frc1923.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.DrivetrainSubsystem;

public class DriveTimeCommand extends Command {

    private double power;

    protected void initialize() {
        this.setTimeout(2000);
        this.power = 0.40;
    }

    protected void execute() {
        DrivetrainSubsystem.getInstance().set(this.power, this.power);
    }

    protected void end() {
        DrivetrainSubsystem.getInstance().set(0, 0);
    }

    protected void interrupted() {
        this.end();
    }

    @Override
    protected boolean isFinished() {
        return this.isTimedOut();
    }

}
