package cat.pebasi.fileorganizer;

import static org.testng.Assert.assertEquals;

import java.nio.file.Path;

import org.testng.annotations.Test;

public class FileToOrganizeTest {

	@Test
	public void testRecord() throws Exception {
		FileToOrganize fileToOrganize = new FileToOrganize(Path.of("someDir", "someOriginalFile"),
				Path.of("someDir", "someTargetFile"));

		assertEquals(fileToOrganize.originalPath().toString(), "someDir/someOriginalFile");
		assertEquals(fileToOrganize.targetPath().toString(), "someDir/someTargetFile");
	}

}
