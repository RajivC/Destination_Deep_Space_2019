package org.frc1923.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.OI;
import org.frc1923.robot.Robot;
import org.frc1923.robot.subsystems.DrivetrainSubsystem;

public class DriveControllerCommand extends Command {

    public DriveControllerCommand() {
        this.requires(DrivetrainSubsystem.getInstance());
    }

    @Override
    protected void execute() {
        double power = Math.pow(OI.getInstance().getDriver().getLeftY(), 2);
        double turn = Math.pow(OI.getInstance().getDriver().getRightX(), 2);

        power *= Math.signum(OI.getInstance().getDriver().getLeftY());
        turn *= Math.signum(OI.getInstance().getDriver().getRightX());

        if (!Robot.PRACTICE_ROBOT) {
            power *= -1;
        }

        DrivetrainSubsystem.getInstance().set(power + turn, power - turn);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
