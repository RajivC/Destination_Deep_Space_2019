package org.frc1923.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.WristSubsystem;
import org.frc1923.robot.utilities.logger.Logger;

public class WristHoldCommand extends Command {

    private int holdPosition;

    public WristHoldCommand() {
        this.requires(WristSubsystem.getInstance());
        this.setInterruptible(true);
    }

    @Override
    protected void initialize() {
        this.holdPosition = WristSubsystem.getInstance().getHoldPosition();
        Logger.logEvent(this, "Initializing", new Logger.DataPair("holdPosition", this.holdPosition));
    }

    @Override
    protected void execute() {
        int holdPosition = WristSubsystem.getInstance().getHoldPosition();

        if (holdPosition != this.holdPosition) {
            Logger.logEvent(
                    this, "Updating holdPosition",
                    new Logger.DataPair("oldHoldPosition", this.holdPosition),
                    new Logger.DataPair("newHoldPosiiton", holdPosition)
            );
            this.holdPosition = holdPosition;
        }

        WristSubsystem.getInstance().set(ControlMode.MotionMagic, this.holdPosition);
    }

    @Override
    protected void end() {
        Logger.logEvent(this, "Ending", new Logger.DataPair("holdPosition", this.holdPosition));
    }

    @Override
    protected void interrupted() {
        Logger.logEvent(this, "Interrupted", new Logger.DataPair("holdPosition", this.holdPosition));
        this.end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}