package cat.pebasi.fileorganizer;

import static org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;

public class ImagingAdapterImp implements ImagingAdapter {

	@Override
	public String getOriginalDateFromFile(File file, String dateTimeFormat) {
		if (!file.exists()) {
			throw new RuntimeException("Non existing file");
		}
		try {
			ImageMetadata metadata = Imaging.getMetadata(file);
			if (metadata instanceof JpegImageMetadata) {
				String dateTimeOriginalValue = getOriginalDateFromImageMetadata(metadata);
				return convertOriginalDateToSpecificFormat(dateTimeOriginalValue, dateTimeFormat);
			}
		} catch (ImagingException e) {
			throw new RuntimeException("Wrong media type:" + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Something wrong: " + e.getClass() + " :" + e.getMessage());
		}
		throw new RuntimeException("TODO");
	}

	private String getOriginalDateFromImageMetadata(ImageMetadata metadata) {
		final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
		TiffField dateTimeOriginal = jpegMetadata
				.findExifValueWithExactMatch(EXIF_TAG_DATE_TIME_ORIGINAL);
		return dateTimeOriginal.getValueDescription();
	}

	private String convertOriginalDateToSpecificFormat(String dateTimeOriginalValue,
			String expectedDateTimeFormat) throws ParseException {
		// String timeFormat = "yyyy:MM:dd HH:mm:ss";
		// DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
		// DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
		String dateTimeOriginalTrimmed = dateTimeOriginalValue.replace("'", "");
		System.err.println(dateTimeOriginalValue);
		// LocalDate imageDateAsLocalDate = LocalDate.parse(dateTimeOriginalTrimmed, timeFormatter);
		// DateTimeFormatter expecedFormatter = DateTimeFormatter.ofPattern(expectedDateTimeFormat);
		// return expecedFormatter.format(imageDateAsLocalDate);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		SimpleDateFormat expectedformatter = new SimpleDateFormat(expectedDateTimeFormat);
		Date date = formatter.parse(dateTimeOriginalTrimmed);

		return expectedformatter.format(date);
	}

	// print out various interesting EXIF tags.
	// printTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_XRESOLUTION);
	// printTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_DATE_TIME);
	// printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
	// printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
	// printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_ISO);
	// printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_SHUTTER_SPEED_VALUE);
	// printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
	// printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_BRIGHTNESS_VALUE);
	// printTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
	// printTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE);
	// printTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
	// printTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE);

	// private static void printTagValue(final JpegImageMetadata jpegMetadata, final TagInfo
	// tagInfo) {
	// final TiffField field = jpegMetadata.findExifValueWithExactMatch(tagInfo);
	// if (field == null) {
	// System.out.println(tagInfo.name + ": " + "Not Found.");
	// } else {
	// System.out.println(tagInfo.name + ": " + field.getValueDescription());
	// }
	// }

}
