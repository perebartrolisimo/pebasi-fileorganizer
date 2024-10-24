package cat.pebasi.fileorganizer.spies;

import java.io.File;

import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;

public class FileSpy extends File {

	public MethodCallRecorder MCR = new MethodCallRecorder();
	public MethodReturnValues MRV = new MethodReturnValues();

	public FileSpy() {
		super("somePathName");
		MCR.useMRV(MRV);
	}

}
