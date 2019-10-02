package src;

import java.util.Arrays;

public class CountingBloomFilter<T> { // mudei tudo para newhashcode, n resultou como tu disseste, nas percentagens,
										// weird af, voltei a mudar
	// generico

	private int size, nFuncs;
	private int[] array;

	public CountingBloomFilter(double size_or_fp, int nFuncs_or_m,int flag) { //new Construc
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
		else if (flag>2) { //aqui a flag � o nHashfunc para nao usar mais argumentos
			double opt_n=((nFuncs_or_m*Math.log(size_or_fp))/(Math.log(2)*Math.log(0.5)));
			int opt_n_i=(int) Math.round(opt_n);
			
			this.size=opt_n_i;
			this.array=new int[size];
			this.nFuncs=flag;
		}
		else {
			System.out.println("Flag Inv�lida");
			return;
		}

	}

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
