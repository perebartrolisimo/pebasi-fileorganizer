package cat.pebasi.fileorganizer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cat.pebasi.fileorganizer.spies.ImagingAdapterSpy;
import se.uu.ub.cora.logger.LoggerProvider;
import se.uu.ub.cora.logger.spies.LoggerFactorySpy;
import se.uu.ub.cora.logger.spies.LoggerSpy;

public class FileOrganizerCommandLineTest {

	private static final String BASEPATH = "./src/test/resources/";
	private static final String BASEPATH_ONLY_IMAGES = "./src/test/resources/onlyImages/";
	private static final String BASEPATH_ONLY_NON_IMAGES = "./src/test/resources/onlyNonImages/";
	private static final String FILENAME_FORMAT = "YYYYMMDDHHmmSS";
	private FileOrganizerCommandLine fileOrganizer;
	private String[] argsBasePath;
	private ImagingAdapterSpy imagingAdapter;
	private LoggerFactorySpy loggerFactorySpy;
	private String[] argsOnlyNonImagesPath;
	private String[] argsOnlyImagesPath;

	@BeforeMethod
	private void beforeMethod() {
		loggerFactorySpy = new LoggerFactorySpy();
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		fileOrganizer = new FileOrganizerCommandLine();
		imagingAdapter = new ImagingAdapterSpy();

		argsBasePath = new String[] { BASEPATH, FILENAME_FORMAT };
		argsOnlyImagesPath = new String[] { BASEPATH_ONLY_IMAGES, FILENAME_FORMAT };
		argsOnlyNonImagesPath = new String[] { BASEPATH_ONLY_NON_IMAGES, FILENAME_FORMAT };

	}

	@Test
	public void testLoggerInit() throws Exception {
		FileOrganizerCommandLine.setImagingAdapter(imagingAdapter);
		FileOrganizerCommandLine.main(argsBasePath);

		loggerFactorySpy.MCR.assertParameters("factorForClass", 0, FileOrganizerCommandLine.class);
		loggerFactorySpy.MCR.assertNumberOfCallsToMethod("factorForClass", 1);
	}

	@Test
	public void testCallsImagingAdapter() throws Exception {
		FileOrganizerCommandLine.setImagingAdapter(imagingAdapter);

		FileOrganizerCommandLine.main(argsOnlyImagesPath);
		List<String> imageFilesWhenCreated = fileOrganizer.getFilesToOrganize();

		imagingAdapter.MCR.assertNumberOfCallsToMethod("getOriginalDateFromFile", 5);

		assertParametersOnMethod("getOriginalDateFromFile", 0,
				Path.of(BASEPATH_ONLY_IMAGES, "archiveStorageDiagram.jpeg"), FILENAME_FORMAT);
		assertParametersOnMethod("getOriginalDateFromFile", 1,
				Path.of(BASEPATH_ONLY_IMAGES, "alvin-record_435677-ATTACHMENT-0001.png"),
				FILENAME_FORMAT);
		assertParametersOnMethod("getOriginalDateFromFile", 2,
				Path.of(BASEPATH_ONLY_IMAGES, "0210 200221 COLLIURE-049.JPG"), FILENAME_FORMAT);
		assertParametersOnMethod("getOriginalDateFromFile", 3,
				Path.of(BASEPATH_ONLY_IMAGES, "alvin-record_55381-ATTACHMENT-0001.tiff"),
				FILENAME_FORMAT);
		assertParametersOnMethod("getOriginalDateFromFile", 4,
				Path.of(BASEPATH_ONLY_IMAGES, "manga.jpg"), FILENAME_FORMAT);

		assertEquals(imageFilesWhenCreated.size(), 5);
	}

	@Test
	public void testSetCorrectFileNamesForEachImage() throws Exception {
		FileOrganizerCommandLine.setImagingAdapter(imagingAdapter);

		FileOrganizerCommandLine.main(argsOnlyImagesPath);
		List<String> filesToOrganize = fileOrganizer.getFilesToOrganize();

		assertTrue(filesToOrganize.get(0).matches("someFormattedDate.jpg"));
	}

