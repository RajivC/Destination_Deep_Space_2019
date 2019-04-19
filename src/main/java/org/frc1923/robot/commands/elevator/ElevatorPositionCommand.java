package org.frc1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.ElevatorSubsystem;
import org.frc1923.robot.utilities.logger.Logger;

public class ElevatorPositionCommand extends Command {

    private int position;

    private int velocity;
    private int acceleration;

    private boolean limitEnable;
    private boolean dynamicRates;

    public ElevatorPositionCommand(int position) {
        this(position, 5 * 4096, 5 * 4096, true);

        this.dynamicRates = true;
    }

    public ElevatorPositionCommand(int position, int velocity, int acceleration, boolean limitEnable) {
        this.requires(ElevatorSubsystem.getInstance());

        this.velocity = velocity;
        this.acceleration = acceleration;
        this.position = position;
        this.limitEnable = limitEnable;
        this.dynamicRates = false;

        this.setTimeout(2);
    }

    @Override
    protected void initialize() {
        int currentPosition = ElevatorSubsystem.getInstance().getPosition();

        int velocity = this.velocity;
        int acceleration = this.acceleration;

        if (this.dynamicRates && this.position > currentPosition) {
            velocity = 6 * 4096;
            acceleration = 6 * 4096;
        }

        ElevatorSubsystem.getInstance().configureMotionSettings(velocity, acceleration);
        ElevatorSubsystem.getInstance().configureLimitSwitchEnable(this.limitEnable);

        Logger.logEvent(
                this, "Initializing",
                new Logger.DataPair("currentPosition", currentPosition),
                new Logger.DataPair("target", this.position),
                new Logger.DataPair("velocity", velocity),
                new Logger.DataPair("acceleration", acceleration),
                new Logger.DataPair("limitEnable", this.limitEnable)
        );
    }

    @Override
    protected void execute() {
        ElevatorSubsystem.getInstance().set(ControlMode.MotionMagic, this.position);
    }

    public double getError() {
        return this.position - ElevatorSubsystem.getInstance().getPosition();
    }

    @Override
    protected boolean isFinished() {
        return this.isTimedOut() || Math.abs(this.getError()) < 5000;
    }

    @Override
    protected void interrupted() {
        Logger.logEvent(this, "Interrupted");

        this.end();
    }

    @Override
    protected void end() {
        ElevatorSubsystem.getInstance().setHoldPosition(this.position);
        ElevatorSubsystem.getInstance().configureLimitSwitchEnable(true);

        Logger.logEvent(
                this, "Ending, setting new hold position to target",
                new Logger.DataPair("target", this.position),
                new Logger.DataPair("targetError", this.getError())
        );
    }

}