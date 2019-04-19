package org.frc1923.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.ElevatorSubsystem;
import org.frc1923.robot.subsystems.IntakeSubsystem;
import org.frc1923.robot.subsystems.WristSubsystem;
import org.frc1923.robot.utilities.logger.Logger;

public class WristPositionCommand extends Command {

    private int target;

    private boolean safe;

    public WristPositionCommand(double targetAngle) {
        this.requires(WristSubsystem.getInstance());

        if (targetAngle < 0 || targetAngle > 180) {
            throw new IllegalArgumentException("Invalid angle.");
        }

        this.target = (int) (targetAngle * 10);
        this.setTimeout(1.5);
    }

    @Override
    protected void initialize() {
        this.safe = true;

        int wristPosition = WristSubsystem.getInstance().getPosition();
        int elevatorPosition = ElevatorSubsystem.getInstance().getPosition();
        boolean intakeExtended = IntakeSubsystem.getInstance().isExtended();

        if (elevatorPosition < 1000) {
            if (wristPosition <= 100 && !intakeExtended && this.target > 100) {
                this.logUnsafeCondition("Safety #1", wristPosition, elevatorPosition, intakeExtended);
                this.safe = false;
            }

            if (wristPosition >= 300 && !intakeExtended && this.target < 400) {
                this.logUnsafeCondition("Safety #2", wristPosition, elevatorPosition, intakeExtended);
                this.safe = false;
            }
        }

        if (elevatorPosition > 4000) {
            if (wristPosition >= 900 && this.target < 900) {
                this.logUnsafeCondition("Safety #3", wristPosition, elevatorPosition, intakeExtended);
                this.safe = false;
            }

            if (WristSubsystem.getInstance().getPosition() < 900 && this.target >= 900) {
                this.logUnsafeCondition("Safety #4", wristPosition, elevatorPosition, intakeExtended);
                this.safe = false;
            }
        }

        Logger.logEvent(
                this, "Initializing",
                new Logger.DataPair("safe", this.safe),
                new Logger.DataPair("target", this.target),
                new Logger.DataPair("wristPosition", wristPosition),
                new Logger.DataPair("elevatorPosition", elevatorPosition),
                new Logger.DataPair("intakeExtended", intakeExtended)
        );
    }

    @Override
    protected void end() {
        Logger.logEvent(this, "Ending, setting new holdPosition", new Logger.DataPair("holdPosition", this.target));
        WristSubsystem.getInstance().setHoldPosition(this.target);
    }

    @Override
    protected void execute() {
        if (this.safe) {
            WristSubsystem.getInstance().set(ControlMode.MotionMagic, this.target);
        } else {
            this.logUnsafeCondition(
                    "RUNTIME: System Unsafe",
                    WristSubsystem.getInstance().getPosition(),
                    ElevatorSubsystem.getInstance().getPosition(),
                    IntakeSubsystem.getInstance().isExtended()
            );
        }
    }

    @Override
    protected boolean isFinished() {
        boolean isFinished = !this.safe || Math.abs(this.target - WristSubsystem.getInstance().getPosition()) < 100 || this.isTimedOut();

        if (isFinished) {
            Logger.logEvent(
                    this, "isFinished() returning true",
                    new Logger.DataPair("safe", this.safe),
                    new Logger.DataPair("targetError", this.target - WristSubsystem.getInstance().getPosition()),
                    new Logger.DataPair("wristPosition", WristSubsystem.getInstance().getPosition()),
                    new Logger.DataPair("timedOut", this.isTimedOut())
            );
        }

        return isFinished;
    }

    protected void logUnsafeCondition(String conditionId, int wristPosition, int elevatorPosition, boolean intakeExtended) {
        Logger.logEvent(
                this,
                "Safety/Unsafe Condition: " + conditionId,
                new Logger.DataPair("wristPosition", wristPosition),
                new Logger.DataPair("elevatorPosition", elevatorPosition),
                new Logger.DataPair("intakeExtended", intakeExtended),
                new Logger.DataPair("target", this.target)
        );
    }

}