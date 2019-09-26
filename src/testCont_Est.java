package projeto;

public class testCont_Est {

	public static void main(String[] args) {

		Contador_Est contador1 = new Contador_Est(0);
		Contador_Est contador2 = new Contador_Est(0.3);
		Contador_Est contador3 = new Contador_Est(0.5);
		Contador_Est contador4 = new Contador_Est(0.75);
		Contador_Est contador5 = new Contador_Est(1);

		double media1,media2,media3,media4,media5,valormax2,valormax3,valormax4,valormax5;
		media1=media2=media3=media4=media5=0;
		
		int iter=5;
		double maxcont=5*10000000;

		for (int h = 1; h <= iter; h++) {
			for (int i = 0; i < 1e7; i++) {
				contador1.count();
				contador2.count();
				contador3.count();
				contador4.count();
				contador5.count();
			}
			System.out.print(h+"\n");
		}
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
		System.out.printf("%15s | %6.2f | %11.2f | %11.2f | %11.2f | %11.2f\n", "Media", media1, media2, media3, media4, media5);
		System.out.printf("%15s | %6s | %8.2f | %8.2f | %8.2f | %8.2f\n", "Valor Contagem", valormax1,valormax2,valormax3,valormax4,valormax5);
		System.out.printf("%15s | %6s | %.9f | %.9f | %.9f | %.9f\n", "Erro %", erro1,erro2,erro3,erro4,erro5);

	}

}
