package src;

import java.io.*;
import java.util.*;
/**
 * Class for testing of the LSH algorithm created in the SimilarityCalc module
 */
public class LSHTest {

	public static void main(String[] args) throws NumberFormatException, IOException {
		/**
		 * Using or generated file of users and hobbies to check for similarites using LSH
		 */
		File usersF = new File("users.txt");
		
		BufferedReader br = new BufferedReader(new FileReader(usersF));

		String line;

		String[] fields;

		ArrayList<Integer> UserIds = new ArrayList<Integer>();
		int nUsers=2000;
		@SuppressWarnings("unchecked")
		ArrayList<String>[] userInfo = new ArrayList[nUsers];

		while ((line = br.readLine()) != null) {
			ArrayList<String> UserInterests = new ArrayList<String>();
			fields = line.split(" ");

			UserIds.add(Integer.parseInt(fields[0]));

			for (int i = 1; i < fields.length; i++) {
				UserInterests.add(fields[i]);
			}
			userInfo[Integer.parseInt(fields[0])-1]=UserInterests;

		}
		
		br.close();
		
		SimilarityCalculator lsh=new SimilarityCalculator(userInfo,0.7,10,200);
		lsh.LSH();

	}

}