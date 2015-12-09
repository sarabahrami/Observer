package wichita.edu.project.incrementalCorpus;

import java.io.IOException;
import java.util.HashMap;

public interface Observer {
	public void update( HashMap<String,String> filesMap) throws IOException;

}
