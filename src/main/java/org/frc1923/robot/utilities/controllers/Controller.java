package org.frc1923.robot.utilities.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.frc1923.robot.commands.LogMessageCommand;
import org.frc1923.robot.utilities.logger.Logger;

public class Controller extends Joystick {

    private static final double DEFAULT_JOYSTICK_DEADZONE = 0.05;
    private static final double DEFAULT_TRIGGER_DEADZONE = 0.01;
    private static final double DEFAULT_TRIGGER_SENSITIVITY = 0.6;

    private final int port;
    private final int leftJoystickXAxisId;
    private final int leftJoystickYAxisId;
    private final int rightJoystickXAxisId;
    private final int rightJoystickYAxisId;

    protected final Joystick controller;

    public Controller(int port, int leftJoystickXAxisId, int leftJoystickYAxisId, int rightJoystickXAxisId, int rightJoystickYAxisId) {
        super(port);

        this.port = port;
        this.leftJoystickXAxisId = leftJoystickXAxisId;
        this.leftJoystickYAxisId = leftJoystickYAxisId;
        this.rightJoystickXAxisId = rightJoystickXAxisId;
        this.rightJoystickYAxisId = rightJoystickYAxisId;

        this.controller = new Joystick(this.port);
    }

    public enum Hand {

        LEFT, RIGHT

    }

    public enum DPad {

        UP(0), UP_RIGHT(45), RIGHT(90), DOWN_RIGHT(135), DOWN(180), DOWN_LEFT(225), LEFT(270), UP_LEFT(315);

        private int value;

        DPad(final int value) {
            this.value = value;
        }

    }

    public static class Trigger extends Button {

        private final Joystick parent;

        private double deadZone;
        private double sensitivity;
        private int axisId;

        Trigger(final Joystick joystick, final int axisId) {
            this.parent = joystick;
            this.axisId = axisId;
            this.deadZone = DEFAULT_TRIGGER_DEADZONE;
            this.sensitivity = DEFAULT_TRIGGER_SENSITIVITY;
        }

        @Override
        public boolean get() {
            return getX() > this.sensitivity;
        }

        public double getX() {
            final double rawInput = this.parent.getRawAxis(this.axisId);
            return createDeadZone(rawInput, this.deadZone);
        }

        public void setTriggerDeadZone(double number) {
            this.deadZone = number;
        }

        public void setTriggerSensitivity(double number) {
            this.sensitivity = number;
        }

    }

    public static class DirectionalPad extends Button {

        private final Joystick parent;

        public final Button up;
        public final Button upRight;
        public final Button right;
        public final Button downRight;
        public final Button down;
        public final Button downLeft;
        public final Button left;
        public final Button upLeft;

        DirectionalPad(final Joystick parent) {
            this.parent = parent;
            this.up = new DPadButton(this, DPad.UP);
            this.upRight = new DPadButton(this, DPad.UP_RIGHT);
            this.right = new DPadButton(this, DPad.RIGHT);
            this.downRight = new DPadButton(this, DPad.DOWN_RIGHT);
            this.down = new DPadButton(this, DPad.DOWN);
            this.downLeft = new DPadButton(this, DPad.DOWN_LEFT);
            this.left = new DPadButton(this, DPad.LEFT);
            this.upLeft = new DPadButton(this, DPad.UP_LEFT);
        }

        public static class DPadButton extends Button {
            private final DPad direction;
            private final DirectionalPad parent;

            DPadButton(final DirectionalPad parent, final DPad dPadDirection) {
                this.direction = dPadDirection;
                this.parent = parent;

                this.whenPressed(new LogMessageCommand(
                        this, "dPad Pressed",
                        new Logger.DataPair("joystick", this.parent.getParent().getPort()),
                        new Logger.DataPair("direction", this.direction.name())
                ));
                this.whenReleased(new LogMessageCommand(
                        this, "dPad Released",
                        new Logger.DataPair("joystick", this.parent.getParent().getPort()),
                        new Logger.DataPair("direction", this.direction.name())
                ));
            }

            @Override
            public boolean get() {
                return this.parent.getAngle() == direction.value;
            }
        }

        private int angle() {
            if (this.parent.getPOVCount() < 1) {
                return -1;
            }

            return this.parent.getPOV();
        }

        @Override
        public boolean get() {
            return angle() != -1;
        }

        public int getAngle() {
            return angle();
        }

        public Joystick getParent() {
            return this.parent;
        }

    }

    private static double createDeadZone(double input, double deadZoneSize) {
        final double negative;
        double deadZoneSizeClamp = deadZoneSize;
        double adjusted;

        if (deadZoneSizeClamp < 0 || deadZoneSizeClamp >= 1) {
            deadZoneSizeClamp = 0;
        }

        negative = input < 0 ? -1 : 1;

        adjusted = Math.abs(input) - deadZoneSizeClamp;
        adjusted = adjusted < 0 ? 0 : adjusted;
        adjusted = adjusted / (1 - deadZoneSizeClamp);

        return negative * adjusted;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    public double getLeftX() {
        double val = this.getRawAxis(this.leftJoystickXAxisId);
        return Math.abs(val) > DEFAULT_JOYSTICK_DEADZONE ? val : 0;
    }

    public double getLeftY() {
        double val = -this.getRawAxis(this.leftJoystickYAxisId);
        return Math.abs(val) > DEFAULT_JOYSTICK_DEADZONE ? val : 0;
    }

    public double getRightX() {
        double val = this.getRawAxis(this.rightJoystickXAxisId);
        return Math.abs(val) > DEFAULT_JOYSTICK_DEADZONE ? val : 0;
    }

    public double getRightY() {
        double val = -this.getRawAxis(this.rightJoystickYAxisId);
        return Math.abs(val) > DEFAULT_JOYSTICK_DEADZONE ? val : 0;
    }

    public Joystick getJoystick() {
        return this.controller;
    }

    public void setRumble(Hand hand, double intensity) {
        this.controller.setRumble(hand == Hand.LEFT ? RumbleType.kLeftRumble : RumbleType.kRightRumble, (float) intensity);
    }

    public void setRumble(double intensity) {
        this.controller.setRumble(RumbleType.kLeftRumble, (float) intensity);
        this.controller.setRumble(RumbleType.kRightRumble, (float) intensity);
    }

}
