package projeto;

import java.io.*;
import java.util.*;

public class generateFile {

	public static void main(String[] args) throws IOException {
		int nUsers = 2000;

		PrintWriter pw = new PrintWriter("users.txt", "UTF-8");

		int nInteresses, ind;
		Random rand = new Random();

		String[] inter = { "Fishing", "Cooking", "Travelling", "Writing", "Gaming", "Dancing", "Reading", "Photography",
				"BoardGames", "Singing", "Music", "Magic", "Pottery", "Knitting", "Television", "Movies", "Running",
				"Arts", "Yoga", "Acting", "Animals", "Astronomy", "Charity", "Camping", "Collecting", "Crafts",
				"Drawing", "Gymnastics", "Golf", "PlayingInstruments", "Hiking", "HorseRiding", "MartialArts",
				"Paiting", "ScubaDiving", "SkyDiving", "Surfing", "Swimming", "Tennis", "Volunteer", "Wrestling",
				"Sports", "Football", "Cycling", "Skiing", "Hockey", "Cosplaying", "Fashion", "History", "Languages",
				"Lego", "Puzzles", "Basketball", "Baseball", "Paintball", "Rugby", "KartRacing", "Bird Watching",
				"Astrology", "Farming", "Gardening", "Poetry", "CardGames", "Darts", "LearningLanguages", "Bowling",
				"Sailing", "Hunting", "WorkingOut", "Boxing", "Walking", "Decorating", "BloggingWebsites", "Tattoing",
				"Programming", "StockMarket"};
		int maxInter = inter.length;

		for (int i = 1; i <= nUsers; i++) {
			pw.print(i + " ");
			nInteresses = rand.nextInt((12 - 4) + 1) + 4;
			int[] interEscolhidos = new int[nInteresses];

			for (int j = 1; j <= nInteresses; j++) {
				ind = rand.nextInt((maxInter - 1) + 1) + 1;
				while ((contains(interEscolhidos, ind)) >= 0) {
					// System.out.println("retrying-> o " + j + " ia ser " + inter[ind - 1] + " no
					// utilizador " + i);
					ind = rand.nextInt((maxInter - 1) + 1) + 1;
				}
				interEscolhidos[j - 1] = ind;
				pw.print(inter[ind - 1] + " ");
			}
			pw.print("\n");
			interEscolhidos = null;
		}
		System.out.println(maxInter);
		pw.close();
		
		boolean duplicates = false;
		for (int k = 0; k < inter.length; k++) {
			for (int l = k + 1; l < inter.length; l++) {
				if (l != k && inter[l] == inter[k]) {
					duplicates = true;}
			}
		}
		System.out.println(duplicates);
	}

	public static int contains(int[] arr, int num) {
		int r = -1;
		for (int i = 0; i < arr.length; i++) {
			if (num == arr[i]) {
				r = 1;
			}
		}
		return r;
	}

}
