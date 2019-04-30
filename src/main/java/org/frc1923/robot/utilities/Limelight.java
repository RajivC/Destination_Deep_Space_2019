package org.frc1923.robot.utilities;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import org.frc1923.robot.utilities.notifier.NamedNotifier;

public class Limelight {

    private String location;
    private Pipeline pipeline;

    private NetworkTable table;

    public Limelight(String location) {
        this.location = "limelight-" + location;
        this.pipeline = Pipeline.DRIVING;

        this.table = NetworkTableInstance.getDefault().getTable(this.location);

        new NamedNotifier(() -> {
            this.table.getEntry("camMode").setNumber(0);
            this.table.getEntry("ledMode").setNumber(0);
            this.table.getEntry("pipeline").setNumber(this.pipeline.getId());
        }, "Limelight." + location, 0.1).start();
    }

    public void setEnableTargeting(boolean enableTargeting) {
        this.pipeline = enableTargeting ? Pipeline.TARGETING : Pipeline.DRIVING;
        this.table.getEntry("pipeline").setNumber(this.pipeline.getId());
    }

    public double getTargetX() {
        return this.table.getEntry("tx").getDouble(0);
    }

    public double getTargetArea() {
        return this.table.getEntry("ta").getDouble(0);
    }

    public String getLocation() {
        return this.location;
    }

    public boolean isTargetValid() {
        return this.table.getEntry("tv").getNumber(0).intValue() == 1;
    }

    public enum Pipeline {

        TARGETING(0),
        DRIVING(1);

        private int id;

        Pipeline(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

    }

}
