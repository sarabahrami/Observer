package wichita.edu.project.incrementalCorpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Srcml implements Observer {
    public   File wd = new File("/usr/bin/");
	public   String sourceCodeDirPath ="/media/extrav/Eclipse/Repository/PlatformProject2";
	public   String outputDirPath = "/media/extrav/Eclipse/Repository/srcml";
    public ArrayList<String> FileNames;
    private HashMap<String,String> filesMap;
    private Subject VersionDiff;
    public Srcml(Subject VersionDiff) 
    {
    	this.VersionDiff=VersionDiff;
		VersionDiff.registerObserver(this);
    }
    
	public void update( HashMap<String,String> filesMap) throws IOException
	{
		this.filesMap=filesMap;
    	FileNames=new ArrayList<String>();
        File folder = new File(sourceCodeDirPath);
        ConvertMaptoList(); 
		cleanDirectory();
        listFilesForFolder(folder);
	}
	
	private void ConvertMaptoList() 
	{
		for(Entry<String,String> entry1: filesMap.entrySet())
		{
			FileNames.add(entry1.getKey());
			
		}
	}
	
	private void cleanDirectory()
    {
    	File folder = new File(outputDirPath);
    	for(File entry:folder.listFiles())
    	{
    		entry.delete();
    	}
    }
    
    /*
     * walking trough source code repository
     */
    public  void listFilesForFolder(final File folder) 
    {
    	Pattern pattern=Pattern.compile("[a-zA-Z_0-9]*(Test)[a-zA-Z_0-9]*");
    	for (final File fileEntry : folder.listFiles()) 
    	{
          if (fileEntry.isDirectory()) 
          {
            listFilesForFolder(fileEntry);
          } 
          else 
          {
            if (fileEntry.isFile())
            {	
            	 
            	Matcher matcher = pattern.matcher(fileEntry.getName());
            	for(String name:FileNames)
            	{
            		if((fileEntry.getAbsolutePath().endsWith(name))&&(!(matcher.find())))
            		{
            			
            			converttoSrcXml(fileEntry.getAbsolutePath());
            			System.out.println(fileEntry.getAbsolutePath());
            		}
            	}
            	
            	
            }
          	}
    }
    }
     /*
      * running src2srcml command for each file and save the XML out put in directory    
      */
     private  void converttoSrcXml(String srcPath) {
    	
    	 srcPath=srcPath.replace(" ","\\ ");
    	 Process proc = null;
        String outPutXmlFilePath = "";
        try {
            proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (proc != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(proc.getOutputStream())), true);
            File srcFile = new File(srcPath);
            String name=Pathcreator(srcFile.getAbsolutePath());
            //String name=srcFile.getName();
           // System.out.println(name);
            outputDirPath=outputDirPath.replace(" ","\\ ");
            outPutXmlFilePath = outputDirPath+"/" +name;

            String cmd = "src2srcml" + " " + srcPath + " " + "-o " + outPutXmlFilePath;
            System.out.println(cmd);
            out.println(cmd);
            out.println("exit");
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                proc.waitFor();
                in.close();
                out.close();
                proc.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 
       
    }
    
    /*
     * Returning the absolute path of file as name string with - instead of /  and removing all the path before "org" 
     * from the string name.
     */
    private  String Pathcreator(String path)
    {
    	int startIndex1=path.indexOf("org");
		path=path.substring(startIndex1);
		path=path.replace('/','-' );
		return path;
		
    }
    
	public HashMap<String,String> getfilesMap()
	{
		return filesMap;
	}

}
