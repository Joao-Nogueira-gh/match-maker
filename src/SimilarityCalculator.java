package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SimilarityCalculator {
	
	// min hash e o minimo da fun�ao hash
	// aplicar hash functions a cada uma das palavras dos documentos e vou buscar a min hash gerada por cada um
	// se usar n hash functions vou acabar com n min hashes;
	// um documento e caracterizado por essas n min hashes -> conjunto de min hashes aka assinatura;
	// aplicar um jaccard meio chines nesses dois conjuntos de min hashes;
	// se forem iguais interse�ao++;
	// a similaridade vai ser dada por interse�ao/n;
	
	int nFuncs;
	int nFiles;
	int b,r;
	ArrayList<String>[] words;	// array de arraylists onde cada posi�ao ira conter um array com as palavras do ficheiro da posi�ao i do array de ficheiros passado como argumento
	double threshold;
	
	@SuppressWarnings("unchecked")
	public SimilarityCalculator(File[] files, int nFuncs) throws FileNotFoundException {
		this.nFuncs=nFuncs;
		this.nFiles=files.length;
		Scanner[] scanArray = new Scanner[nFiles];
		this.words=new ArrayList[nFiles];
		
		for(int i=0; i < files.length; i++) {
			scanArray[i] = new Scanner(files[i]);
			words[i]=new ArrayList<String>();
			while(scanArray[i].hasNext()) {
				words[i].add(scanArray[i].next());
			}
			scanArray[i].close();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public SimilarityCalculator(String[][] wordArrays, int nFuncs) throws FileNotFoundException {
		this.nFuncs=nFuncs;
		this.nFiles=wordArrays.length;
		this.words=new ArrayList[nFiles];
		
		for(int i=0; i < nFiles; i++) {
			words[i]=new ArrayList<>();
			for(int j=0; j < wordArrays[i].length; j++) {
				if(wordArrays[i][j]!=null) {
					words[i].add(wordArrays[i][j]);
				}
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public SimilarityCalculator(ArrayList<ArrayList<String>> arrList, int nFuncs) {
		this.nFuncs = nFuncs;
		this.nFiles = arrList.size();
		this.words = new ArrayList[nFiles];

		for (int i = 0; i < nFiles; i++) {
			words[i] = arrList.get(i);
		}
	}
	
	public SimilarityCalculator(ArrayList<String>[] arr, int nFuncs) { //novo construtor
		this.nFuncs = nFuncs;
		this.nFiles = arr.length;
		this.words=arr;
	}
	
	public void printWords() {		// prints every line of every file
		int line;
		int i;
		for(int file=0; file < this.nFiles; file++) {
			System.out.println("FICHEIRO " + (file+1));
			line=1;
			for(i = 0;i<words[file].size();i++) {
				System.out.println("Palavra " + line + "->" + words[file].get(i));
				line++;
			}
		}
	}
			
	public int[][] getSignatureMatrix() {
		int hash, minHash;
		int assinatura[][]=new int[nFuncs][nFiles];
		for(int ficheiro = 0; ficheiro < this.nFiles; ficheiro++) {
			for(int i = 0;i<nFuncs;i++) {
				minHash=1000000000;	//resetting the minhash
				for(int j = 0; j < words[ficheiro].size(); j++) {
					String elemento = words[ficheiro].get(j);
					String key = elemento;
					key=key+(i*Math.pow(2, 32)%12345678);	// trying to get sparser minHashes
					hash = Math.abs(key.hashCode());
					if(hash < minHash) {
						minHash=hash;
					}else if(hash > 1000000000) {
						hash=Math.floorDiv(hash,2);
						if(hash < minHash) {
							minHash=hash;
						}
					}
				}
				assinatura[i][ficheiro]=minHash;
			}
		}
		return assinatura;
	}
	
	
	public double[][] getSimilarity() {	
		
		double[][] similarity = new double[nFiles][nFiles];
		int[][] sign= this.getSignatureMatrix();
		
		for(int file1=0;file1 < nFiles-1;file1++) {
			
			for(int file2=file1+1;file2<nFiles;file2++) {
				
				int iguais=0;
		
				for(int i=0;i < this.nFuncs;i++) {
											
						if(sign[i][file1]==sign[i][file2]) {
							iguais++;
						}
				}
				
				similarity[file1][file2]=(double)iguais/(double)nFuncs;
			}
		}
		
		
		return similarity;
	}
	
	public int newHashCode(String key) {		// sounds good, doesnt work
		int hash=1;
		for(int i=0; i < key.length();i++) {
			hash*=(int) key.charAt(i);
		}
		return hash;
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
	
	public SimilarityCalculator(ArrayList<String>[] arrList, double threshold, int b, int r) {
		// split the signature matrix M into bands
		// hash bands of different documents
		// after hashing multiple times, 2 bands hashed at least once to the same bucket
		// chances are they are similar
		
		// r rows/ b bands (b*r=nFuncs)
		this.b=b;
		this.r=r;
		this.nFuncs=b*r;
		this.threshold=threshold;
		
		this.nFiles = arrList.length;
		this.words=arrList;
	}
	
	public void LSH() {
		int[][] sim=this.getSignatureMatrix();
		ArrayList<int[]> bandas=new ArrayList<>();	// arrayList de arrays (bandas), onde cada
												// posi�ao sera ocupada por um array (banda)
		for(int i=0;i<b;i++) {					// onde cada posi�ao equivale ao hashing
			//if(bandas.get(i)==null){			// de cada coluna (documento)
				int[] banda=new int[nFiles];
				for(int ai=0;ai<nFiles;ai++) {
					int hashThis=1;
					for(int rows=0;rows<r;rows++) {
						hashThis*=sim[rows][ai];
					}
					banda[ai]=hash(hashThis);
				}
				bandas.add(banda);
			//}
		}
		
		// Temos neste momento uma estrutura de dados que contem todas as bandas, uma em cada posi�ao
		// Queremos agora mapear cada uma das posi�oes das bandas para buckets; se pelo menos
		// uma das posi�oes da alguma das bandas mapear para a mm posi�ao que outra
		// probably esses cenas sao semelhantes
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] buckets = new ArrayList[10000];
				
		for(int i=0; i<b;i++) {
			for(int j=0;j<nFiles;j++) {
				if(buckets[bandas.get(i)[j]]==null) {
					buckets[bandas.get(i)[j]]=new ArrayList<>();
					buckets[bandas.get(i)[j]].add(j);
				}else {
					if(!buckets[bandas.get(i)[j]].contains(j)) {
						buckets[bandas.get(i)[j]].add(j);
					}
				}
			}
		}
		
		// Agora temos uma estrutura de dados com todos os buckets e onde foram inseridas
		// todas as hashes de cada coluna de cada banda. Sempre que o size de cada posi�ao
		// for maior que 1, quer dizer que temos similaridade entre os dois ficheiros
		// nessa posi�ao
		
		double[][] similarity=this.getSimilarity();
		
		for(int i=0; i<buckets.length;i++) {
			if(buckets[i]!=null && buckets[i].size()>1) {
				// Agora quero iterar por todos estes itens semelhantes e verificar se o sao de facto
				// 
				for(int user1=0;user1<buckets[i].size()-1;user1++) {
					for(int user2=user1+1;user2<buckets[i].size();user2++) {
						if(similarity[user1][user2]>threshold) {
							System.out.println("Utilizadores semelhantes: " + buckets[i].get(user1)+" - "+buckets[i].get(user2));
							System.out.println(buckets[i].get(user1) + ": " + words[buckets[i].get(user1)]);
							System.out.println(buckets[i].get(user2) + ": " + words[buckets[i].get(user2)]);
						}
					}
				}
			}
		}
		
		
		
		// WHAT IF: arrays de documentos onde cada array representa uma banda
		// e cada posi�ao do array representa a hash duma coluna
		
	}
	
	public int hash(int nr) {
		return Math.abs((nr*nFiles)%10000);	// 10000 = espa�o no bucket
	}
	
	

}
