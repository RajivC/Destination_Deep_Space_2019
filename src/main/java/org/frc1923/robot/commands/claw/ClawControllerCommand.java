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
        double demand = OI.getInstance().getOperator().getLeftY();

        if (Math.abs(demand) < 0.15) {
            demand = 0;
        }

        if (demand == 0
                && ClawSubsystem.getInstance().isOpen()
                && WristSubsystem.getInstance().getPosition() < 100
                && ElevatorSubsystem.getInstance().getPosition() < 5000) {
            demand = OI.getInstance().getOperator().getRightY();

            if (Math.abs(demand) < 0.15) {
                demand = 0;
            }
        }

        ClawSubsystem.getInstance().set(demand, demand);
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
