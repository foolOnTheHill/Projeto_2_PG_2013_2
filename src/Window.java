import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;


public class Window extends JPanel {

	private static final long serialVersionUID = 1L;

	/*Informações da cena*/
	private Camera camera;
	private Objeto objeto;
	private Iluminacao iluminacao;
	
	/*Alg. do Pintor*/
	private double[][] zbuffer;
	private Graphics2D graphics;
	
	public Window(Camera camera, Objeto objeto, Iluminacao iluminacao) {
		this.camera = camera;
		this.objeto = objeto;
		this.iluminacao = iluminacao;
	}
	
	private void scanline(Ponto A, Ponto B, Ponto C, double xMin, double xMax, double y) {
		
	}
	
	private double[] phong(Ponto pixel, Ponto normal) {
		
		double[] cor = new double[3];
		
		double ka = iluminacao.ka;
		
		cor[0] = ka*iluminacao.Ia[0];
		cor[1] = ka*iluminacao.Ia[1];
		cor[2] = ka*iluminacao.Ia[2];
		
		Ponto L = new Ponto(iluminacao.Pl.x - pixel.x, iluminacao.Pl.y - pixel.y, iluminacao.Pl.z - pixel.z);
		Algebra.normalizar(L);
		
		double escalarLNormal = Algebra.produtoEscalar(L, normal);
		
		if (escalarLNormal > 0) {
			cor[0] += iluminacao.Od[0]*iluminacao.Il[0]*iluminacao.kd*escalarLNormal;
			cor[1] += iluminacao.Od[1]*iluminacao.Il[1]*iluminacao.kd*escalarLNormal;
			cor[2] += iluminacao.Od[2]*iluminacao.Il[2]*iluminacao.kd*escalarLNormal;
		}
		
		double aux = escalarLNormal / Algebra.produtoEscalar(normal, normal);
		
		Ponto R = new Ponto(2*aux*normal.x - L.x, 2*aux*normal.y - L.y, 2*aux*normal.z - L.z);
		Algebra.normalizar(R);
		
		Ponto p = new Ponto(-pixel.x, -pixel.y, -pixel.z);
		Algebra.normalizar(p);
		
		double escalarRp = Algebra.produtoEscalar(p, R);
		
		if (escalarRp > 0) {
			double rugosidade = iluminacao.ks*Math.pow(escalarRp, iluminacao.n);
			cor[0] += iluminacao.Il[0]*rugosidade;
			cor[1] += iluminacao.Il[1]*rugosidade;
			cor[2] += iluminacao.Il[2]*rugosidade;
		}
		
		cor[0] = Math.max(cor[0], 255);
		cor[1] = Math.max(cor[1], 255);
		cor[2] = Math.max(cor[2], 255);
		
		return cor;
	}
	
	private void pintor(Graphics g) {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		pintor(g);
	}
	
}
	
