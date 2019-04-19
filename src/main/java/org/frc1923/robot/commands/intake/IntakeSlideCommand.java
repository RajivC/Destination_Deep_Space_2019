package org.frc1923.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.subsystems.IntakeSubsystem;

public class IntakeSlideCommand extends Command {

    private boolean desiredState;
    private boolean waitForCompletion;

    private boolean toggle;

    private double timestamp;

    public IntakeSlideCommand() {
        this(false, false);

        this.toggle = true;
    }

    public IntakeSlideCommand(boolean desiredState) {
        this(desiredState, false);
    }

    public IntakeSlideCommand(boolean desiredState, boolean waitForCompletion) {
        this.desiredState = desiredState;
        this.waitForCompletion = waitForCompletion;
    }

    @Override
    protected void initialize() {
        boolean currentState = IntakeSubsystem.getInstance().isExtended();
        this.timestamp = Timer.getFPGATimestamp();

        if (this.toggle) {
            this.desiredState = !currentState;
        }

        if (currentState == this.desiredState) {
            this.timestamp -= 10; // "trick" the command into finishing faster
        } else {
            IntakeSubsystem.getInstance().setExtended(this.desiredState);
        }
    }

    @Override
    protected boolean isFinished() {
        return !this.waitForCompletion || Timer.getFPGATimestamp() - this.timestamp > 0.50;
    }

}
