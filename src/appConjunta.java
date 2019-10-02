package src;

import java.io.*;
import java.util.*;

public class appConjunta {

	public static void main(String[] args) throws IOException {

		File usersF = new File("users.txt");
		
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
		
		/*					Leitura do ficheiro					*/

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
			// System.out.println("entrou "+fields[0]);

			UserIds.add(Integer.parseInt(fields[0]));

			for (int i = 1; i < fields.length; i++) {
				UserInterests.add(fields[i]);
			}
			userInfo[Integer.parseInt(fields[0])-1]=UserInterests;
			// System.out.println(line);

		}
		
		br.close();
		
		/*				Tratamento dos dados recebidos				*/

		
		// Se quisermos fazer a app interativa podemos perguntar qts utilizadores querem selecionar (% do cont_est)
		// e tambem qual o threshold de semelhan�a q querem verificar
		// e tipo quantos utilizadores mais semelhantes querem ver
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Qual o threshold desejado?");
		double threshold=sc.nextDouble();
		sc.close();
		
		Contador_Est cont=new Contador_Est(0.5);	//selecionemos % dos utilizadores
		@SuppressWarnings({ "rawtypes", "unchecked" })
		CountingBloomFilter<String> cbf=new CountingBloomFilter(10000,5,1);
		
		for(int i=0;i<userInfo.length;i++) {
			cont.count();
		}
		
		int betaUsers=cont.getCount();
		
		@SuppressWarnings("unchecked")
		ArrayList<String>[] selectedUsers= new ArrayList[betaUsers];

		
		for(int i=0;i<betaUsers;i++) {
			selectedUsers[i]=userInfo[i];
			for(int j=0;j<selectedUsers[i].size();j++) {
				cbf.insert(selectedUsers[i].get(j));
			}
		}

		SimilarityCalculator similarityCalc=new SimilarityCalculator(selectedUsers, 5);
		ArrayList<int[]> sims=new ArrayList<>();
		
		double[][] sim=similarityCalc.getSimilarity();
		Contador_Est areaCounter = new Contador_Est(0.5);
		
		for(int i=0;i<betaUsers;i++) {
			for(int r=0;r<selectedUsers[i].size();r++)
			{
				areaCounter.count();
			}
			for(int j=0;j<betaUsers;j++) {
				if(j <= i) {
					//System.out.printf("%5s","-");
				}else {
					//System.out.printf("%3.2f", sim[i][j]);
					if(sim[i][j] >= threshold) {
						System.out.println("Adicionei "+i+" "+j);
						int[] par=new int[2];
						par[0]=i;
						par[1]=j;
						sims.add(par);
					}
				}				
			}
			//System.out.println();
		}
		
		int maxFreq=0;
		String coolArea="ERRO";
		
		for(int i=0;i<inter.length;i++) {
			if(cbf.count(inter[i])>maxFreq) {
				maxFreq=cbf.count(inter[i]);
				coolArea=inter[i];
			}
		}
		
		int[] fixe;

		for(int i=0; i<sims.size(); i++)
		{
			fixe= sims.get(i);
			System.out.println("Similar users:\n" + fixe[0] + ":"+selectedUsers[fixe[0]]+"\n"+
					fixe[1] + ":"+selectedUsers[fixe[1]]);
		}
		
		System.out.println("A Area mais popular � " + coolArea + " com " + maxFreq + " escolhas entre os utilizadores");
		System.out.println("Foram feitos " + sims.size() + " pares");
		System.out.printf("Em m�dia, cada utilizador escolheu %3.2f interesses",(double)areaCounter.getCount()*2/(double)betaUsers);


		
		
		
	}
}
