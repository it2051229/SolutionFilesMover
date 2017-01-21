
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;

public class SolutionFilesMover {

    // Zips demo folder and moves all images to the destination
    public static void main(String[] args) throws Exception {
        // Ask for the destination directory
        String xmlFolder = JOptionPane.showInputDialog(null, "Destination XML Folder:");

        if (xmlFolder == null) {
            return;
        }
        
        // Zip the "demo" folder and delete it afterwards
        File currentDirectory = new File("./demo");

        if (currentDirectory.isDirectory() && currentDirectory.exists()) {
            File[] files = currentDirectory.listFiles();

            if (files.length == 0) {
                // Stop if there are no files to zip
                return;
            }

            Path p = Files.createFile(Paths.get("./demo.zip"));
            try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
                Path pp = Paths.get("./demo");
                Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                    ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                    try {
                        zs.putNextEntry(zipEntry);
                        zs.write(Files.readAllBytes(path));
                        zs.closeEntry();
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                });
            }

            // Delete the contents of the demo folder afterwards
            for (File f : files) {
                if (!f.isDirectory()) {
                    f.delete();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "demo folder missing.");
            return;
        }

        // Move the files to WAMP server
        try {
            currentDirectory = new File("C:\\wamp64\\www\\data_solutions\\" + xmlFolder);

            // Transfer the images and demo files to the directory
            for (File f : new File("./").listFiles()) {
                if (f.isFile()) {
                    String filename = f.getName().toLowerCase();

                    if (f.getName().endsWith(".png") || f.getName().endsWith(".zip")) {
                        if (f.renameTo(new File(currentDirectory.getAbsolutePath() + "\\" + filename))) {
                            // Delete file if successfully copied
                            f.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
