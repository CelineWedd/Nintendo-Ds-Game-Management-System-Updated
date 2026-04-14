package ndsgms;
/**
 * @author Celine Wedderburn
 * CEN-3024C - Software Development 1
 * @since Mar 27, 2026
 * ndsgmsLauncher.java
 *
 * Launcher class for the Nintendo DS Game Management System in replacement of GUI class.
 */

public class ndsgmsLauncher {
    /**
     * this is where the application starts
     * Calls the GUI class to launch JavaFX
     *
     * @param args a list of arguments passed to the system/program
     */
    public static void main(String[] args) {
        ndsgms.GUI.main(args);
    }
}
