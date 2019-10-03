package src;

import java.io.*;
import java.util.*;
/**
 * Class for testing of the SimilarityCalculator module
 */
public class SimCalcTest {

	public static void main(String[] args) throws IOException {

		/**
		 * Test number 1, calculate similarities between text files
		 */

		int nFiles = 3;
		int nFuncs = 10;
		File[] files = new File[nFiles];

		for (int numero = 1; numero <= nFiles; numero++) {
			files[numero - 1] = new File("C:\\Users\\John\\Desktop\\mpei\\livro" + (numero) + ".txt");
		}

		SimilarityCalculator simCalc = new SimilarityCalculator(files, nFuncs);

		// Começaremos por ver o conteudo de cada ficheiro

		simCalc.printWords();

		// A matriz assinatura para 2 ficheiros será composta por 2 colunas (nFicheiros)
		// e
		// nFuncs linhas (neste caso 5)

		@SuppressWarnings("unused")
		int[][] sign1 = simCalc.getSignatureMatrix();
		double[][] sim = simCalc.getSimilarity();

		SimilarityCalculator temp;

		for (int i = 20; i <= 140; i += 20) {
			System.out.println("nFuncs: " + i);
			temp = new SimilarityCalculator(files, i);
			sim = temp.getSimilarity();

			for (int t = 0; t < nFiles; t++) {
				for (int j = 0; j < nFiles; j++) {
					if (j <= t) {
						System.out.printf("%5s", "- ");
					} else {
						System.out.printf("%3.2f ", sim[t][j]);
					}
				}
				System.out.println();
			}
		}

		System.out.println("--------------------------------------");

		System.out.println("Ready?");
		System.in.read();
		System.in.read();

		/**
		 * Test number 2, calculate similarities between text files, this time using Shingles
		 */

		int numShingles = 5;
		nFuncs = 10;
		nFiles = 3;
//
//		
		ArrayList<String> a = makeShingles("C:\\Users\\John\\Desktop\\mpei\\livro1.txt", numShingles);
		ArrayList<String> b = makeShingles("C:\\Users\\John\\Desktop\\mpei\\livro2.txt", numShingles);
		ArrayList<String> c = makeShingles("C:\\Users\\John\\Desktop\\mpei\\livro3.txt", numShingles);

		ArrayList<ArrayList<String>> conj = new ArrayList<ArrayList<String>>();
		conj.add(a);
		conj.add(b);
		conj.add(c);

		SimilarityCalculator simCalc2 = new SimilarityCalculator(conj, nFuncs);

		simCalc2.printWords();

		@SuppressWarnings("unused")
		double[][] simil;

		SimilarityCalculator temp2;
		
		for (int i = 20; i <= 140; i += 20) {
			System.out.println("nFuncs: " + i);
			temp2 = new SimilarityCalculator(conj, i);
			simil = temp2.getSimilarity();

			for (int t = 0; t < nFiles; t++) {
				for (int j = 0; j < nFiles; j++) {
					if (j <= t) {
						System.out.printf("%5s", "- ");
					} else {
						System.out.printf("%3.2f ", sim[t][j]);
					}
				}
				System.out.println();
			}
		}
		System.out.println("--------------------------------------");

		System.out.println("Ready?");
		System.in.read();
		System.in.read();

		/**
		 * Test number 3, calculate similarities between users of a 'IMDB-like' website, MovieLens
		 */

		HashMap<Integer, ArrayList<String>> filmes = MovieLens("C:\\Users\\John\\Desktop\\mpei\\u.data");
		// int nUsers = filmes.keySet().size();
		ArrayList<Integer> userslist = new ArrayList<>();
		for (Integer id : filmes.keySet()) {
			userslist.add(id);
		}
		Collection<ArrayList<String>> values = filmes.values();
		ArrayList<ArrayList<String>> filmes_per_user = new ArrayList<>();
		for (ArrayList<String> entry : values) {
			filmes_per_user.add(entry);
		}
		SimilarityCalculator movielen = new SimilarityCalculator(filmes_per_user, 300);

		// movielen.printWords();

		System.out.println("done");

		double[][] simili = movielen.getSimilarity();

		System.out.println("done");
		int iguais = 0;
		for (int i = 0; i < filmes_per_user.size(); i++) {
			for (int j = i + 1; j < filmes_per_user.size(); j++) {
				if (simili[i][j] > 0.6) {
					System.out.println("Utilizadores semelhantes: " + (i+1) + " - " + (j+1) + " com sim="+simili[i][j]);
					iguais++;
				}

			}
		}
		System.out.println("Numero de semelhanças com threshold superior ao definido: " + iguais);

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
	 * Function used to read the raw data from the Movie Lens website
	 * @param filepath
	 * @return hashmap of users and films
	 * @throws IOException
	 */
	public static HashMap<Integer, ArrayList<String>> MovieLens(String filepath) throws IOException {
		File file = new File(filepath);

		// return set, users, nu=size(users)

		HashMap<Integer, ArrayList<String>> filmes = new HashMap<>();

		BufferedReader br = new BufferedReader(new FileReader(file));

		String line;
		String[] fields;
		int id;
		String film;
		while ((line = br.readLine()) != null) {
			fields = line.split("\t");
			for (int i = 0; i < 2; i++) {
				id = Integer.parseInt(fields[0]);
				film = fields[1];

				if (filmes.containsKey(id)) {
					ArrayList<String> oldarrl = filmes.get(id);
					oldarrl.add(film);
					filmes.put(id, oldarrl);
				} else {
					ArrayList<String> ufilms = new ArrayList<>();
					ufilms.add(film);
					filmes.put(id, ufilms);
				}
				// System.out.print(fields[i]+" ");
			}
			// System.out.println();

		}

		br.close();

		return filmes;
	}
}