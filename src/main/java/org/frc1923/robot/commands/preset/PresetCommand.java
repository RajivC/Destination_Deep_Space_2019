package org.frc1923.robot.commands.preset;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

import org.frc1923.robot.commands.elevator.ElevatorPositionCommand;
import org.frc1923.robot.commands.intake.IntakeSlideCommand;
import org.frc1923.robot.commands.wrist.WristPositionCommand;
import org.frc1923.robot.subsystems.ClawSubsystem;
import org.frc1923.robot.subsystems.ElevatorSubsystem;
import org.frc1923.robot.subsystems.IntakeSubsystem;
import org.frc1923.robot.subsystems.WristSubsystem;
import org.frc1923.robot.utilities.CGUtils;
import org.frc1923.robot.utilities.logger.Logger;

public class PresetCommand extends Command {

    private Preset preset;

    private Command commandGroup;

    public PresetCommand(Preset preset) {
        this.preset = preset;
    }

    @Override
    protected void initialize() {
        if (WristSubsystem.getInstance().getPosition() < 900 && ElevatorSubsystem.getInstance().getPosition() > 18000) {
            Logger.logEvent(
                    this, "Cancelling initialization (#1)",
                    new Logger.DataPair("wristPosition", WristSubsystem.getInstance().getPosition()),
                    new Logger.DataPair("elevatorPosition", ElevatorSubsystem.getInstance().getPosition()),
                    new Logger.DataPair("preset", this.preset.name())
            );

            return;
        }

        if ((this.preset == Preset.SHIP_FRONT || this.preset == Preset.SHIP || this.preset == Preset.CLIMB) && !ClawSubsystem.getInstance().isOpen()) {
            ClawSubsystem.getInstance().setOpen(true);
        }

        Logger.logEvent(
                this, "Initializing",
                new Logger.DataPair("wristPosition", WristSubsystem.getInstance().getPosition()),
                new Logger.DataPair("elevatorPosition", ElevatorSubsystem.getInstance().getPosition()),
                new Logger.DataPair("clawOpen", ClawSubsystem.getInstance().isOpen()),
                new Logger.DataPair("preset", this.preset.name())
        );

        this.commandGroup = CGUtils.sequential(
                new ConditionalCommand(new IntakeSlideCommand(true, true)) {
                    @Override
                    protected boolean condition() {
                        return WristSubsystem.getInstance().getPosition() < 900
                                && !IntakeSubsystem.getInstance().isExtended();
                    }
                },
                new ConditionalCommand(new WristPositionCommand(ClawSubsystem.getInstance().isOpen() ? 135 : 170)) {
                    @Override
                    protected boolean condition() {
                        // Ensure the wrist doesn't move out again when returning to base preset or front ship preset
                        return (PresetCommand.this.preset != Preset.BASE
                                && PresetCommand.this.preset != Preset.SHIP_FRONT)
                                || WristSubsystem.getInstance().getPosition() >= 900;
                    }
                },
                new ConditionalCommand(new ElevatorPositionCommand(
                        ClawSubsystem.getInstance().isOpen()
                                ? this.preset.getCargoPosition()
                                : this.preset.getHatchPosition()
                )) {
                    @Override
                    protected boolean condition() {
                        // Triple check to ensure that the wrist is in the correct position before
                        // moving the elevator up or down
                        return WristSubsystem.getInstance().getPosition() > 1100; // > 110 degrees
                    }
                },
                new ConditionalCommand(CGUtils.sequential(
                        // Intake must always be out
                        new IntakeSlideCommand(true, true),
                        new WristPositionCommand(0)
                )) {
                    @Override
                    protected boolean condition() {
                        return PresetCommand.this.preset == Preset.BASE;
                    }
                },
                new ConditionalCommand(CGUtils.sequential(
                        // Intake must always be out
                        new IntakeSlideCommand(true, true),
                        new WristPositionCommand(70)
                )) {
                    @Override
                    protected boolean condition() {
                        return PresetCommand.this.preset == Preset.SHIP_FRONT;
                    }
                },
                new ConditionalCommand(new WristPositionCommand(170)) {
                    @Override
                    protected boolean condition() {
                        return PresetCommand.this.preset == Preset.SHIP;
                    }
                }

        );
        this.commandGroup.start();
    }

    @Override
    protected void end() {
        Logger.logEvent(this, "Ending", new Logger.DataPair("preset", this.preset.name()));
        this.commandGroup.cancel();
    }

    @Override
    protected void interrupted() {
        Logger.logEvent(this, "Interrupted", new Logger.DataPair("preset", this.preset.name()));
        this.end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    public enum Preset {

        BASE(0, 0),
        LOW(15000, 12500),
        MED(54500, 49500),
        HIGH(93500, 89000),
        SHIP(43500, 43500),
        SHIP_FRONT(0, 0),
        CLIMB(58500, 58500);

        private int hatchPosition;
        private int cargoPosition;

        Preset(int hatchPosition, int cargoPosition) {
            this.hatchPosition = hatchPosition;
            this.cargoPosition = cargoPosition;
        }

        public int getHatchPosition() {
            return this.hatchPosition;
        }

        public int getCargoPosition() {
            return this.cargoPosition;
        }

    }

}
