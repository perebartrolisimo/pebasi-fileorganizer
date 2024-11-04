package cat.pebasi.fileorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import cat.pebasi.fileorganizer.imaging.ImagingAdapter;
import se.uu.ub.cora.logger.Logger;
import se.uu.ub.cora.logger.LoggerProvider;

public class FileOrganizer {

	private static ImagingAdapter imagingAdapter;
	private static String filenameFormat;
	private static Path basePath;
	private static List<String> filesToOrganize;
	private static Logger logger;

	public FileOrganizer() {
		logger = LoggerProvider.getLoggerForClass(FileOrganizer.class);
	}

	public static void main(String[] args) {

		String basePathArg = args[0];
		basePath = Path.of(basePathArg);
		filenameFormat = args[1];

		filesToOrganize = new ArrayList<>();

		// TODO:
		// Accept tiff, jpg, jpeg, png, raw
		// If original file date not present use create date instead.
		// Modes: dry(show target path) copy(copies files and keeps original) move(moves the files
		// to target)
		try {
			handleFolderOrFile(basePath);
		} catch (Exception e) {
			logger.logErrorUsingMessage("FileOrganizer failed due to: " + e.getMessage());
		}
	}

	private static void recurseOnFolders(Path parentPath) {
		Stream<Path> fileList = getFilesList(parentPath);
		fileList.forEach(childPath -> {
			handleFolderOrFile(childPath);
		});
	}

	private static void handleFolderOrFile(Path childPath) {
		if (Files.isDirectory(childPath)) {
			recurseOnFolders(childPath);

		}
		handleFile(childPath);
	}

	private static Stream<Path> getFilesList(Path parentPath) {
		try {
			return Files.list(parentPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error while reading files from path:" + parentPath
					+ " due to: " + e.getCause());
		}
	}

	private static void handleFile(Path childPath) {
		if (Files.isRegularFile(childPath)) {
			String fileExtension = getFileExtension(childPath);
			if (isItAnImage(fileExtension)) {
				handleImages(childPath);
			} else {
				writeToLog(childPath);
			}
		}
	}

	private static void writeToLog(Path originalPathAndFile) {
		logger.logWarnUsingMessage(
				"The file " + originalPathAndFile + " is not an image and cannot be organized.");
	}

	private static void handleImages(Path originalPathAndFile) {
		try {
			String originalDateFromFile = imagingAdapter
					.getOriginalDateFromFile(originalPathAndFile, filenameFormat);
			filesToOrganize.add(originalDateFromFile + ".jpg");
		} catch (ImagingException e) {
			logger.logWarnUsingMessageAndException(
					"The file " + originalPathAndFile
							+ " cannot be organized due to problems while reading creation date.",
					e);
		}
	}

	private static boolean isItAnImage(String extension) {
		Set<String> allowedExtensions = Set.of("jpg", "jpeg", "tiff", "png");
		return allowedExtensions.contains(extension);
	}

	private static String getFileExtension(Path originalPathAndFile) {
		String originalPathAsString = originalPathAndFile.toString();
		int indexOfExtension = originalPathAsString.lastIndexOf(".");
		String extension = originalPathAsString.substring(indexOfExtension + 1,
				originalPathAsString.length());
		return extension.toLowerCase();
	}

	public List<String> getFilesToOrganize() {
		return filesToOrganize;
	}

	public static void setImagingAdapter(ImagingAdapter adapter) {
		imagingAdapter = adapter;
	}

}
