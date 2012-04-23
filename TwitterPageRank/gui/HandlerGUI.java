/***********************************************************************
 *                                                                     *
 * @author Bernard Hernandez Perez                                     *
 ***********************************************************************/

package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HandlerGUI {
	
	/**
	 * Maximun number of users ranked we want to show.
	 */
	public static final int maxUsers = 20;
	
	/**
	 * Method to get all the document that are in a directory.
	 * @param path
	 * @return File[]
	 */
	public static File[] getElementsIn(String path){
		File dir = new File(path);
		if (dir.exists())
			return dir.listFiles();
		return null;
	}
	
	/**
	 * Method to get all the users in the file "BlackList".
	 */
	@SuppressWarnings("finally")
	public static ArrayList<String> getContentBlackList(String path){
		ArrayList<String> blackList = new ArrayList<String>();
		try {
			File file = new File(path);
			
			if (file.exists()){
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while ((line=br.readLine())!=null) {
					blackList.add(line);
				}
				br.close();
			} else {
				blackList.add("The file " + path + " doesnt exist.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return blackList;
		}	
		
	}
	
	/**
	 * Method to get all the content of one "PageRank" file.
	 * @param file
	 * @return String
	 */
	@SuppressWarnings("finally")
	public static String getContentRank(String path){
		String content = "";
		try {
			File file = new File(path);
			
			if (file.exists()){
				BufferedReader br = new BufferedReader(new FileReader(file));
				int cont = 1;
				String line;
				while (((line=br.readLine())!=null) && (cont<=maxUsers)) {
					content += String.valueOf(cont) + ". " + line + "<br>";
					cont++;
				}
				br.close();
			} else {
				content = "The file " + path + " doesnt exist.";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return content;
		}	
	}
	
	public static void main(String[] args){
	}
	
}
