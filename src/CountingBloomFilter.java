

import java.util.Arrays;
/**
 * This class implements a simple Counting Bloom Filter.
 * @param <T> -> use of generics.
 */
public class CountingBloomFilter<T> {

	private int size, nFuncs;
	private int[] array;
	/**
	 * This single constructor implements 3 different ones according to the used flag.
	 * @param size_or_fp -> stores either the size of the array of the bloom filter or the "fp" parameter (used to calculate optimal size of the array).
	 * @param nFuncs_or_m -> stores either the number of different hash functions being used or the "m" parameter (used to calculate optimal number of hash functions).
	 * @param flag -> determines the type of constructor, what each parameter refers to.
	 */
	public CountingBloomFilter(double size_or_fp, int nFuncs_or_m,int flag) {
		if (flag==1) {
			this.size=(int) size_or_fp;
			this.array=new int[size];
			this.nFuncs=nFuncs_or_m;
		}
		else if (flag==2) {
			this.size = (int) size_or_fp;
			this.array = new int[size];
			
			double opt_k=Math.log(2)*size/nFuncs_or_m;
			int opt_k_i=(int) Math.round(opt_k);
			
			this.nFuncs = opt_k_i;
		}
		else if (flag>2) { //flag and nhf at the same time
			double opt_n=((nFuncs_or_m*Math.log(size_or_fp))/(Math.log(2)*Math.log(0.5)));
			int opt_n_i=(int) Math.round(opt_n);
			
			this.size=opt_n_i;
			this.array=new int[size];
			this.nFuncs=flag;
		}
		else {
			System.out.println("Flag Inválida");
			return;
		}

	}
	/**
	 * Insert element into the counting bloom filter, uses k different hash functions assigned in the constructor.
	 * @param elemento -> element to insert.
	 */
	public void insert(T elemento) {
		String key = elemento.toString();
		for (int i = 0; i < nFuncs; i++) {
			key = key + i;
			int hash = key.hashCode();
			int index = Math.abs(hash % size);
			// System.out.println("elemento: " + elemento + "; hash: "+hash);
			array[index]++;
		}
	}
	/**
	 * Delete element from the CBF
	 * @param elemento -> element to delete from the CBF
	 * @return 1 if element existed and was successfully deleted, else -1
	 */
	public int delete(T elemento) {
		if (this.isMember(elemento)) {
			String key = elemento.toString();
			for (int i = 0; i < nFuncs; i++) {
				key = key + i;
				int hash = key.hashCode();
				int index = Math.abs(hash % size);
				array[index]--;
			}
			return 1;
		} else {
			return -1;
		}
	}
	/**
	 * Boolean function to check if an element exists in the CBF
	 * @param elemento -> element to check
	 * @return boolean value (true if it exists, else false)
	 */
	public boolean isMember(T elemento) {
		String key = elemento.toString();
		for (int i = 0; i < nFuncs; i++) {
			key = key + i;
			int hash = key.hashCode();
			int index = Math.abs(hash % size);
			if (array[index] == 0) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Function used to count number of times said element appears in the CBF
	 * @param element to check
	 * @return counter
	 */
	public int count(T elemento) {
		int returnThis = 0;
		String key = elemento.toString();
		for (int i = 0; i < nFuncs; i++) {
			key = key + i;
			int hash = key.hashCode();
			int index = Math.abs(hash % size);
			if (array[index] < returnThis || i == 0) {
				returnThis = array[index];
			}
		}
		return returnThis;

	}
	/**
	 * Function that generates the hash code for each inserted element
	 * @param key
	 * @return hash
	 */
	public int newHashCode(String key) {
		int hash = 1;
		for (int i = 0; i < key.length(); i++) {
			hash = hash * (key.length() - i) + (int) key.charAt(i);
		}
		return Math.abs(hash);
	}

	public int getSize() {
		return size;
	}

	public int getnFuncs() {
		return nFuncs;
	}

	public int[] getArray() {
		return array;
	}

	@Override
	public String toString() {
		return "CountingBloomFilter->array=" + Arrays.toString(array) + "]";
	}
	
}
