package org.frc1923.robot.utilities.controllers;

public class XboxController extends Controller {

    private static final double TRIGGER_DEADZONE = 0.2;

    public final ExtendedJoystickButton a;
    public final ExtendedJoystickButton b;
    public final ExtendedJoystickButton x;
    public final ExtendedJoystickButton y;
    public final ExtendedJoystickButton leftBumper;
    public final ExtendedJoystickButton rightBumper;
    public final ExtendedJoystickButton back;
    public final ExtendedJoystickButton start;
    public final ExtendedJoystickButton rightClick;
    public final ExtendedJoystickButton leftClick;

    public final DirectionalPad dPad;

    private final Trigger leftTrigger;
    private final Trigger rightTrigger;

    public XboxController(int port) {
        super(port, 0, 1, 4, 5);

        this.a = new ExtendedJoystickButton(this.controller, 1);
        this.b = new ExtendedJoystickButton(this.controller, 2);
        this.x = new ExtendedJoystickButton(this.controller, 3);
        this.y = new ExtendedJoystickButton(this.controller, 4);
        this.leftBumper = new ExtendedJoystickButton(this.controller, 5);
        this.rightBumper = new ExtendedJoystickButton(this.controller, 6);
        this.back = new ExtendedJoystickButton(this.controller, 7);
        this.start = new ExtendedJoystickButton(this.controller, 8);
        this.leftClick = new ExtendedJoystickButton(this.controller, 9);
        this.rightClick = new ExtendedJoystickButton(this.controller, 10);

        this.dPad = new DirectionalPad(this.controller);

        this.leftTrigger = new Trigger(this.controller, 2);
        this.rightTrigger = new Trigger(this.controller, 3);
        this.leftTrigger.setTriggerDeadZone(TRIGGER_DEADZONE);
        this.rightTrigger.setTriggerDeadZone(TRIGGER_DEADZONE);
    }

    public double getLeftTrigger() {
        return this.leftTrigger.getX();
    }

    public double getRightTrigger() {
        return this.rightTrigger.getX();
    }

}