package org.frc1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.OI;
import org.frc1923.robot.subsystems.ElevatorSubsystem;
import org.frc1923.robot.utilities.logger.Logger;

public class ElevatorControllerCommand extends Command  {

    public ElevatorControllerCommand() {
        this.requires(ElevatorSubsystem.getInstance());
    }

    @Override
    protected void execute() {
        double demand = OI.getInstance().getOperator().getLeftTrigger() > 0 ? -OI.getInstance().getOperator().getLeftTrigger() : OI.getInstance().getOperator().getRightTrigger();

        if (demand == 0) {
            if (ElevatorSubsystem.getInstance().getHoldPosition() == -1923) {
                ElevatorSubsystem.getInstance().resetHoldPosition();
                Logger.logEvent(this, "Resetting hold position", new Logger.DataPair("holdPosition", ElevatorSubsystem.getInstance().getHoldPosition()));
            }

            ElevatorSubsystem.getInstance().set(ControlMode.MotionMagic, ElevatorSubsystem.getInstance().getHoldPosition());
        } else {
            ElevatorSubsystem.getInstance().setHoldPosition(-1923);
            ElevatorSubsystem.getInstance().set(demand);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void interrupted() {
        this.end();
    }

}
