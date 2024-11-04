package cat.pebasi.fileorganizer;

public class ImagingException extends RuntimeException {

	private static final long serialVersionUID = -255261285196817577L;

	private ImagingException(String message) {
		super(message);
	}

	private ImagingException(String message, Exception e) {
		super(message, e);
	}

	public static ImagingException withMessage(String message) {
		return new ImagingException(message);
	}

	public static ImagingException withMessageAndException(String message, Exception e) {
		return new ImagingException(message, e);
	}

}
