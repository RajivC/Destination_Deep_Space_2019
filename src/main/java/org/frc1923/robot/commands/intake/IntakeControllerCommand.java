package org.frc1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.OI;
import org.frc1923.robot.Robot;
import org.frc1923.robot.subsystems.IntakeSubsystem;

public class IntakeControllerCommand extends Command {

    public IntakeControllerCommand() {
        this.requires(IntakeSubsystem.getInstance());
    }

    @Override
    protected void execute() {
        double output = OI.getInstance().getOperator().getRightY();

        if (Math.abs(output) < 0.15) {
            output = 0;
        }

        if (!Robot.PRACTICE_ROBOT) {
            output *= -1;
        }

        IntakeSubsystem.getInstance().set(output);
    }

    @Override
    protected void end() {
        IntakeSubsystem.getInstance().set(0);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
