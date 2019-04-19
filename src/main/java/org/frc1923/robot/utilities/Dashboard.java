package org.frc1923.robot.utilities;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Dashboard {

    private static NetworkTable dashboard = NetworkTableInstance.getDefault().getTable("SmartDashboard");

    public static void putNumber(String key, double value) {
        dashboard.getEntry(key).setDouble(value);
    }

    public static void putBoolean(String key, boolean value) {
        dashboard.getEntry(key).setBoolean(value);
    }

}
