package cat.pebasi.fileorganizer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class ImagingExceptionTest {
	@Test
	public void testWithMessage() {
		String message = "message";
		ImagingException exception = ImagingException.withMessage(message);
		assertEquals(exception.getMessage(), "message");
		assertTrue(exception instanceof RuntimeException);
	}

	@Test
	public void testWithMessageAndError() throws Exception {
		String message = "message";
		Exception exception = new RuntimeException();
		ImagingException storageException = ImagingException
				.withMessageAndException(message, exception);
		assertEquals(storageException.getMessage(), "message");
		assertEquals(storageException.getCause(), exception);
	}
}
