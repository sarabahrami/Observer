 /* 
  Copyright Software Engineering Research laboratory <serl@cs.wichita.edu>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Library General Public
 License as published by the Free Software Foundation; either
 version 2 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Library General Public License for more details.

 You should have received a copy of the GNU Library General Public
 License along with this program; if not, write to the Free
 Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 
 */
package wichita.edu.project.incrementalCorpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/*
 * @author: Sara Bahrami <mxbahramizanjani@wichita.edu>
 * @input: BugId+BugDescription and Corpus ( created by XmlParser Class)
 * @output: List of 10 relevant file for each bug
 * Calls :machinelearningwithoutlsi.py which is a Python file uses Gensim library for text matching and KNN
 */
public class Run {
	public static  void main(String[] args)throws IOException, InterruptedException{
	String RepositoryFolderPath="/media/extrav/Eclipse/Repository/PlatformProject/";
	String Path ="/media/extrav/Eclipse";
	//Benchmark : IssueId	BugDescription
	String	issuesinput="testbenchmark";
	//output file from XmlParser class
	String	Corpusfile="CorpusplusCopyFinal.txt";
	//Machine learning part
	String   pythonfile="machinelearningwithoutlsi.py";
	String   resultfilename;
	//this file generates by machinelearningwithoutlsi.py 
	String   Corpuswithoutidfile="Corpuswithoutid.txt";

		String strLine=null;
		BufferedReader reader1 = new BufferedReader(new FileReader(Path+"/"+issuesinput));
		while ((strLine = reader1.readLine())!= null) {
	           	//String[] strsplit= strLine.split("\t",2);
	           	String[] strsplit= strLine.split("\t",3);
	           	System.out.println(strsplit[2]);
	           	resultfilename=strsplit[0].replace("\n", "").replace("\r", "");
	           	System.out.println(resultfilename);
	           	String bugdesc=strsplit[1].replace("\"","");
	            // System.out.println(bugdesc);
	           	VersionDiff version=new VersionDiff(strsplit[2],Corpusfile,Path,RepositoryFolderPath);
	           //	System.out.println("version.getfilesMap().size()");
	           	//System.out.println(version.getfilesMap().size());
	           Srcml objsrcml=new Srcml(version);
	           Corpus corpus=new Corpus(Path,Corpusfile,version);
	           version.setfilesMap();
		//Pythonrun(bugdesc,Path,pythonfile,resultfilename,Corpusfile,Corpuswithoutidfile);

		}	
		}

	/*****************************************************************/
	public static void Pythonrun(String bugdesc,String Path,String pythonfile,String resultfilename,String Corpusfile,String Corpuswithoutidfile)
	{	File wd = new File("/usr/bin/");
     	Process proc = null;
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
    		String cmd = "cd" + " " + Path ;
    		out.println(cmd);
    		String cmd1 ="python"+" "+"\""+pythonfile+"\""+" "+"\""+bugdesc+"\""+" "+"\""+resultfilename+"\""+" "+"\""+Corpusfile+"\""+" "+"\""+Corpuswithoutidfile+"\"";
    		System.out.println(cmd1);
    		
    		out.println(cmd1);
    		
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
    			
    		} 	catch (Exception e) {
    			e.printStackTrace();
    }
}
	}
	
}
