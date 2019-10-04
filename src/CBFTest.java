package src;

import java.io.*;
import java.util.*;
/**
 * Class for testing of the module CountingBloomFilter
 */
public class CBFTeste {

	public static void main(String[] args) throws IOException {
		/**
		 * In order to test our Counting Bloom Filter we will follow the procedures learned in the lab assignments: <br>
		 * Insert <i>n</i> random strings in the filter and look for other <i>m</i> (also random generated) strings in that filter. <br>
		 * The probability of two exactly equal strings in those two groups being generated is incredibly small so it's disregarded. <br>
		 * Whenever a string from <i>m</i> is detected in the filter, we consider it a false positive.
		 */

		CountingBloomFilter<String> cbf;
		String texto;
		int fp;
		int n = 80000, m = 10000;
		double percentagem, pteorica;

		CountingBloomFilter<String> valor_k = new CountingBloomFilter<>(n, m, 2);

		int opt_k = valor_k.getnFuncs();
		int[] nFuncs = { 1, 5, 10, 15, 20, 25, 50, 100, opt_k };

		System.out.println("Valor optimo de k: " + opt_k);
		System.out.println();

		for (int nFunc = 0; nFunc < nFuncs.length; nFunc++) {

			fp = 0;

			cbf = new CountingBloomFilter<String>(n, nFuncs[nFunc], 1);

			for (int i = 0; i < m; i++) {
				texto = generateString();
				cbf.insert(texto); // inserimos 1e4 membros
			}

			for (int i = 0; i < 100000; i++) {
				texto = generateString();
				if (cbf.isMember(texto)) { // testamos 1e5 membros
					fp++;
				}
			}

			percentagem = (double) fp / (double) 100000;
			double e = Math.exp((double) -nFuncs[nFunc] * m / n);
			pteorica = Math.pow(1 - e, nFuncs[nFunc]);

			System.out.println("Percentagem prática de falsos positivos para " + nFuncs[nFunc] + " hashfunctions: "
					+ percentagem * 100 + "%");
			System.out.println("Percentagem teórica de falsos positivos para " + nFuncs[nFunc] + " hashfunctions: "
					+ pteorica * 100 + "%");
			System.out.println(
					"----------------------------------------------------------------------------------------------");

		}
		double e = Math.exp((double) -opt_k * m / n);
		pteorica = Math.pow(1 - e, opt_k);

		CountingBloomFilter<String> valor_n = new CountingBloomFilter<String>(pteorica, m, opt_k);

		int opt_n = valor_n.getSize();
		System.out.println("Valor optimo de n: " + opt_n);

		System.out.println(
				"----------------------------------------------------------------------------------------------");

		System.out.println("Ready?");
		System.in.read();
		System.in.read();
		
		File livro = new File("./../res/livrog.txt");
		
		CountingBloomFilter<String> cbfLivro=new CountingBloomFilter<>(10000, 1000, 2);

		BufferedReader br = new BufferedReader(new FileReader(livro)); //lorem ipsum 1000 palavras
		ArrayList<String> palavras=new ArrayList<String>();
		HashSet<String> palavrasSet=new HashSet<String>();
		String st;
		while ((st = br.readLine()) != null) {
			String[] fields = st.split(" ");
			for (int j = 0; j < fields.length; j++) {
				palavras.add(fields[j]);
			}
		}
		for (int i = 0; i < palavras.size(); i++) {
			palavrasSet.add(palavras.get(i));
		}
		for (int i = 0; i < palavras.size(); i++) {
			cbfLivro.insert(palavras.get(i));
		}
		int j=0;
		for (String s: palavrasSet) {
			System.out.println(s+" -> "+cbfLivro.count(s));
			j++;
		}
		System.out.println(j+ " palavras diferentes em 1000");
		br.close();
		
		System.out.println(
				"----------------------------------------------------------------------------------------------");

		System.out.println("Ready?");
		System.in.read();
		
		CountingBloomFilter<String> teste=new CountingBloomFilter<String>(1000,100,2);
		int fp2=0;
		int fn=0;
		ArrayList<String> membros=new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			texto=generateString();
			teste.insert(texto);
			membros.add(texto);
		}
		for (int i = 0; i < 1000; i++) {
			texto=generateString();
			if (teste.isMember(texto)) {
				teste.delete(texto);
				fp2++;
			}
		}
		for (int i = 0; i < membros.size(); i++) {
			if (teste.isMember(membros.get(i))==false) {
				fn++;
			}
		}
		System.out.println("Houve "+fp2 +" falsos positivos, que deram origem a "+fn+" falsos negativos!");
	}

	public static String generateString() {
		String uuid = Long.toHexString(Double.doubleToLongBits(Math.random()));
		uuid = uuid.substring(2, uuid.length());
		return uuid;
	}

}