package src;

/**
 * Classe para desenvolvimento do módulo do 'Contador Estocástico', um contador
 * que só é incrementado com base numa probabilidade.
 */
public class Contador_Est {

	private int count, fator; //* variable declaration for the counter, probability and factor */
	private double prob;

	/**
	 * Um construtor unico que executa o trabalho de dois construtores diferentes. <br>
	 * Caso o valor passado seja menor que 1, estamos a trabalhar com uma probabilidade, caso seja maior que 1 estamos a trabalhar com um fator.
	 */
	public Contador_Est(double prob) {
		if (prob>=0 && prob<=1) {
			this.count = 0;
			this.prob = prob;
			this.fator = 0;
		}
		else if (prob>1) {
			this.count = 0;
			this.fator = (int) Math.round(prob);	
		}
		else {
			System.out.println("Invalido");
			return;
		}

	}
	/**
	 * In case 1, probability, the counter only goes up if a random generated number, r (0<=r<=1), is inferior to the given probability, r<prob <br>
	 * In case 2, factor, the counter goes up if r is inferior to factor**(-counter)
	 * @return the current value of the counter, or -1 if it did not succeed.
	 */
	public int count() {
		double r = Math.random();
		if (fator == 0) {
			if (r < this.prob) {
				this.count++;
				return this.count;
			} else
				return -1;
		}
		else {
			if (r < Math.pow(fator, -this.getCount())) {
				this.count++;
				return this.count;
			}
			else
				return -1;
		}
	}
	/**
	 * reset counter
	 */
	public void clear() {
		this.count = 0;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}

	@Override
	public String toString() {
		return "Contador_Est [count=" + count + ", prob=" + prob + "]";
	}

}