package cat.pebasi.fileorganizer.spies;

import java.nio.file.Path;

import cat.pebasi.fileorganizer.imaging.ImagingAdapter;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;

public class ImaagingAdapterSpy implements ImagingAdapter {
	public MethodCallRecorder MCR = new MethodCallRecorder();
	public MethodReturnValues MRV = new MethodReturnValues();

	public ImaagingAdapterSpy() {
		MCR.useMRV(MRV);
		MRV.setDefaultReturnValuesSupplier("getOriginalDateFromFile", () -> "someFormattedDate");
	}

	@Override
	public String getOriginalDateFromFile(Path path, String dateTimeFormat) {
		return (String) MCR.addCallAndReturnFromMRV("path", path, "dateTimeFormat", dateTimeFormat);
	}

}
