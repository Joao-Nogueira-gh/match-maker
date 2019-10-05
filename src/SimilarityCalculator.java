

import java.io.*;
import java.util.*;
/**
 * The SimilarityCalculator determines the most identical documents out of a set. <br>
 * How it works: <br>
 *		* apply <i>k</i> hash functions to each of the words in the documents; <br>
 *		* retrieve the minimal hash value that was generated; <br>
 *		* having k hash functions, a document's signature is the set of k min hashes <br>
 *		* through algorithms like the <i>Jaccard Index</i> the similarity of those signatures is computed.
 */
public class SimilarityCalculator {

	int nFuncs;
	int nFiles;
	int b, r;
	ArrayList<String>[] words; /**< ArrayList of Arrays of strings, where each ArrayList position <i>i</i> contains the an array of Strings with the words of each document. */
	double threshold;

	@SuppressWarnings("unchecked")
	/**
	 * First constructor option, directly receives the files and number of hash functions, stores all words in the file
	 * @param files array of files to check
	 * @param nFuncs number of hash functions being used
	 * @throws FileNotFoundException
	 */
	public SimilarityCalculator(File[] files, int nFuncs) throws FileNotFoundException {
		this.nFuncs = nFuncs;
		this.nFiles = files.length;
		Scanner[] scanArray = new Scanner[nFiles];
		this.words = new ArrayList[nFiles];

		for (int i = 0; i < files.length; i++) {
			scanArray[i] = new Scanner(files[i]);
			words[i] = new ArrayList<String>();
			while (scanArray[i].hasNext()) {
				words[i].add(scanArray[i].next());
			}
			scanArray[i].close();
		}

	}

