package src;
/**
 * Class for simple testing of the module Contador_Est
 */
public class TestCont_Est {

	public static void main(String[] args) {
		/**
		 * Initialization of several counters with different probabilities
		 */
		Contador_Est contador1 = new Contador_Est(0.0);
		Contador_Est contador2 = new Contador_Est(0.3);
		Contador_Est contador3 = new Contador_Est(0.5);
		Contador_Est contador4 = new Contador_Est(0.75);
		Contador_Est contador5 = new Contador_Est(1.0);

		double media1,media2,media3,media4,media5,valormax2,valormax3,valormax4,valormax5;
		media1=media2=media3=media4=media5=0;
		
		int iter=5;
		double maxcont=iter*1e6;
		/**
		 * We try to increment each different counter 1 million times, and we run this 5 times to be able to calculate the average and the associated theoretical error.
		 */
		for (int h = 1; h <= iter; h++) {
			for (int i = 1; i <= 1e6; i++) {
				contador1.count();
				contador2.count();
				contador3.count();
				contador4.count();
				contador5.count();
			}
			System.out.print(h+"\n");
		}
		System.out.println();
		
		media1=contador1.getCount()/iter;
		media2=contador2.getCount()/iter;
		media3=contador3.getCount()/iter;
		media4=contador4.getCount()/iter;
		media5=contador5.getCount()/iter;
		
		char valormax1='/';
		valormax2=contador2.getCount()/0.3;
		valormax3=contador3.getCount()/0.5;
		valormax4=contador4.getCount()/0.75;
		valormax5=contador5.getCount()/1;
		
		char erro1='/';
		double erro2=(valormax2-maxcont);
		erro2=Math.abs(erro2);
		erro2=erro2/maxcont*100;
		
		double erro3=(valormax3-maxcont);
		erro3=Math.abs(erro3);
		erro3=erro3/maxcont*100;
		
		double erro4=(valormax4-maxcont);
		erro4=Math.abs(erro4);
		erro4=erro4/maxcont*100;
		
		double erro5=(valormax5-maxcont);
		erro5=Math.abs(erro5);
		erro5=erro5/maxcont*100;

		System.out.printf("%15s | %6d | %11.2f | %11.2f | %11.2f | %11d     | %s\n", "Probabilidade %", 0, 0.3, 0.5, 0.75, 1,"5 iterações até 1e7 cada");
		System.out.println();
		System.out.printf("%15s | %6d | %11d | %11d | %11d | %11d\n","Valor Contador",contador1.getCount(), contador2.getCount(),contador3.getCount(),contador4.getCount(),contador5.getCount());
		System.out.printf("%15s | %6.0f | %11.0f | %11.0f | %11.0f | %11.0f\n","Valor Esperado",maxcont*0,maxcont*0.3,maxcont*0.5,maxcont*0.75,maxcont*1);
		System.out.printf("%15s | %6s | %.9f | %.9f | %.9f | %.9f\n", "Erro %",Math.abs(contador1.getCount()-maxcont*0)/(maxcont*0)*100,Math.abs(contador2.getCount()-maxcont*0.3)/(maxcont*0.3)*100,Math.abs(contador3.getCount()-maxcont*0.5)/(maxcont*0.5)*100,Math.abs(contador4.getCount()-maxcont*0.75)/(maxcont*0.75)*100,Math.abs(contador5.getCount()-maxcont*1)/(maxcont*1)*100);
		System.out.println();
		System.out.printf("%15s | %6.2f | %11.2f | %11.2f | %11.2f | %11.2f\n", "Media", media1, media2, media3, media4, media5);
		System.out.printf("%15s | %6.2f | %11.2f | %11.2f | %11.2f | %11.2f\n", "Media teor", 0*1e6, 0.3*1e6, 0.5*1e6, 0.75*1e6, 1*1e6);
		System.out.printf("%15s | %6s | %.9f | %.9f | %.9f | %.9f\n", "Erro %",Math.abs(media1-0*1e6)/(0*1e6)*100,Math.abs(media2-0.3*1e6)/(0.3*1e6)*100,Math.abs(media3-0.5*1e6)/(0.5*1e6)*100,Math.abs(media4-0.75*1e6)/(0.75*1e6)*100,Math.abs(media5-1*1e6)/(1*1e6)*100);
		System.out.println();
		System.out.printf("%15s | %6s | %11.2f | %11.2f | %11.2f | %11.2f\n", "Num Contagens", valormax1,valormax2,valormax3,valormax4,valormax5);
		System.out.printf("%15s | %6s | %.9f | %.9f | %.9f | %.9f\n", "Erro %", erro1,erro2,erro3,erro4,erro5);
		
		System.out.println("---------------------------------------------------------------");
		System.out.println("Ready?");
		System.in.read();
		
		/**
		 * Now we test the exponencial counter with a factor of 3, seeing how the probability of the counter actually incrementing decreases very quickly.
		*/
		Contador_Est exp=new Contador_Est(3);
		for (int i = 0; i < 100; i++) {
			exp.count();
			System.out.println(exp.getCount());
		}
	}

}