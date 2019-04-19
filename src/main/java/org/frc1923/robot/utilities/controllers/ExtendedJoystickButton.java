package org.frc1923.robot.utilities.controllers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

import org.frc1923.robot.commands.LogMessageCommand;
import org.frc1923.robot.utilities.logger.Logger;

public class ExtendedJoystickButton extends JoystickButton {

    public ExtendedJoystickButton(GenericHID joystick, int buttonNumber) {
        super(joystick, buttonNumber);

        this.whenPressed(new LogMessageCommand(
                this, "Button Pressed",
                new Logger.DataPair("joystick", joystick.getPort()),
                new Logger.DataPair("buttonNumber", buttonNumber)
        ));
        this.whenReleased(new LogMessageCommand(
                this, "Button Released",
                new Logger.DataPair("joystick", joystick.getPort()),
                new Logger.DataPair("buttonNumber", buttonNumber)
        ));
    }

    public void whileHeldOnce(Command command) {
        new ButtonScheduler() {

            private boolean lastPressed = ExtendedJoystickButton.super.get();

            @Override
            public void execute() {
                boolean pressed = ExtendedJoystickButton.super.get();

                if (!this.lastPressed && pressed) {
                    command.start();
                }

                if (!pressed) {
                    command.cancel();
                }

                this.lastPressed = pressed;
            }

        }.start();
    }

}
