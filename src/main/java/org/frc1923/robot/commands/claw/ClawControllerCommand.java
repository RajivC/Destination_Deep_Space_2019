package org.frc1923.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.OI;
import org.frc1923.robot.subsystems.ClawSubsystem;
import org.frc1923.robot.subsystems.ElevatorSubsystem;
import org.frc1923.robot.subsystems.WristSubsystem;

public class ClawControllerCommand extends Command {

    public ClawControllerCommand() {
        this.requires(ClawSubsystem.getInstance());
    }

    @Override
    protected void execute() {
        double output = OI.getInstance().getOperator().getLeftY();

        if (Math.abs(output) < 0.15) {
            output = 0;
        }

        if (output == 0
                && ClawSubsystem.getInstance().isOpen()
                && WristSubsystem.getInstance().getPosition() < 100
                && ElevatorSubsystem.getInstance().getPosition() < 5000) {
            output = OI.getInstance().getOperator().getRightY();

            if (Math.abs(output) < 0.15) {
                output = 0;
            }
        }

        ClawSubsystem.getInstance().set(output, output);
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
