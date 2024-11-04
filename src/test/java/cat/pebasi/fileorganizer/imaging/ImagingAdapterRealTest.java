package cat.pebasi.fileorganizer.imaging;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.util.UUID;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cat.pebasi.fileorganizer.imaging.ImagingAdapter;
import cat.pebasi.fileorganizer.imaging.internal.ImagingAdapterImp;

public class ImagingAdapterRealTest {

	private static final String TEST_RESOURCE_PATH = "./src/test/resources/";
	private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

	private ImagingAdapter imagingAdapter;

	@BeforeMethod
	private void beforeMethod() {
		imagingAdapter = new ImagingAdapterImp();
		// Imaging imagingSpy = new ImagingSpy();
	}

	@Test
	public void callGetOriginalDate_JPEG() throws Exception {
		File realFile = new File(TEST_RESOURCE_PATH + "manga.jpg");
		String originalDate = imagingAdapter.getOriginalDateFromFile(realFile, DATE_TIME_FORMAT);

		assertEquals(originalDate.toString(), "20230926142206");

	}

	@Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Wrong "
			+ "media type: Unknown extension: tikaVideo.mp4")
	public void callGetOriginalDate_MP4() throws Exception {
		File realFile = new File(TEST_RESOURCE_PATH + "tikaVideo.mp4");

		imagingAdapter.getOriginalDateFromFile(realFile, DATE_TIME_FORMAT);
	}

	@Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Non existing file")
	public void nonExistingFile() throws Exception {
		File realFile = new File(TEST_RESOURCE_PATH + "NonExisting");

		imagingAdapter.getOriginalDateFromFile(realFile, DATE_TIME_FORMAT);
	}

	@Test
	public void testUUID() throws Exception {
		System.out.println(UUID.randomUUID());
	}
}
