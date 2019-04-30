package org.frc1923.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.drivetrain.DriveControllerCommand;
import org.frc1923.robot.utilities.notifier.NamedNotifier;
import org.frc1923.robot.utilities.logger.Logger;

public class DrivetrainSubsystem extends Subsystem {

    private static DrivetrainSubsystem instance;

    private CANSparkMax[] leftSparks;
    private CANSparkMax[] rightSparks;

    private double leftDemand;
    private double rightDemand;

    private double lastUpdated;

    private DrivetrainSubsystem() {
        this.leftSparks = new CANSparkMax[RobotMap.Drivetrain.LEFT_SPARKS.length];
        this.rightSparks = new CANSparkMax[RobotMap.Drivetrain.RIGHT_SPARKS.length];

        for (int i = 0; i < this.leftSparks.length; i++) {
            this.leftSparks[i] = RobotMap.Drivetrain.LEFT_SPARKS[i].createSpark();

            if (i > 0) {
                this.leftSparks[i].follow(this.leftSparks[0]);
            }
        }

        for (int i = 0; i < this.rightSparks.length; i++) {
            this.rightSparks[i] = RobotMap.Drivetrain.RIGHT_SPARKS[i].createSpark();

            if (i > 0) {
                this.rightSparks[i].follow(this.rightSparks[0]);
            }
        }

        new NamedNotifier(() -> {
            if (DriverStation.getInstance().isDisabled()) {
                return;
            }

            if (Timer.getFPGATimestamp() - this.lastUpdated > 0.50) {
                Logger.logEvent(
                        this, "Drivetrain Safety Triggered",
                        new Logger.DataPair("leftDemand", this.leftDemand),
                        new Logger.DataPair("rightDemand", this.rightDemand)
                );

                this.leftDemand = 0;
                this.rightDemand = 0;
            }

            this.leftSparks[0].set(this.leftDemand);
            this.rightSparks[0].set(this.rightDemand);
        }, "DrivetrainSubsystem.Set0", 0.02).start();
    }

    public void set(double left, double right) {
        this.leftDemand = left;
        this.rightDemand = right;
        this.lastUpdated = Timer.getFPGATimestamp();
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new DriveControllerCommand());
    }

    public static synchronized void initialize() {
        if (DrivetrainSubsystem.instance == null) {
            DrivetrainSubsystem.instance = new DrivetrainSubsystem();
        }
    }

    public static DrivetrainSubsystem getInstance() {
        return DrivetrainSubsystem.instance;
    }

}
