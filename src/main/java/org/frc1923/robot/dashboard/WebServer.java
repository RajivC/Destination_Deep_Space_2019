package org.frc1923.robot.dashboard;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import org.frc1923.robot.commands.VisionToggleCommand;
import spark.Spark;

import java.io.BufferedReader;
import java.io.FileReader;

import static spark.Spark.*;

public class WebServer implements Runnable {

    @Override
    public void run() {
        port(5801);

        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(Filesystem.getDeployDirectory() + "/web/index.html"));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Spark.get("/", (a, b) -> sb.toString());
        Spark.get("/stream_url", (a, b) -> VisionToggleCommand.getInstance().getStreamUrl());
        Spark.get("/match_time", (a, b) -> ((int) (DriverStation.getInstance().getMatchTime() * 10)) / 10D);
    }

}
