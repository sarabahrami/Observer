package wichita.edu.project.incrementalCorpus;

import java.io.IOException;

public interface Subject {
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObserver() throws IOException;

}
