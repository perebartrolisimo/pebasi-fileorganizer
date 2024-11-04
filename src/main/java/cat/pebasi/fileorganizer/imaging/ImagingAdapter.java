package cat.pebasi.fileorganizer.imaging;

import java.nio.file.Path;

public interface ImagingAdapter {

	String getOriginalDateFromFile(Path path, String dateTimeFormat);

}