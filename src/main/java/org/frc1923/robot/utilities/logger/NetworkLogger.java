package org.frc1923.robot.utilities.logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkLogger {

    private static Host[] hosts = {
            new Host("robotRadio", "10.19.23.1"),
            new Host("roboRIO.eth0", "10.19.23.2"),
            new Host("roboRIO.lo", "127.0.0.1"),
            new Host("fieldRadio", "10.19.23.4"),
            new Host("driverStation", "10.19.23.5"),
            new Host("limelightFront", "10.19.23.8"),
            new Host("limelightBack", "10.19.23.9")
    };
    private static Map<Host, Double> rtt = new ConcurrentHashMap<>();

    private static boolean fmsPresent;

    public static void startLogger() {
        for (Host host : hosts) {
            new Notifier(() -> {
                double tripTime = ping(host.getAddress());

                if (tripTime > 0 && rtt.getOrDefault(host, -1.0) == -1) {
                    Logger.logEvent(
                            "NetworkLogger", "Host now reachable",
                            new Logger.DataPair("host", host.getName()),
                            new Logger.DataPair("rtt", tripTime)
                    );
                } else if (tripTime < 0 && rtt.getOrDefault(host, -1.0) > 0) {
                    Logger.logEvent("NetworkLogger", "Host now unreachable", new Logger.DataPair("host", host.getName()));
                }

                rtt.put(host, tripTime);
            }).startPeriodic(0.5);
        }

        new Notifier(() -> {
            fmsPresent = fmsPresent || DriverStation.getInstance().isFMSAttached();

            if (fmsPresent) {
                Logger.DataPair[] dataPairs = new Logger.DataPair[hosts.length];

                for (int i = 0; i < hosts.length; i++) {
                    Host host = hosts[i];
                    dataPairs[i] = new Logger.DataPair(host.getName(), rtt.getOrDefault(host, -1.0));
                }

                Logger.logEvent("NetworkLogger", "Network Report", dataPairs);
            }
        }).startPeriodic(1);
    }

    private static double ping(String host) {
        try {

            Process process = Runtime.getRuntime().exec("ping -4 -c 1 -q -w 1 " + host);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("round-trip")) {
                    return Double.parseDouble(line.split("/")[3]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static class Host {

        private String name;
        private String address;

        public Host(String name, String address) {
            this.name = name;
            this.address = address;
        }

        public String getName() {
            return this.name;
        }

        public String getAddress() {
            return this.address;
        }

    }

}