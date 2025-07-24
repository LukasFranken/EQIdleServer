package de.instinct.base.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManager {
    
    private static Path getAppDirectoryPath() {
        return Paths.get("").toAbsolutePath();
    }
    
    public static String loadFile(String fileName) {
        try {
            Path filePath = getAppDirectoryPath().resolve(fileName);
            if (!Files.exists(filePath)) {
                System.err.println("File not found: " + filePath);
                return null;
            }
            return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void saveFile(String fileName, String content) {
        try {
            Path filePath = getAppDirectoryPath().resolve(fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), 
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
