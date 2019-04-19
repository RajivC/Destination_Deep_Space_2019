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

    private static Compressor compressor;

    public ClimbBrakeCommand() {
        this.velocity = new MovingAverage(10);

        if (compressor == null) {
            compressor = new Compressor(0);
        }
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

        if (this.timestamp == Integer.MAX_VALUE && speed < 100) {
            this.timestamp = Timer.getFPGATimestamp();
            Logger.logEvent(this, "Starting Timer", new Logger.DataPair("speed", speed));
        }

//        if (DriverStation.getInstance().getMatchTime() > 0 && this.timestamp != Integer.MAX_VALUE) {
//            double timeRemaining = (this.timestamp + 50) - Timer.getFPGATimestamp();
//
//            Logger.logEvent(this, "Execution Loop",
//                    new Logger.DataPair("timeRemaining", timeRemaining),
//                    new Logger.DataPair("timestamp", this.timestamp),
//                    new Logger.DataPair("FPGATimestamp", Timer.getFPGATimestamp()),
//                    new Logger.DataPair("speed", speed)
//            );
//
//            if (timeRemaining > DriverStation.getInstance().getMatchTime()
//                    && DriverStation.getInstance().getMatchTime() < 10
//                    && speed >= 100) {
//                ElevatorSubsystem.getInstance().setBrakeEngaged(true);
//            }
//        }

        if (Timer.getFPGATimestamp() - this.timestamp > 0.75) {
            ElevatorSubsystem.getInstance().setBrakeEngaged(true);
            compressor.setClosedLoopControl(true);
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
