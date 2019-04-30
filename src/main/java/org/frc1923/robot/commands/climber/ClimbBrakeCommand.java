package org.frc1923.robot.commands.climber;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.ElevatorSubsystem;
import org.frc1923.robot.utilities.logger.Logger;
import org.frc1923.robot.utilities.MovingAverage;

public class ClimbBrakeCommand extends Command {

    private MovingAverage velocity;
    private double timestamp;

    private static Compressor compressor = new Compressor(0);

    public ClimbBrakeCommand() {
        this.velocity = new MovingAverage(10);
    }

    @Override
    protected void initialize() {
        this.velocity.clear();
        this.timestamp = Integer.MAX_VALUE;
        compressor.setClosedLoopControl(false);
    }

    @Override
    protected void execute() {
        this.velocity.add(ElevatorSubsystem.getInstance().getEncoderVelocity());

        double speed = Math.abs(this.velocity.get());

        if (speed >= 100) {
            this.timestamp = Integer.MAX_VALUE;
        }

        if (this.timestamp == Integer.MAX_VALUE && speed < 100 && ElevatorSubsystem.getInstance().getPosition() < 6144) {
            this.timestamp = Timer.getFPGATimestamp();
            Logger.logEvent(this, "Starting Timer", new Logger.DataPair("speed", speed));
        }

        if (Timer.getFPGATimestamp() - this.timestamp > 0.25) {
            ElevatorSubsystem.getInstance().setBrakeEngaged(true);
            compressor.setClosedLoopControl(true);
            Logger.logEvent(
                    this, "Engaging Brakes",
                    new Logger.DataPair("speed", speed),
                    new Logger.DataPair("position", ElevatorSubsystem.getInstance().getPosition())
            );
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        compressor.setClosedLoopControl(true);
    }

    @Override
    protected void interrupted() {
        this.end();
    }

}
