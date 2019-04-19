package org.frc1923.robot.utilities.controllers;

public class PS4Controller extends Controller {

    public final ExtendedJoystickButton square;
    public final ExtendedJoystickButton triangle;
    public final ExtendedJoystickButton circle;
    public final ExtendedJoystickButton cross;
    public final ExtendedJoystickButton leftBumper;
    public final ExtendedJoystickButton rightBumper;
    public final ExtendedJoystickButton share;
    public final ExtendedJoystickButton options;
    public final ExtendedJoystickButton rightClick;
    public final ExtendedJoystickButton leftClick;
    public final ExtendedJoystickButton pad;

    public final DirectionalPad dPad;

    // Raw axis values for triggers range from -1.0 (default) to 1.0 (fully pressed)
    private final Trigger leftTrigger;
    private final Trigger rightTrigger;

    private final ExtendedJoystickButton leftTriggerButton;
    private final ExtendedJoystickButton rightTriggerButton;

    public PS4Controller(int port) {
        super(port, 0, 1, 2, 5);

        this.square = new ExtendedJoystickButton(this.controller, 1);
        this.cross = new ExtendedJoystickButton(this.controller, 2);
        this.circle = new ExtendedJoystickButton(this.controller, 3);
        this.triangle = new ExtendedJoystickButton(this.controller, 4);
        this.leftBumper = new ExtendedJoystickButton(this.controller, 5);
        this.rightBumper = new ExtendedJoystickButton(this.controller, 6);
        this.leftTriggerButton = new ExtendedJoystickButton(this.controller, 7);
        this.rightTriggerButton = new ExtendedJoystickButton(this.controller, 8);
        this.share = new ExtendedJoystickButton(this.controller, 9);
        this.options = new ExtendedJoystickButton(this.controller, 10);
        this.leftClick = new ExtendedJoystickButton(this.controller, 11);
        this.rightClick = new ExtendedJoystickButton(this.controller, 12);
        this.pad = new ExtendedJoystickButton(this.controller, 14);

        this.dPad = new DirectionalPad(this.controller);

        this.leftTrigger = new Trigger(this.controller, 3);
        this.rightTrigger = new Trigger(this.controller, 4);
    }

    public double getLeftTrigger() {
        if (!this.leftTriggerButton.get()) {
            return 0;
        }

        return (this.leftTrigger.getX() + 1.0) / 2.0;
    }

    public double getRightTrigger() {
        if (!this.rightTriggerButton.get()) {
            return 0;
        }

        return (this.rightTrigger.getX() + 1.0) / 2.0;
    }

}