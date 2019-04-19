package org.frc1923.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.frc1923.robot.dashboard.WebServer;
import org.frc1923.robot.subsystems.*;
import org.frc1923.robot.utilities.Limelight;
import org.frc1923.robot.utilities.logger.Logger;

public class Robot extends TimedRobot {

    private Thread webServer;

    public static Limelight frontLimelight;
    public static Limelight backLimelight;

    public static final boolean DEBUG = false;
    public static final boolean PRACTICE_ROBOT = true;

    @Override
    public void robotInit() {
        double startTime = Timer.getFPGATimestamp();

        this.webServer = new Thread(new WebServer());
        this.webServer.start();

        frontLimelight = new Limelight("front");
        backLimelight = new Limelight("back");

        ClawSubsystem.initialize();
        ClimberSubsystem.initialize();
        DrivetrainSubsystem.initialize();
        ElevatorSubsystem.initialize();
        IntakeSubsystem.initialize();
        WristSubsystem.initialize();

        OI.initialize();

        LiveWindow.disableAllTelemetry();

        Logger.logEvent(this, "robotInit() completed", new Logger.DataPair("elapsedTime", Timer.getFPGATimestamp() - startTime));
    }

    @Override
    public void teleopInit() {
        Logger.logEvent(this, "Robot state changed", new Logger.DataPair("robotState", "TELEOP"));

        WristSubsystem.getInstance().resetHoldPosition();
        ElevatorSubsystem.getInstance().resetHoldPosition();
        ElevatorSubsystem.getInstance().setBrakeEngaged(false);
    }

    @Override
    public void autonomousInit() {
        Logger.logEvent(this, "Robot state changed", new Logger.DataPair("robotState", "AUTONOMOUS"));

        WristSubsystem.getInstance().resetHoldPosition();
        ElevatorSubsystem.getInstance().resetHoldPosition();
        ElevatorSubsystem.getInstance().setBrakeEngaged(false);
    }

    @Override
    public void disabledInit() {
        Logger.logEvent(this, "Robot state changed", new Logger.DataPair("robotState", "DISABLED"));

        WristSubsystem.getInstance().set(0);
        ElevatorSubsystem.getInstance().set(0);
    }

    @Override
    public void disabledPeriodic() {
        WristSubsystem.getInstance().resetHoldPosition();
        ElevatorSubsystem.getInstance().resetHoldPosition();
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
    }

    public static void main(String[] args) {
        RobotBase.startRobot(Robot::new);
    }

}