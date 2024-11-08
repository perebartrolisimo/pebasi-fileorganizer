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

public class FileOrganizerImp implements FileOrganizer {

	public static FileOrganizerImp usingImagingAdapter(ImagingAdapter imagingAdapter,
			String filenameFormat) {
		return new FileOrganizerImp(imagingAdapter, filenameFormat);
	}

	private String filenameFormat;
	private Logger logger;
	private ImagingAdapter imagingAdapter;
	private List<String> filesToOrganize = new ArrayList<>();

	private FileOrganizerImp(ImagingAdapter imagingAdapter, String filenameFormat) {
		this.imagingAdapter = imagingAdapter;
		this.filenameFormat = filenameFormat;
		logger = LoggerProvider.getLoggerForClass(FileOrganizerImp.class);
	}

	@Override
	public void organize(Path childPath) {
		try {
			handleDirectoryOrFile(childPath);
		} catch (Exception e) {
			logger.logErrorUsingMessage("FileOrganizer failed due to: " + e.getMessage());
		}
	}

	private void handleDirectoryOrFile(Path childPath) {
		if (Files.isDirectory(childPath)) {
			recurseOnFolders(childPath);

		}
		handleFile(childPath);
	}

	private void recurseOnFolders(Path parentPath) {
		Stream<Path> fileList = getFilesList(parentPath);
		fileList.forEach(childPath -> {
			organize(childPath);
		});
	}

	private Stream<Path> getFilesList(Path parentPath) {
		try {
			return Files.list(parentPath);
		} catch (IOException e) {
			throw new RuntimeException("Error while reading files from path:" + parentPath
					+ " due to: " + e.getCause());
		}
	}

	private void handleFile(Path childPath) {
		if (Files.isRegularFile(childPath)) {
			String fileExtension = getFileExtension(childPath);
			if (isItAnImage(fileExtension)) {
				handleImages(childPath);
			} else {
				writeToLog(childPath);
			}
		}
	}

	private void writeToLog(Path originalPathAndFile) {
		logger.logWarnUsingMessage(
				"The file " + originalPathAndFile + " is not an image and cannot be organized.");
	}

	private void handleImages(Path originalPathAndFile) {
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

	private boolean isItAnImage(String extension) {
		Set<String> allowedExtensions = Set.of("jpg", "jpeg", "tiff", "png");
		return allowedExtensions.contains(extension);
	}

	private String getFileExtension(Path originalPathAndFile) {
		String originalPathAsString = originalPathAndFile.toString();
		int indexOfExtension = originalPathAsString.lastIndexOf(".");
		String extension = originalPathAsString.substring(indexOfExtension + 1,
				originalPathAsString.length());
		return extension.toLowerCase();
	}

	public List<String> onlyForTestGetFilesToOrganize() {
		return filesToOrganize;
	}

	public Object onlyForTestGetImagingAdapter() {
		return imagingAdapter;
	}

	public String onlyForTestGetFileformat() {
		return filenameFormat;
	}

}
