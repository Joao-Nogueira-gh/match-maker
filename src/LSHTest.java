package src;

import java.io.*;
import java.util.*;
/**
 * Class for testing of the LSH algorithm, developed in the SimilarityCalculator module.
 */
public class LSHTest {

	public static void main(String[] args) throws NumberFormatException, IOException {
		/**
		 * Using our generated file of users and their hobbies to check for similarites using the LSH algorithm.
		 */
		File usersF = new File("./../res/users.txt");
		
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