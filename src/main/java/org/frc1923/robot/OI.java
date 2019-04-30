package org.frc1923.robot;

import org.frc1923.robot.commands.claw.ClawOutputCommand;
import org.frc1923.robot.commands.claw.ClawToggleCommand;
import org.frc1923.robot.commands.climber.ForkCommand;
import org.frc1923.robot.commands.VisionToggleCommand;
import org.frc1923.robot.commands.climber.ClimbBrakeCommand;
import org.frc1923.robot.commands.drivetrain.DriveOutputCommand;
import org.frc1923.robot.commands.drivetrain.VisionAlignCommand;
import org.frc1923.robot.commands.elevator.ElevatorBrakeToggleCommand;
import org.frc1923.robot.commands.elevator.ElevatorPositionCommand;
import org.frc1923.robot.commands.intake.IntakeSlideCommand;
import org.frc1923.robot.commands.preset.PresetCommand;
import org.frc1923.robot.commands.wrist.WristControllerCommand;
import org.frc1923.robot.utilities.CGUtils;
import org.frc1923.robot.utilities.controllers.PS4Controller;
import org.frc1923.robot.utilities.controllers.XboxController;

public class OI {

    private static OI instance;

    private PS4Controller driver;
    private XboxController operator;

    private OI() {
        this.driver = new PS4Controller(0);
        this.operator = new XboxController(1);

        this.operator.leftClick.whenPressed(new ClawToggleCommand());
        this.operator.rightClick.whenPressed(new IntakeSlideCommand());

        this.operator.dPad.up.whileHeld(new WristControllerCommand(0.35));
        this.operator.dPad.down.whileHeld(new WristControllerCommand(-0.35));
        this.operator.dPad.left.whenPressed(new ElevatorBrakeToggleCommand());
        this.operator.dPad.right.whenPressed(new ForkCommand());

        this.operator.y.whileHeld(new PresetCommand(PresetCommand.Preset.HIGH));
        this.operator.b.whileHeld(new PresetCommand(PresetCommand.Preset.MED));
        this.operator.a.whileHeld(new PresetCommand(PresetCommand.Preset.LOW));
        this.operator.x.whileHeld(new PresetCommand(PresetCommand.Preset.BASE));

        this.operator.rightBumper.whileHeld(new PresetCommand(PresetCommand.Preset.SHIP));
        this.operator.leftBumper.whileHeld(new PresetCommand(PresetCommand.Preset.SHIP_FRONT));

        this.operator.back.whileHeld(new ClawOutputCommand(0.40));

        this.driver.leftBumper.whenPressed(VisionToggleCommand.getInstance());
        this.driver.rightBumper.whileHeldOnce(new VisionAlignCommand(Robot.backLimelight));
        this.driver.circle.whileHeldOnce(new VisionAlignCommand(Robot.frontLimelight));
        this.driver.square.whileHeldOnce(new VisionAlignCommand(Robot.backLimelight));

        this.driver.pad.whileHeld(new DriveOutputCommand(0.60));

        this.driver.triangle.whileHeldOnce(new PresetCommand(PresetCommand.Preset.CLIMB));
        this.driver.cross.whileHeldOnce(CGUtils.parallel(
                new ElevatorPositionCommand(-6144, 5120, 5120, false),
                new ClimbBrakeCommand()
        ));
    }

    public PS4Controller getDriver() {
        return this.driver;
    }

    public XboxController getOperator() {
        return this.operator;
    }

    public static synchronized void initialize() {
        if (OI.instance == null) {
            OI.instance = new OI();
        }
    }

    public static OI getInstance() {
        return OI.instance;
    }

}
