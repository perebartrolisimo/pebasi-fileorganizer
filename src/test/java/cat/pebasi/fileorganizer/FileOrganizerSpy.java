package cat.pebasi.fileorganizer;

import java.nio.file.Path;

import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;

public class FileOrganizerSpy implements FileOrganizer {

	public MethodCallRecorder MCR = new MethodCallRecorder();
	public MethodReturnValues MRV = new MethodReturnValues();

	public FileOrganizerSpy() {
		MCR.useMRV(MRV);
	}

	@Override
	public void organize(Path childPath) {
		MCR.addCall("childPath", childPath);
	}

}
