package org.frc1923.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class VisionToggleCommand extends Command {

    private static VisionToggleCommand instance;

    private String[] streamUrls = new String[]{
            "http://10.19.23.8:5800",
            "http://10.19.23.9:5800"
    };

    private int index;

    private VisionToggleCommand() {
        this.index = 0;
        this.setRunWhenDisabled(true);
    }

    @Override
    protected void initialize() {
        this.index = this.index + 1 >= this.streamUrls.length ? 0 : this.index + 1;
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    public String getStreamUrl() {
        return this.streamUrls[this.index];
    }

    public static synchronized VisionToggleCommand getInstance() {
        if (instance == null) {
            instance = new VisionToggleCommand();
        }

        return instance;
    }

}
