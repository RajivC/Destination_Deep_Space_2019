package org.frc1923.robot.utilities.notifier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;

import java.lang.reflect.Field;

/**
 * Allows for the naming of a Notifier object (thread)
 * for easier debugging while using JMX/VisualVM
 */
public class NamedNotifier extends Notifier {

    private String name;
    private double period;

    public NamedNotifier(Runnable runnable, String name, double period) {
        super(new NotifierRunnable(runnable, name, period));

        this.name = name;
        this.period = period;

        try {
            Field notifierThread = Notifier.class.getDeclaredField("m_thread");
            notifierThread.setAccessible(true);

            ((Thread) notifierThread.get(this)).setName("Notifier-" + this.name);
        } catch (Exception e) {
            DriverStation.reportError("Unable to set Notifier thread name to " + this.name, e.getStackTrace());
        }
    }

    @Override
    public void startPeriodic(double period) {
        if (period != this.period) {
            DriverStation.reportError("Unable to change Notifier period for " + this.name, false);
        } else {
            this.start();
        }
    }

    public void start() {
        super.startPeriodic(this.period);
    }

}
