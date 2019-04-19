package org.frc1923.robot.subsystems;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.frc1923.robot.Robot;
import org.frc1923.robot.RobotMap;
import org.frc1923.robot.commands.elevator.ElevatorControllerCommand;
import org.frc1923.robot.utilities.Dashboard;

public class ElevatorSubsystem extends Subsystem {

    private static ElevatorSubsystem instance;

    private TalonSRX[] talons;

    private DoubleSolenoid solenoid;
    private boolean brakeEngaged;

    public DoubleSolenoid forks = new DoubleSolenoid(0, 1);

    private int position;
    private int holdPosition;

    private ElevatorSubsystem() {
        this.talons = new TalonSRX[RobotMap.Elevator.TALONS.length];

        for (int i = 0; i < this.talons.length; i++) {
            this.talons[i] = RobotMap.Elevator.TALONS[i].createTalon();

            if (i > 0) {
                this.talons[i].follow(this.talons[0]);
            }
        }

        this.talons[0].configForwardLimitSwitchSource(
                RemoteLimitSwitchSource.RemoteTalonSRX,
                LimitSwitchNormal.NormallyOpen,
                RobotMap.Elevator.TALON_LIMITSWITCH_PORT
        );
        this.talons[0].configReverseLimitSwitchSource(
                RemoteLimitSwitchSource.RemoteTalonSRX,
                LimitSwitchNormal.NormallyOpen,
                RobotMap.Elevator.TALON_LIMITSWITCH_PORT
        );

        this.talons[0].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        this.talons[0].setSensorPhase(Robot.PRACTICE_ROBOT); //pbot = true, cbot = false
        this.talons[0].configSelectedFeedbackCoefficient(1.0);
        this.talons[0].configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0, 0);

        this.talons[0].selectProfileSlot(0, 0);
        this.talons[0].config_kP(0, 0.080);
        this.talons[0].config_kI(0, 0.000);
        this.talons[0].config_kD(0, 0.000);
        this.talons[0].config_kF(0, 0.085);
        this.talons[0].configAllowableClosedloopError(0, 1024);

        this.configureMotionSettings(4096 * 5, 4096 * 5);
        this.configureLimitSwitchEnable(true);

        this.solenoid = new DoubleSolenoid(RobotMap.Elevator.BRAKE_FWD_PORT, RobotMap.Elevator.BRAKE_REV_PORT);
        this.brakeEngaged = false;

        new Notifier(() -> {
            this.position = this.talons[0].getSelectedSensorPosition();
        }).startPeriodic(0.1);

        new Notifier(() -> {
            Dashboard.putBoolean("Elevator Fwd Lmt", this.talons[1].getSensorCollection().isFwdLimitSwitchClosed());
            Dashboard.putBoolean("Elevator Rev Lmt", this.talons[1].getSensorCollection().isRevLimitSwitchClosed());
            Dashboard.putNumber("Elevator Position", this.getPosition());
            Dashboard.putNumber("Elevator Hld Position", this.getHoldPosition());

            this.solenoid.set(this.brakeEngaged ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        }).startPeriodic(0.1);
    }

    public void configureMotionSettings(int velocity, int acceleration) {
        this.talons[0].configMotionCruiseVelocity(velocity);
        this.talons[0].configMotionAcceleration(acceleration);
    }

    public void configureLimitSwitchEnable(boolean enabled) {
        this.talons[0].overrideLimitSwitchesEnable(enabled);
    }

    public int getHoldPosition() {
        return this.holdPosition;
    }

    public void setHoldPosition(int holdPosition) {
        this.holdPosition = holdPosition;
    }

    public double getEncoderVelocity() {
        return this.talons[0].getSelectedSensorVelocity();
    }

    public void set(double output) {
        this.set(ControlMode.PercentOutput, output);
    }

    public void set(ControlMode controlMode, double output) {
        this.talons[0].set(controlMode, output);
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new ElevatorControllerCommand());
    }

    public void resetHoldPosition() {
        this.holdPosition = this.getPosition();
    }

    public static synchronized void initialize() {
        if (ElevatorSubsystem.instance == null) {
            ElevatorSubsystem.instance = new ElevatorSubsystem();
        }
    }

    public static ElevatorSubsystem getInstance() {
        return ElevatorSubsystem.instance;
    }

    public int getPosition() {
        return this.position;
    }

    public void setBrakeEngaged(boolean brakeEngaged) {
        this.brakeEngaged = brakeEngaged;
    }

    public boolean isBrakeEngaged() {
        return this.brakeEngaged;
    }

}
