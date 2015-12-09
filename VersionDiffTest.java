package wichita.edu.project.incrementalCorpus;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class VersionDiffTest {

	@Test
	public void test() throws IOException, InterruptedException {
		VersionDiff version=new VersionDiff("2015-03-06 15:38:31","CorpusTest.txt","/media/extrav/Eclipse","/media/extrav/Eclipse/Repository/PlatformProject/");
		assertEquals(1644,version.getfilesMap().size());
        Srcml objsrcml=new Srcml(version);
        Corpus corpus=new Corpus("/media/extrav/Eclipse","CorpusTest.txt",version);
        version.setfilesMap();
        assertEquals(1644,objsrcml.getfilesMap().size());
        assertEquals(1644,corpus.getfilesMap().size());
	
	}

}
