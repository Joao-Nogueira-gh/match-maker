package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class SimCalcTest {

	public static void main(String[] args) throws FileNotFoundException {
		
		int nFiles=8;
		int nFuncs=5;
		File[] files= new File[nFiles];
		
		for(int numero=1;numero<=nFiles;numero++) {
			files[numero-1]=new File("C:\\Users\\smoos\\eclipse-workspace\\MPEI\\src\\projeto\\text"+numero+".txt");
		}
		
		SimilarityCalculator simCalc = new SimilarityCalculator(files,nFuncs);
		
		// Come�aremos por ver o conteudo de cada ficheiro
		
		simCalc.printWords();
		
		// A matriz assinatura para 2 ficheiros ser� composta por 2 colunas (nFicheiros) e
		// nFuncs linhas (neste caso 5)
				
		double[][] sim;
		
		/*for(int i=0; i < nFuncs; i++) {
			for(int j=0; j < nFiles; j++) {
				System.out.printf("%9d ",sign1[i][j]);
			}
			System.out.println();
		}
		
		for(int i=0; i < nFiles; i++) {
			for(int j=0; j < nFiles; j++) {
				System.out.printf("%3.2f ",sim[i][j]);
			}
			System.out.println();
		}*/
		
		SimilarityCalculator temp;
		
		for(int i=20; i <= 140; i+=20) {
			System.out.println("nFuncs: " + i);
			temp=new SimilarityCalculator(files,i);
			sim=temp.getSimilarity();
			
			/*for(int t=0; t < i; t++) {
				for(int j=0; j < nFiles; j++) {
					System.out.printf("%9d ",sign1[t][j]);
				}
				System.out.println();
			}*/
			
			for(int t=0; t < nFiles; t++) {
				for(int j=0; j < nFiles; j++) {
					if(j <= t) {
						System.out.printf("%5s","- ");
					}else {
					System.out.printf("%3.2f ",sim[t][j]);
					}
				}
				System.out.println();
			}
		}
		
		////////////////////////////////// U.DATA ///////////////////////////////////////////
		
		File udata=new File("u.data");
		Scanner sc = new Scanner(udata);
		int utilizador;
		
		HashMap<Integer, ArrayList<Integer>> users=new HashMap<Integer, ArrayList<Integer>>();
		
		while(sc.hasNextInt()) {
			utilizador=sc.nextInt();
			if(users.get(utilizador)==null) {
				users.put(utilizador, new ArrayList<Integer>());
			}
			users.get(utilizador).add(sc.nextInt());
			sc.nextLine();
		}
		
		sc.close();
		
		for(int i:users.keySet()) {
			System.out.println(i + " " + users.get(i));	// impressao dos utilizadores e filmes q estes avaliaram	
		}

		// Neste momento temos um mapa com todos os utilizadores e filmes que eles avaliaram
		// Vamos agora passar o nosso HashMap para uma matriz de Strings
		
		// para criar a matriz bem ajustada preciso do nr de utilizadores e do
		// maximo de filmes assistidos por um utilizador
		
		int maxUsers=Collections.max(users.keySet());
		
		ArrayList<Integer> sizes=new ArrayList<>();
		
		for(int i:users.keySet()) {
			sizes.add(users.get(i).size());
		}
		
		int maxMovies=Collections.max(sizes);
		
		String[][] usersMatrix = new String[maxUsers][maxMovies];

		for(int i=1;i<=maxUsers;i++) {
			for(int j=0;j<users.get(i).size();j++) {
				usersMatrix[i-1][j]=users.get(i).get(j).toString();
			}
		}
		
		// Temos agora uma matriz em que cada linha corresponde a um utilizador e aos filmes q ele avaliou
		
		SimilarityCalculator simCalc2=new SimilarityCalculator(usersMatrix,100);
		//int[][] signMat = simCalc2.getSignatureMatrix();	// matriz assinatura (minHashes de cada user)
		
		/*for(int i=0;i<signMat.length;i++) {		// deveria imprimir 5 linhas pq 5 hashFuncs
			for(int j=0;j<signMat[i].length;j++) {
				System.out.print(signMat[i][j] + ";");
			}
			System.out.println();
		}*/		// -> Impressao da matriz assinatura

		double[][] simMat = simCalc2.getSimilarity();
		int similarUser=0;
		
		for(int t=0; t < maxUsers; t++) {
			for(int j=0; j < maxUsers; j++) {
				/*if(j <= t) {
					System.out.printf("%5s","- ");
				}else {
					System.out.printf("%3.2f ",simMat[t][j]);
				}*/		// -> impressao da matriz similaridade entre Users
				if(simMat[t][j] > 0.6) {
					System.out.println("Utilizadores semelhantes: " + t + " - " + j + " com sim="+simMat[t][j]);
					similarUser++;
				}
			}
			//System.out.println();
		}

		System.out.println("Numero de semelhan�as com threshold superior ao definido: " + similarUser);
	}

}
