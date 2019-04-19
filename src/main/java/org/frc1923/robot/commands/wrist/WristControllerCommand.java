package org.frc1923.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.WristSubsystem;
import org.frc1923.robot.utilities.logger.Logger;

public class WristControllerCommand extends Command {

    private double power;

    public WristControllerCommand(double power) {
        this.power = power;
    }

    @Override
    protected void initialize() {
        Logger.logEvent(this, "Initializing", new Logger.DataPair("power", this.power));
    }

    @Override
    protected void execute() {
        WristSubsystem.getInstance().set(this.power);
    }

    @Override
    protected void end() {
        int holdPosition = WristSubsystem.getInstance().getHoldPosition();
        WristSubsystem.getInstance().resetHoldPosition();
        WristSubsystem.getInstance().set(ControlMode.MotionMagic, WristSubsystem.getInstance().getHoldPosition());

        Logger.logEvent(
                this,
                "Ending & setting new holdPosition",
                new Logger.DataPair("oldHoldPosition", holdPosition),
                new Logger.DataPair("newHoldPosition", WristSubsystem.getInstance().getHoldPosition())
        );
    }

    @Override
    protected void interrupted() {
        Logger.logEvent(this, "Interrupted");
        this.end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
