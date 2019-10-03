package src;

import java.io.*;
/**
 * This class is used to test how good our hash functions are, and generates matlab (.m) files from which you can create graphs to check said performance
 */
public class HashFunctionTest {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		/**
		 * Generate n different strings <br>
		 * Hash them accross m slots <br>
		 * Check their distribution <br>
		 */		
		int n=1000;
		int nFuncs;
		int[] array=new int[1000000];
		int[] hashFunctions = {1, 5, 10, 20, 50, 100};
		PrintWriter writer;
		
		for(int k=0;k<5;k++) {
			String fileName=k+".m";
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println("data=[");
			//System.out.println("data=[");

			nFuncs=hashFunctions[k];
		
			for(int i=0; i < n; i++)
			{

				String randStr=generateString();
				for(int j = 0;j<nFuncs;j++) {
					int hash=myHashFunction(randStr,j);
					hash=hash%1000000;
					array[hash]++;
				}
				
			}

			// fazer histograma para o k atual aqui
			
			for(int i=0; i<array.length;i++)
			{
				writer.println(array[i]+(i==array.length-1 ? "];" : ","));
				//System.out.println(array[i]+(i==array.length-1 ? "];" : ","));

			}
			
			writer.close();
		
		}

	}

	/**
	 * Generates an UUID (Universally Unique Identifier)
	 * @return UUID
	 */
	public static String generateString() {
        String uuid = Long.toHexString(Double.doubleToLongBits(Math.random()));
        uuid=uuid.substring(2, uuid.length());
        return uuid;
    }
	/**
	 * Hash Code Function
	 * @param key
	 * @param index
	 * @return hash code
	 */
	public static int myHashFunction(String key, int index)
	{
		int hash;
		String elemento = key;
		elemento=elemento+(index*Math.pow(2, 32)%12345678);	// trying to get sparser hashes
		hash = Math.abs(elemento.hashCode());
		return hash;
	}

}