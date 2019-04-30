package org.frc1923.robot.utilities.logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

import org.frc1923.robot.utilities.notifier.NamedNotifier;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {

    private static Queue<String> buffer;
    private static BufferedWriter writer;

    private static boolean usbPresent;

    static {
        double start = Timer.getFPGATimestamp();

        buffer = new ConcurrentLinkedQueue<>();

        File loggingDir = new File("/media/sda1");

        usbPresent = loggingDir.exists() && loggingDir.isDirectory();

        try {
            writer = new BufferedWriter(new FileWriter("/media/sda1/robot-" + System.currentTimeMillis() + ".log"));
        } catch (Exception e) {
            usbPresent = false;
        }

        new NamedNotifier(() -> {
            while (!buffer.isEmpty()) {
                String message = buffer.poll();

                if (!usbPresent) {
                    System.out.println(message);
                }

                if (!usbPresent) {
                    return;
                }

                try {
                    writer.write(message);
                    writer.newLine();
                } catch (Exception e) {

                }
            }

            if (usbPresent) {
                try {
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Logger.FileWriter", 0.25).start();

        new NamedNotifier(() -> {
            DriverStation ds = DriverStation.getInstance();
            Logger.logEvent(
                    "DriverStation", "Debug",
                    new DataPair("dsAttached", ds.isDSAttached()),
                    new DataPair("fmsAttached", ds.isFMSAttached()),
                    new DataPair("event", ds.getEventName()),
                    new DataPair("alliance", ds.getAlliance().name()),
                    new DataPair("location", ds.getLocation()),
                    new DataPair("matchType", ds.getMatchType()),
                    new DataPair("matchNumber", ds.getMatchNumber()),
                    new DataPair("matchReplayNumber", ds.getReplayNumber()),
                    new DataPair("matchTime", ds.getMatchTime()),
                    new DataPair("gameSpecificMessage", ds.getGameSpecificMessage())
            );
        }, "Logger.MatchReport", 2.5).start();

        NetworkLogger.startLogger();

        System.out.println("Done starting in " + (Timer.getFPGATimestamp() - start) + " seconds.");
    }

    public static void logEvent(Object caller, String message, DataPair... dataPairs) {
        StringBuilder sb = new StringBuilder();

        if (!(caller instanceof String)) {
            caller = caller.getClass().getSimpleName();
        }

        sb.append("[").append(caller).append("@").append(Timer.getFPGATimestamp()).append("] ")
                .append(message);

        if (dataPairs.length > 0) {
            sb.append(" {");

            for (DataPair dataPair : dataPairs) {
                sb.append(dataPair.getKey()).append("=").append(dataPair.getValue()).append(", ");
            }

            sb.append("}");
        }

        buffer.add(sb.toString().replaceAll(", }$", "}"));
    }

    public static class DataPair {

        private String key;
        private Object value;

        public DataPair(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

    }

}
