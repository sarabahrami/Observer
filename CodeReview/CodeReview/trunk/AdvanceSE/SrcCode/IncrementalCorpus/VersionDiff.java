package wichita.edu.project.incrementalCorpus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import wichita.edu.revisionexperiment.CheckoutRevision;
import wichita.edu.revisionexperiment.GitDiff;
import wichita.edu.revisionexperiment.ReturnRevision;
import wichita.edu.revisionexperiment.Srcml;
import wichita.edu.revisionexperiment.UpdatingCorpus;
public class VersionDiff implements Subject
{
	private ArrayList observers;
	private HashMap<String,String> filesMap;
	private ArrayList<String> files;
	private HashMap<String, String> VersionMapold;
	private HashMap<String, String> VersionMapnew;
	private String date;
	private String filename;
	private String path;
	private String RepositoryFolderPath;
	public VersionDiff(String Date, String name, String dirpath, String RepositoryFolderPath) throws IOException, InterruptedException
	{
		observers=new ArrayList();
		filesMap=new HashMap<String,String>(); 
		files=new ArrayList<String>();
		VersionMapold=new HashMap<String, String>();
		VersionMapnew=new HashMap<String, String>();
		date=Date;
		filename=name;
		path=dirpath;
		this.RepositoryFolderPath=RepositoryFolderPath;
		run();
	}	
	
	public void registerObserver(Observer o)
	{
		observers.add(o);
	}
	
	public void removeObserver(Observer o)
	{
		int i=observers.indexOf(o);
		if(i>=0)
		{
			observers.remove(i);
		}
	}
	public void notifyObserver() throws IOException
	{
		for(int i=0;i<observers.size();i++)
			try {
				{
					Observer observer=(Observer)observers.get(i);
					observer.update(filesMap);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private void run() throws IOException, InterruptedException
	{
		ReturnRevision objold=new ReturnRevision( RepositoryFolderPath);
		VersionMapold=objold.returnRevision();
		CheckoutRevision objnew=new CheckoutRevision(date,RepositoryFolderPath);
		VersionMapnew=objnew.returnRevision();
		
		for(Entry<String,String> entry:VersionMapnew.entrySet())
		{
			for(Entry<String,String> entryold:VersionMapold.entrySet())
			{
				if (entry.getKey().equalsIgnoreCase(entryold.getKey()))
				{
					//System.out.println(entry.getKey()+"\t"+entry.getValue()+"\t"+entryold.getValue());
					GitDiff obj=new GitDiff(entryold.getValue(),entry.getValue(),entry.getKey(),RepositoryFolderPath);
					for(Entry<String,String> entry1: obj.returnfiles().entrySet())
					{
						if(entry1.getKey().endsWith(".java"))
						{
							filesMap.put(entry1.getKey(),entry1.getValue());
						}
						
					}
					
				}
			}
			
		}
		
		
		
		
	}
	
	public void setfilesMap() throws IOException
	{
		if(filesMap.size()>0)
		{
			notifyObserver();
		}
	}
	
	public HashMap<String,String> getfilesMap()
	{
		return filesMap;
	}
}
