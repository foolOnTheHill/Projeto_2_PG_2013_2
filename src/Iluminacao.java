
public class Iluminacao {
	
	double ka; // Reflexão ambiental
	double kd; // Constante difusa
	double ks; // Parte especular
	double n; // Constante de rugosidade
	
	Ponto Pl; // Coordenadas da fonte de luz
	double[] Ia; // Vetor da cor ambiental
	double[] Od; // Vetor difuso
	double[] Il; // Cor da fonte de luz (em RGB)
	
	public Iluminacao(double ka, double kd, double ks, double n, Ponto pl, double[] ia, double[] od, double[] il) {
		this.ka = ka;
		this.kd = kd;
		this.ks = ks;
		this.n = n;
		this.Pl = pl;
		this.Ia = ia;
		this.Od = od;
		this.Il = il;
	}
	
}
