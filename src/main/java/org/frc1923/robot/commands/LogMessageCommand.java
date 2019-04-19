package org.frc1923.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;

import org.frc1923.robot.utilities.logger.Logger;

public class LogMessageCommand extends InstantCommand {

    private Object caller;
    private String message;
    private Logger.DataPair[] dataPairs;

    public LogMessageCommand(Object caller, String message, Logger.DataPair... dataPairs) {
        this.caller = caller;
        this.message = message;
        this.dataPairs = dataPairs;

        this.setRunWhenDisabled(true);
    }

    @Override
    protected void initialize() {
        Logger.logEvent(this.caller, this.message, this.dataPairs);
    }

}
