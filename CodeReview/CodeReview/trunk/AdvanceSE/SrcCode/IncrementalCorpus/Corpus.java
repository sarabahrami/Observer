package wichita.edu.project.incrementalCorpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wichita.edu.revisionexperiment.XmlParser;

public class Corpus implements Observer{
	
	private String Path;
	private String FileName;
	private HashMap<String,ArrayList<String>> Corpus;
	private HashMap<String,String> files;
	private StringBuilder content;
	private Subject VersionDiff;
	
	public Corpus(String path,String filename,Subject VersionDiff) throws IOException
	{
		this.VersionDiff=VersionDiff;
		VersionDiff.registerObserver(this);
		Path=path;
		FileName=filename;
		Corpus=new HashMap<String,ArrayList<String>>();
		XmlParser obj=new XmlParser();
		Corpus=obj.ReturnCorpus();
		files=new HashMap<String,String>();
		//files=FilesMap;
		content=new StringBuilder();
		//System.out.println("UpdateCorpus");
		
	}
	public void update( HashMap<String,String> filesMap) throws IOException
	{
		this.files=filesMap;
		ChangeCorpus();
	}
	
	
	private void ChangeCorpus() throws IOException
	{
		
		File Corpusold=new File(Path+"/"+FileName);
		//System.out.println(FileName);
		
		String str="null";
		String name;
		BufferedReader reader =new BufferedReader(new FileReader(Corpusold));
		Pattern pattern=Pattern.compile("[a-zA-Z_0-9]*(Test)[a-zA-Z_0-9]*.java");
		while((str=reader.readLine())!=null)
		{
			str=str.replace("\n", "");
			name=str.split("\t")[0];

				if(files.containsKey(name))
				{
					//System.out.println(name);
					Matcher matcher = pattern.matcher(name);
					if ((files.get(name).equalsIgnoreCase("M"))&&(!(matcher.find())))
					{
						//System.out.println(name);
						//content=content.append(name+"\t"+MatchCorpus(name)+"\n");
						//content=content.append(str+" "+MatchCorpus(name)+"\n");
						MatchCorpus(str);
					}
				}
				
				else if(!(files.containsKey(name)))
				{
					
					content=content.append(str+"\n");
				}

		}
		
		
		for (Entry<String,String> entry:files.entrySet())
		{
			Matcher matcher = pattern.matcher(entry.getKey());
			if ((entry.getValue().equalsIgnoreCase("A"))&&(!(matcher.find())))
			{
				//System.out.println(entry.getKey());
				AddtoCorpus(entry.getKey());
				
			}
		}
		reader.close();
		File Corpusnew=new File(Path+"/"+FileName);
		BufferedWriter writer=new BufferedWriter(new FileWriter(Corpusnew));
		writer.write(content.toString());
		writer.flush();
		writer.close();
		
	}
	
	/*
	 * This function for all of the files which are added to this revision, retrieves the textual information
	 * from source code and review comments. This information then will be added to the corpus.  
	 */
	private void AddtoCorpus(String name) throws IOException
	{
		ArrayList<String>temp=new ArrayList<String>();
		BufferedReader reader1=new BufferedReader(new FileReader("/media/extrav/Eclipse/fileGeneralcomment.txt"));
		BufferedReader reader2=new BufferedReader(new FileReader("/media/extrav/Eclipse/filecommentinline.txt"));
		StringBuilder line=new StringBuilder();
		for (Entry <String,ArrayList<String>> entry:Corpus.entrySet())
		{
			if(entry.getKey().equalsIgnoreCase(name))
			{
				line=line.append(name+"\t");
				for(String x:entry.getValue())
				{
					x=x.replace("\n", "").replace("\r", "");
					line=line.append(x+" ");
				}
				temp=RetrunDatafromFile(name,reader1);
				if(temp.size()>0)
				{
					for(String value:temp)
					{
						value=value.replace("\n", "").replace("\r", "");
						line=line.append(value+" ");
					}
					
				}
				temp=RetrunDatafromFile(name,reader2);
				if(temp.size()>0)
				{
					for(String value:temp)
					{
						value=value.replace("\n", "").replace("\r", "");
						//content=content.append(value+" ");
						line=line.append(value+" ");
					}
					
				}
				
			}
		}

		if (line.length()>0)
		{
			//System.out.println("addcorpus");
			//System.out.println(line.toString());
			content=content.append(line.toString());
			content=content.append("\n");
			
		}
		//content=content.append("\n");
		
	}
	
	/*
	 * this function for those files which have been modified in this revision of source code adds all the 
	 * information from source code to the exist corpus.
	 */
	private void MatchCorpus(String str)
	{
		String name=str.split("\t")[0];
		String data=str.split("\t")[1];
		for (Entry <String,ArrayList<String>> entry:Corpus.entrySet())
		{
			if(entry.getKey().equalsIgnoreCase(name))
			{
				content=content.append(name+"\t"+data);
				
				for(String x:entry.getValue())
				{
					x=x.replace("\n", "").replace("\r", "");
					if(!data.contains(x))
					{
						content=content.append(" ");
						content=content.append(x);
					}
				}
			}
		}
		content.append("\n");
	}
	
	
	/*
	 * for each file read the data from file.
	 */
	public static ArrayList<String> RetrunDatafromFile(String Name,BufferedReader reader) throws IOException
	{
		
		ArrayList<String>templist=new ArrayList<String>();
		String strline="null";
		while((strline=reader.readLine())!=null)
		{
			String strsplit[]=strline.split("\t",2);
			String FileName=strsplit[0];
			
			if (Name.endsWith(FileName))
			{

		 		String temp=strsplit[1].trim().replaceAll(" +", " ");
		 		temp=temp.replaceAll("\t"," ");
				templist.add(temp);
			}

		}
		
		return templist;
	}
	
	public HashMap<String,String> getfilesMap()
	{
		return files;
	}
}
