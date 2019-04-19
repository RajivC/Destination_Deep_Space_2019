package org.frc1923.robot.utilities;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class MotorController {

    private int deviceId;

    private boolean inverted;

    public MotorController(int deviceId) {
        this(deviceId, false);
    }

    public MotorController(int deviceId, boolean inverted) {
        this.deviceId = deviceId;
        this.inverted = inverted;
    }

    public int getDeviceId() {
        return this.deviceId;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    public CANSparkMax createSpark() {
        CANSparkMax canSparkMax = new CANSparkMax(this.getDeviceId(), CANSparkMaxLowLevel.MotorType.kBrushless);
        canSparkMax.restoreFactoryDefaults();
        canSparkMax.setInverted(this.isInverted());

        return canSparkMax;
    }

    public TalonSRX createTalon() {
        TalonSRX talonSRX = new TalonSRX(this.getDeviceId());
        talonSRX.configFactoryDefault();
        talonSRX.setInverted(this.isInverted());

        talonSRX.enableVoltageCompensation(true);
        talonSRX.configVoltageCompSaturation(11);

        return talonSRX;
    }

}
