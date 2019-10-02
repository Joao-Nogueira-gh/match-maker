package mpei;

import java.io.*;
import java.util.*;

public class TesteConjunto {

	public static void main(String[] args) throws IOException {

		// Teste dos tres modulos
		
		int nFiles=8;
		File[] ficheiros= new File[nFiles];
		for(int numero=1;numero<=nFiles;numero++) {
			ficheiros[numero-1]=new File("C:\\Users\\John\\Desktop\\mpei\\text"+numero+".txt");
		}
		
		
		// Testaremos agora o cbf usando os 8 ficheiros acima
		
		Scanner sc;
		CountingBloomFilter<String> cbf = new CountingBloomFilter<String>(100000,15,1);
		Contador_Est ce=new Contador_Est(0.5);
		
		for(int ficheiro=0;ficheiro < nFiles; ficheiro++) {
			sc=new Scanner(ficheiros[ficheiro]);
			while(sc.hasNext()) {
				cbf.insert(sc.next());
				ce.count();
			}
		}
		
		System.out.println("Quer saber a frequencia de que palavra nos 8 ficheiros analisados?");
		sc=new Scanner(System.in);
		String palavra=sc.nextLine();
		sc.close();
		System.out.println("A palavra '"+palavra+"' apareceu "+cbf.count(palavra)+" vez(es)");
		
		// para testar o contador estocastico quero saber quantas palavras ha no total
		System.out.println("O contador estocástico contou " + ce.getCount() + " palavras. Logo temos cerca de " + ce.getCount()/0.5 + " palavras no total.");
		
		//	Iremos testar agora os falsos positivos
		
		int nFuncs=100;		
		SimilarityCalculator sca1= new SimilarityCalculator(ficheiros,nFuncs);
		sca1.printWords();
		System.out.println();
		
		@SuppressWarnings("unused")
		int[][] sign = sca1.getSignatureMatrix();
		double[][] sims = sca1.getSimilarity();
		double maxSim=0;
		int simFile1=0,simFile2=0;

//		for(int i=0; i < nFuncs; i++) {
//			for(int j=0; j < nFiles; j++) {
//				System.out.printf("%9d ",sign[i][j]);
//				
//			}
//			System.out.println();
//		}
		
		
		for(int i=0; i < nFiles; i++) {
			for(int j=0; j < nFiles; j++) {
				System.out.printf("%3.2f ",sims[i][j]);
				if(sims[i][j]>maxSim) {
					maxSim=sims[i][j];
					simFile1=i+1;
					simFile2=j+1;
				}
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.println("The two most similar files are files number "+simFile1+" and "+simFile2+" with a similarity of "+maxSim);



	}
	
	public static String generateString() {
        String uuid = Long.toHexString(Double.doubleToLongBits(Math.random()));
        uuid=uuid.substring(2, uuid.length());
        return uuid;
    }
	public static ArrayList<String> makeShingles(String filepath, int k) throws IOException {
		File file = new File(filepath);

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		ArrayList<String> conjShingles = new ArrayList<String>();

		while ((st = br.readLine()) != null) {
			for (int i = 0; i < st.length() - k + 1; i++) {
				conjShingles.add(st.substring(i, i + k));
			}
		}

		br.close();

		return conjShingles;
	}

}