package betterConverter.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConvertUtils {

    public static File convertM4AToMP3(Path inputFilePath) {

        // Create outputFilePath
        String outputFilePath = inputFilePath.toString().replace(".m4a", ".mp3");

        try {
            String command = String.format("ffmpeg -i %s -acodec libmp3lame -aq 4 -y %s", inputFilePath, outputFilePath);

            Process process = Runtime.getRuntime().exec(command);

            // Read the output to check for any errors
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete
            File mp3File = null;
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Conversion successful");
                mp3File = new File(outputFilePath);
            } else {
                System.err.println("Conversion failed");
            }

            cleanup(inputFilePath);

            return mp3File;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void cleanup(Path path) {
        try {
            Files.deleteIfExists(path);
        }
        catch (Exception ignored) {}
    }
}
