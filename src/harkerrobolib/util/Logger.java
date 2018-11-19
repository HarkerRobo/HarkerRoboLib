package harkerrobolib.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * 
 * @author Finn Frankis
 * @version Nov 18, 2018
 */
public class Logger {
    private static String defaultLoc = "/home/lvuser/logs/";
    private String fileLoc;
    private PrintWriter logger;

    public Logger () {
        this.fileLoc = defaultLoc;
    }

    public Logger (String fileLoc) {
        this.fileLoc = fileLoc;
    }

    public void start () {
        String fileName = DriverStation.getInstance().getEventName() + DriverStation.getInstance().getMatchType() + "-" 
                + DriverStation.getInstance().getAlliance() + DriverStation.getInstance().getLocation() + "-"
                + DriverStation.getInstance().getMatchNumber() + ".txt";
        try {
            logger = new PrintWriter(fileLoc + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void log (String val) {
        String message = (DriverStation.getInstance().isAutonomous() ? "A" : "T") + DriverStation.getInstance().getMatchTime() + " " + 
                DriverStation.getInstance().getBatteryVoltage() + "V " + val;
        if (logger != null) {
            logger.println(message);
        } else {
            System.out.println(message);
        }
    }
}
