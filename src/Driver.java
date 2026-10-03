import java.io.File;

/* Run the application using one of these options:
 * 
 * 1. Use the run scripts provided:
 *    - On macOS/Linux: ./run.sh
 *    - On Windows: run.bat
 * 
 * 2. Compile and run manually with the MySQL connector jar in the lib folder:
 *    - On macOS/Linux:
 *      javac -cp "../lib/mysql-connector-j-9.3.0.jar:." *.java
 *      java -cp "lib/mysql-connector-j-9.3.0.jar:src" Driver
 *    - On Windows:
 *      javac -cp "..\lib\mysql-connector-j-9.3.0.jar;." *.java
 *      java -cp "lib\mysql-connector-j-9.3.0.jar;src" Driver
 */
public class Driver { 
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            new ClothingStoreApp();
        } catch (ClassNotFoundException e) {
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            String scriptName = isWindows ? "run.bat" : "run.sh";
            
            System.out.println("\nAttempting to run " + scriptName + " automatically...\n");
            
            try {
                File script = new File(scriptName);
                if (!script.exists()) {
                    System.out.println("Error: " + scriptName + " not found!");
                    return;
                }
                if (!isWindows) {
                    new ProcessBuilder("chmod", "+x", scriptName).inheritIO().start().waitFor();
                }
                String command = isWindows ? scriptName : "." + File.separator + scriptName;
                new ProcessBuilder(command).inheritIO().start();
                
                System.exit(0);
            } catch (Exception ex) {
                System.out.println("Failed to run script automatically. Please run manually:\n");
                System.out.println(isWindows ? "run.bat" : "./run.sh");
            }
        }
    }
}