	@Test
	public void testImagingAdapterThrowsExcpetion() throws Exception {
		imagingAdapter.MRV.setAlwaysThrowException("getOriginalDateFromFile",
				ImagingException.withMessage("error from spy"));
		FileOrganizerCommandLine.setImagingAdapter(imagingAdapter);

		FileOrganizerCommandLine.main(argsOnlyImagesPath);
		List<String> imageFilesWhenCreated = fileOrganizer.getFilesToOrganize();

		imagingAdapter.MCR.assertNumberOfCallsToMethod("getOriginalDateFromFile", 5);
		assertEquals(imageFilesWhenCreated.size(), 0);

		LoggerSpy logger = getLogger();
		logger.MCR.assertNumberOfCallsToMethod("logWarnUsingMessageAndException", 5);
		logger.MCR.assertParameters("logWarnUsingMessageAndException", 0,
				"The file " + Path.of(BASEPATH_ONLY_IMAGES, "archiveStorageDiagram.jpeg").toString()
						+ " cannot be organized due to problems while reading creation date.");
		logger.MCR.assertParameters("logWarnUsingMessageAndException", 1,
				"The file "
						+ Path.of(BASEPATH_ONLY_IMAGES, "alvin-record_435677-ATTACHMENT-0001.png")
								.toString()
						+ " cannot be organized due to problems while reading creation date.");
		logger.MCR.assertParameters("logWarnUsingMessageAndException", 2,
				"The file "
						+ Path.of(BASEPATH_ONLY_IMAGES, "0210 200221 COLLIURE-049.JPG").toString()
						+ " cannot be organized due to problems while reading creation date.");
		logger.MCR.assertParameters("logWarnUsingMessageAndException", 3,
				"The file "
						+ Path.of(BASEPATH_ONLY_IMAGES, "alvin-record_55381-ATTACHMENT-0001.tiff")
								.toString()
						+ " cannot be organized due to problems while reading creation date.");
		logger.MCR.assertParameters("logWarnUsingMessageAndException", 4,
				"The file " + Path.of(BASEPATH_ONLY_IMAGES, "manga.jpg").toString()
						+ " cannot be organized due to problems while reading creation date.");

	}

	@Test
	public void testAcceptOnlyImageFormat_JPG_otherwiseLogFilesWhichAreNotProcessed()
			throws Exception {
		FileOrganizerCommandLine.setImagingAdapter(imagingAdapter);

		FileOrganizerCommandLine.main(argsOnlyNonImagesPath);
		List<String> imageFilesWhenCreated = fileOrganizer.getFilesToOrganize();

		imagingAdapter.MCR.assertNumberOfCallsToMethod("getOriginalDateFromFile", 0);
		assertEquals(imageFilesWhenCreated.size(), 0);

		LoggerSpy logger = getLogger();
		logger.MCR.assertNumberOfCallsToMethod("logWarnUsingMessage", 2);
		logger.MCR.assertParameters("logWarnUsingMessage", 0,
				"The file " + Path.of(BASEPATH_ONLY_NON_IMAGES, "textFile.txt").toString()
						+ " is not an image and cannot be organized.");
		logger.MCR.assertParameters("logWarnUsingMessage", 1,
				"The file " + Path.of(BASEPATH_ONLY_NON_IMAGES, "testPDF.pdf").toString()
						+ " is not an image and cannot be organized.");

	}

	@Test
	public void testFindFilesInDeeperFolderStructure() throws Exception {
		FileOrganizerCommandLine.setImagingAdapter(imagingAdapter);

		FileOrganizerCommandLine.main(argsBasePath);
		fileOrganizer.getFilesToOrganize();

		imagingAdapter.MCR.assertNumberOfCallsToMethod("getOriginalDateFromFile", 5);

	}

	@Test
	public void testPathMightBeAFile() throws Exception {
		String[] argsToAnImage = new String[] { "./src/test/resources/onlyImages/manga.jpg",
				FILENAME_FORMAT };
		FileOrganizerCommandLine.setImagingAdapter(imagingAdapter);

		FileOrganizerCommandLine.main(argsToAnImage);
		fileOrganizer.getFilesToOrganize();

		imagingAdapter.MCR.assertNumberOfCallsToMethod("getOriginalDateFromFile", 1);
	}

	@Test
	public void testNotExistingPath() throws Exception {
		String[] notExistingPath = new String[] { "notExistingPath", FILENAME_FORMAT };
		FileOrganizerCommandLine.setImagingAdapter(imagingAdapter);

		FileOrganizerCommandLine.main(notExistingPath);
		fileOrganizer.getFilesToOrganize();

		LoggerSpy logger = getLogger();
		logger.MCR.assertNumberOfCallsToMethod("logErrorUsingMessage", 1);
		logger.MCR.assertParameters("logErrorUsingMessage", 0, "FileOrganizer failed due to:");
	}

	private LoggerSpy getLogger() {
		return (LoggerSpy) loggerFactorySpy.MCR.getReturnValue("factorForClass", 0);
	}

	private void assertParametersOnMethod(String methodName, int callNumber, Path path,
			String format) {
		imagingAdapter.MCR.assertParameterAsEqual(methodName, callNumber, "path", path);
		imagingAdapter.MCR.assertParameter(methodName, callNumber, "dateTimeFormat", format);
	}

}