	@SuppressWarnings("unchecked")
	/**
	 * Second constructor option, instead of accepting the files to analyzed, it handles the array of words of those files directly
	 * @param wordArrays array of arrays containg words of each file
	 * @param nFuncs number of hash functions
	 * @throws FileNotFoundException
	 */
	public SimilarityCalculator(String[][] wordArrays, int nFuncs) throws FileNotFoundException {
		this.nFuncs = nFuncs;
		this.nFiles = wordArrays.length;
		this.words = new ArrayList[nFiles];

		for (int i = 0; i < nFiles; i++) {
			words[i] = new ArrayList<>();
			for (int j = 0; j < wordArrays[i].length; j++) {
				if (wordArrays[i][j] != null) {
					words[i].add(wordArrays[i][j]);
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * Third constructor option, this time the first argument is an ArrayList of ArrayList of all the words in the already analyzed docs
	 * @param arrList arraylist of arraylists containg words of each file
	 * @param nFuncs number of hash functions
	 */
	public SimilarityCalculator(ArrayList<ArrayList<String>> arrList, int nFuncs) {
		this.nFuncs = nFuncs;
		this.nFiles = arrList.size();
		this.words = new ArrayList[nFiles];

		for (int i = 0; i < nFiles; i++) {
			words[i] = arrList.get(i);
		}
	}
	/**
	 * Fourth and final constructor option, accepts an Arraylist of Arrays of Strings
	 * @param arr Arraylist of arrays of string containg words in each file
	 * @param nFuncs number of hash functions
	 */
	public SimilarityCalculator(ArrayList<String>[] arr, int nFuncs) { // novo construtor
		this.nFuncs = nFuncs;
		this.nFiles = arr.length;
		this.words = arr;
	}

	public ArrayList<String>[] getWords() {
		return words;
	}

	public void setWords(ArrayList<String>[] words) {
		this.words = words;
	}
	/**
	 * Basically prints all the analyzed files, line by line
	 */
	public void printWords() { // prints every line of every file
		int line;
		int i;
		for (int file = 0; file < this.nFiles; file++) {
			System.out.println("FICHEIRO " + (file + 1));
			line = 1;
			for (i = 0; i < words[file].size(); i++) {
				System.out.println("Palavra " + line + "->" + words[file].get(i));
				line++;
			}
		}
	}
	/**
	 * Computes the signature of each file, to be compared later
	 * @return document signature
	 */
	public int[][] getSignatureMatrix() {
		int[] a = new int[nFuncs]; //n usado
		int[] b = new int[nFuncs];
		Random rand = new Random();
		for (int i = 0; i < this.nFuncs; i++) {
			a[i] = rand.nextInt((10000 - 1) + 1) + 1;
			b[i] = rand.nextInt((10000 - 1) + 1) + 1;
		}

		int hash, minHash;
		int assinatura[][] = new int[nFuncs][nFiles];
		for (int ficheiro = 0; ficheiro < this.nFiles; ficheiro++) {
			for (int i = 0; i < nFuncs; i++) {
				minHash = 1000000000; // resetting the minhash
				for (int j = 0; j < words[ficheiro].size(); j++) {
					String elemento = words[ficheiro].get(j);
					String key = elemento;
					key = key + (i * Math.pow(2, 32) % 12345678); // trying to get sparser minHashes
					hash = Math.abs(key.hashCode());
					if (hash < minHash) {
						minHash = hash;
					} else if (hash > 1000000000) {
						hash = Math.floorDiv(hash, 2);
						if (hash < minHash) {
							minHash = hash;
						}
					}
				}
				assinatura[i][ficheiro] = minHash;
			}
		}
		return assinatura;
	}
	/**
	 * Computes the similarity between all the analyzed files, using the already computed signatures of each file
	 * @return array of similarities between files
	 */
	public double[][] getSimilarity() {

		double[][] similarity = new double[nFiles][nFiles];

		int[][] sign = this.getSignatureMatrix();

		for (int file1 = 0; file1 < nFiles - 1; file1++) {

			for (int file2 = file1 + 1; file2 < nFiles; file2++) {

				int iguais = 0;

				for (int i = 0; i < this.nFuncs; i++) {
					if (sign[i][file1] == sign[i][file2]) {
						iguais++;
					}
				}
				similarity[file1][file2] = (double) iguais / (double) nFuncs;
			}
		}
		return similarity;
	}
	/**
	 * Hash Code function
	 * @param key to be hashed
	 * @return hash code
	 */
	public int newHashCode(String key) {
		int hash = 0;
		for (int i = 0; i < key.length(); i++) {
			hash *= (int) key.charAt(i);
		}
		return hash;
	}
	/**
	 * Function used to create Shingles (small blocks of text used for comparison)
	 * @param filepath
	 * @param k length of each shingle
	 * @return an array of shingles
	 * @throws IOException
	 */
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
	/**
	 * Another constructor for using the LSH method, providing a threshold, number of rows and bands and the usual array of words
	 * @param arrList of all words in all files
	 * @param threshold of similarity to be considered
	 * @param b number of bands
	 * @param r number of rows
	 */
	public SimilarityCalculator(ArrayList<String>[] arrList, double threshold, int b, int r) {
		this.b=b;
		this.r=r;
		this.nFuncs=b*r;
		this.threshold=threshold;
		
		this.nFiles = arrList.length;
		this.words=arrList;
	}
	/**
	 * LSH, an advance method for computing the similarities after having obtained the signature of the documents <br>
	 * How it works: <br>
	 * 		* split the signature matrix M into bands <br>
	 * 		* hash bands of different documents <br>
	 * 		* after hashing multiple times, if 2 bands are hashed at least once to the same bucket chances are they are similar
	 */
	public void LSH() {
		int[][] sim=this.getSignatureMatrix();
		ArrayList<int[]> bandas=new ArrayList<>();
		for(int i=0;i<b;i++) {				
			//if(bandas.get(i)==null){
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
		// todas as hashes de cada coluna de cada banda. Sempre que o size de cada posiçao
		// for maior que 1, quer dizer que temos similaridade entre os dois ficheiros
		// nessa posiçao
		
		double[][] similarity=this.getSimilarity();
		
		int pares=0;
		
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
							pares++;
						}
					}
				}
			}
		}
		System.out.println(pares +" pares encontrados!");
		
	}
	/**
	 * An extra hash function
	 */
	public int hash(int nr) {
		return Math.abs((nr*nFiles)%10000);	// 10000 = espaço no bucket
	}

}
