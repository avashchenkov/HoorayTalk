import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

class UniqueFileNameGenerator {
    public Path generateUniqueFilePath(String originalFileName, String pathToStorage) {
        String fileExtension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            fileExtension = originalFileName.substring(i);
        }
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        return Paths.get(pathToStorage, uniqueFileName);
    }
}