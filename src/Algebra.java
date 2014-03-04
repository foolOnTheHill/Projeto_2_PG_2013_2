
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
		double j = (s.z*t.z) - (s.x*t.z);
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
	
	public static double area(Ponto A, Ponto B, Ponto C) {
		double s = A.x*(B.y - C.y) + B.x*(C.y - A.y) + C.x*(A.y - B.y);
		double r = Math.abs(s)/2;
		return r;
	}
	
	/*Auxiliar p/ o Scanline
	* Referências:
	* - http://www.cmpe.boun.edu.tr/~sahiner/cmpe460web/FALL2009/scanlinefill.pdf */
	public static int orientacao(double xA, double yA, double xB, double yB, double xC, double yC) {
		
		if (Math.round(xB - xA) == 0) {
			
			if (xC < xB) {
				if (yB > yA) {
					return 1;
				} else {
					return -1;
				}
			} else if (xC > xB) {
				if (yB > yA) {
					return -1;
				} else {
					return 1;
				}
			}
			
			return 0;
		} else if (Math.round(yB - yA) == 0) {
			
			if (yC < yB) {
				if (xB > xA) {
					return -1;
				} else {
					return 1;
				}
			} else if (yC > yB) {
				if (xB > xA) {
					return 1;
				} else {
					return -1;
				}
			}
			
			return 0;
		}
		
		double tang = (yB - yA)/(xB - xA);
		double y = yA - (tang*xA);
		double comp = y + (tang*xC);
		
		if (tang != 0) {
			if (yC > comp) {
				if (xB > xA) {
					return 1;
				} else {
					return -1;
				}
			} else if (yC < comp) {
				if (xB > xA) {
					return -1;
				} else {
					return 1;
				}
			}
		}
		
		return 0;
	}
	
}
