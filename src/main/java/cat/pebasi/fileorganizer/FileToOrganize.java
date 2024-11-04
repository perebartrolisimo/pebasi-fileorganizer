package cat.pebasi.fileorganizer;

import java.nio.file.Path;

public record FileToOrganize(Path originalPath, Path targetPath) {
}
