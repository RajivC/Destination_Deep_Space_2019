package org.frc1923.robot.subsystems;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.Robot;
import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.wrist.WristHoldCommand;
import org.frc1923.robot.utilities.Dashboard;
import org.frc1923.robot.utilities.notifier.NamedNotifier;

public class WristSubsystem extends Subsystem {

    private static WristSubsystem instance;

    private TalonSRX talon;

    private int position;
    private int holdPosition;

    private ControlMode controlMode = ControlMode.PercentOutput;
    private double demand = 0;

    private WristSubsystem() {
        this.talon = RobotMap.Wrist.TALON.createTalon();

        this.talon.setSensorPhase(!Robot.PRACTICE_ROBOT); //pbot = false, cbot = true
        this.talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        this.talon.configSelectedFeedbackCoefficient(3600.0 / 4096);
        this.talon.configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0, 0);

        this.talon.selectProfileSlot(0, 0);
        this.talon.config_kP(0, 1.250);
        this.talon.config_kI(0, 0.000);
        this.talon.config_kD(0, 0.000);
        this.talon.config_kF(0, 3.250);
        this.talon.configAllowableClosedloopError(0, 30);

        this.talon.configMotionAcceleration(1114);
        this.talon.configMotionCruiseVelocity(400);

        this.holdPosition = this.getPosition();

        new NamedNotifier(() -> {
            Dashboard.putNumber("Wrist Position", this.getPosition());
            Dashboard.putBoolean("Wrist Rev Lmt", this.talon.getSensorCollection().isRevLimitSwitchClosed());

            this.position = this.talon.getSelectedSensorPosition();
        }, "IntakeSubsystem.Set0", 0.1).start();

        new NamedNotifier(() -> {
            this.talon.set(this.controlMode, this.demand);
        }, "IntakeSubsystem.Set1", 0.05).start();
    }

    public void setHoldPosition(int holdPosition) {
        this.holdPosition = holdPosition;
    }

    public int getHoldPosition() {
        return this.holdPosition;
    }

    public void set(double demand) {
        this.set(ControlMode.PercentOutput, demand);
    }

    public void resetHoldPosition() {
        this.holdPosition = this.getPosition();
    }

    public void set(ControlMode controlMode, double demand) {
        this.controlMode = controlMode;
        this.demand = demand;
    }

    public int getPosition() {
        return this.position;
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new WristHoldCommand());
    }

    public static synchronized void initialize() {
        if (WristSubsystem.instance == null) {
            WristSubsystem.instance = new WristSubsystem();
        }
    }

    public static WristSubsystem getInstance() {
        return WristSubsystem.instance;
    }

}