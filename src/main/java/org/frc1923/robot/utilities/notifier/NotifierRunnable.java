package org.frc1923.robot.utilities.notifier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

import org.frc1923.robot.utilities.logger.Logger;

/**
 * Wrapper class for Runnable that prints loop overrun
 * messages.
 */
public class NotifierRunnable implements Runnable {

    private Runnable runnable;
    private String name;
    private double period;

    public NotifierRunnable(Runnable runnable, String name, double period) {
        this.runnable = runnable;
        this.name = name;
        this.period = period;
    }

    @Override
    public void run() {
        double startTime = Timer.getFPGATimestamp();

        this.runnable.run();

        double elapsedTime = Timer.getFPGATimestamp() - startTime;

        if (elapsedTime > this.period && !this.name.startsWith("NetworkLogger.Ping")) {
            DriverStation.reportWarning(this.name + " loop overrun: " + elapsedTime + "s", false);
            Logger.logEvent(this, "Loop Overrun", new Logger.DataPair("notifier", this.name), new Logger.DataPair("elapsedTime", elapsedTime));
        }
    }

    public String getName() {
        return this.name;
    }

    public double getPeriod() {
        return this.getPeriod();
    }

}
