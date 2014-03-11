class Ponto implements Comparable<Ponto> {
	double x, y, z;
	int id;
	
	public Ponto (double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int compareTo(Ponto p) {
		return (int) (y - p.y);
	}
	
}

public class Algebra {
	
	public static double norma(Ponto p) {
		double s = (p.x*p.x) + (p.y*p.y) + (p.z*p.z);
		return Math.sqrt(s);
	}
	
	public static double produtoEscalar(Ponto s, Ponto t) {
		double r = (s.x*t.x) + (s.y*t.y) + (s.z*t.z);
		return r;
	}
	
	public static void normalizar(Ponto p) {
		double norma = norma(p);
		p.x = p.x / norma;
		p.y = p.y / norma;
		p.z = p.z / norma;
	}
	
	public static Ponto produtoVetorial(Ponto s, Ponto t) {
		double i = (s.y*t.z) - (s.z*t.y);
		double j = (s.z*t.x) - (s.x*t.z);
		double k = (s.x*t.y) - (s.y*t.x);
		
		return new Ponto(i, j, k);
	}

	public static double[] coordenadasBaricentricas(Ponto A, Ponto B, Ponto C, Ponto p) {
		double xA = A.x, yA = A.y;
		double xB = B.x, yB = B.y;
		double xC = C.x, yC = C.y;
		
		double x = p.x, y = p.y;
		
		double a_b_c[] = new double[3];
		double mult = (xA - xC)*(yB - yC) - (yA - yC)*(xB - xC);
		
		a_b_c[0] = ((yB - yC)*(x - xC) - (xC - xB)*(yC - y))/mult;
		a_b_c[1] = ((yC - yA)*(x - xC) - (xA - xC)*(yC - y))/mult;
		a_b_c[2] = 1 - a_b_c[0] - a_b_c[1];
		
		return a_b_c;
	}
	
	/*Auxiliar p/ o Scanline
	* Referências: Usa o Produto Vetorial no R2 para determinar a orientação dos 3 pontos.
	* - http://stackoverflow.com/questions/3461453/determine-which-side-of-a-line-a-point-lies */
	public static int orientacao(double xA, double yA, double xB, double yB, double xC, double yC) {
		return (int) ((xB-xA)*(yC-yA) - (yB-yA)*(xC-xA));
	}
	
	public static boolean isTriangle(Ponto A, Ponto B, Ponto C) {
		return !(((int)A.x == (int)B.x && (int)A.y == (int)B.y) || ((int)A.x == (int)C.x && (int)A.y == (int)C.y) || ((int)B.x == (int)C.x && (int)B.y == (int)C.y));
	}
	
}
