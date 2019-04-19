package org.frc1923.robot;

import org.frc1923.robot.utilities.MotorController;

public abstract class RobotMap {

    public static abstract class Claw {

        public static final MotorController LEFT_SPARK = new MotorController(4);
        public static final MotorController RIGHT_SPARK = new MotorController(5, true);

        public static final int FWD_PORT = 6;
        public static final int REV_PORT = 7;

    }

    public static abstract class Climber {

        public static final MotorController[] TALONS = {
                new MotorController(4),
                new MotorController(5)
        };

    }

    public static abstract class Drivetrain {

        public static final MotorController[] LEFT_SPARKS = {
                new MotorController(6, true),
                new MotorController(1, true)
        };
        public static final MotorController[] RIGHT_SPARKS = {
                new MotorController(2, false),
                new MotorController(3, false)
        };

    }

    public static abstract class Elevator {

        public static final MotorController[] TALONS = {
                new MotorController(0, false),
                new MotorController(1, true),
                new MotorController(2, false),
                new MotorController(3, true)
        };


        public static final int TALON_LIMITSWITCH_PORT = 1;


        public static final int BRAKE_FWD_PORT = 4;
        public static final int BRAKE_REV_PORT = 5;

    }

    public static abstract class Intake {

        public static final MotorController TALON = new MotorController(6);

        public static final int FWD_PORT = 3;
        public static final int REV_PORT = 2;

    }

    public static abstract class Wrist {

        public static final MotorController TALON = new MotorController(7);

    }

}
