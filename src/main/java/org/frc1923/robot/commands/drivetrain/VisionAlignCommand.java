package org.frc1923.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.utilities.Limelight;
import org.frc1923.robot.OI;
import org.frc1923.robot.Robot;
import org.frc1923.robot.subsystems.DrivetrainSubsystem;
import org.frc1923.robot.utilities.logger.Logger;
import org.frc1923.robot.utilities.MovingAverage;

public class VisionAlignCommand extends Command {

    private Limelight limelight;

    private MovingAverage tx;
    private MovingAverage ta;

    private boolean targetValid;

    private double startTime;
    private static final double WAIT_TIME = 0.2;

    public VisionAlignCommand(Limelight limelight) {
        this.requires(DrivetrainSubsystem.getInstance());

        this.limelight = limelight;

        this.tx = new MovingAverage(3);
        this.ta = new MovingAverage(3);
    }

    @Override
    protected void initialize() {
        this.startTime = Timer.getFPGATimestamp();
        this.limelight.setEnableTargeting(true);

        this.tx.clear();
        this.ta.clear();

        this.tx.add(this.limelight.getTargetX());
        this.ta.add(this.limelight.getTargetArea());

        this.targetValid = this.limelight.isTargetValid();

        Logger.logEvent(this, "Initializing", new Logger.DataPair("location", this.limelight.getLocation()));
    }

    @Override
    protected void execute() {
        if (Timer.getFPGATimestamp() - this.startTime < WAIT_TIME) {
            return;
        }

        if (!this.targetValid && this.limelight.isTargetValid()) {
            Logger.logEvent(this, "Found valid target");
        }

        if (this.targetValid && !this.limelight.isTargetValid()) {
            Logger.logEvent(this, "Lost valid target");
        }

        this.tx.add(this.limelight.getTargetX());
        this.ta.add(this.limelight.getTargetArea());
        this.targetValid = this.limelight.isTargetValid();

        double tx = this.tx.get();
        double ta = this.ta.get();

        double turningConstant = 0.155;

        double turn;
        double power;

        if (this.limelight.getLocation().equals("limelight-front")) {
            turn = (turningConstant / 20) * tx;
            power = (15 - ta) * (0.025);
        } else {
            turn = (turningConstant / 20) * tx;
            power = -Math.pow(2.8, -1.0 / 3.0 * (ta + 1.2));
        }

        if (!Robot.PRACTICE_ROBOT) {
            power *= -1;
        }

        if (!this.targetValid) {
            power = Math.pow(OI.getInstance().getDriver().getLeftY(), 3) * (!Robot.PRACTICE_ROBOT ? -1 : 1);
            turn = Math.pow(OI.getInstance().getDriver().getRightX(), 3);

            this.tx.clear();
            this.ta.clear();
        }

        double left = bound(power + turn);
        double right = bound(power - turn);

        DrivetrainSubsystem.getInstance().set(left, right);
    }

    @Override
    protected boolean isFinished() {
        if (!this.targetValid) {
            return false;
        }

        if (Timer.getFPGATimestamp() - this.startTime < WAIT_TIME) {
            return false;
        }

        if (this.tx.get() == 0 && this.ta.get() == 0) {
            return false;
        }

        if (this.limelight.getLocation().equals("limelight-front")) {
            return this.tx.get() < 2 && this.ta.get() > 13 && this.targetValid;
        } else {
            return this.tx.get() < 2 && this.ta.get() > 7.5 && this.targetValid;
        }
    }

    @Override
    protected void end() {
        this.limelight.setEnableTargeting(false);

        Logger.logEvent(
                this, "Ending",
                new Logger.DataPair("targetX", this.tx.get()),
                new Logger.DataPair("targetArea", this.ta.get()),
                new Logger.DataPair("targetValid", this.targetValid),
                new Logger.DataPair("cameraLocation", this.limelight.getLocation()),
                new Logger.DataPair("runtime", Timer.getFPGATimestamp() - this.startTime)
        );
    }

    @Override
    protected void interrupted() {
        Logger.logEvent(this, "Interrupted");
        this.end();
    }

    protected double bound(double val) {
        if (val > 0.90) {
            return 0.90;
        }

        if (val < -0.90) {
            return -0.90;
        }

        return val;
    }

}
