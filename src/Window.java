import java.awt.Color;
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
	
	/*Textura*/
	private char componente; // Componente que será 'perturbado'
	private double fator; // Fator de aleatorização
	
	public Window(Camera camera, Objeto objeto, Iluminacao iluminacao, char componente, double fator) {
		this.camera = camera;
		this.objeto = objeto;
		this.iluminacao = iluminacao;
		this.componente = componente;
		this.fator = fator;
	}
	
	private void pintor(Ponto A, Ponto B, Ponto C, double xMin, double xMax, double y) {
		double a, b, c;
		double[] coords;
		
		Ponto P1, P2, P3, P;
		Ponto N1, N2, N3, N;
		Ponto V;
		
		double[] color;
		
		Ponto pixel;
		
		for (int x = (int) Math.round(xMin); x <= Math.round(xMax); x++) {
			
			pixel = new Ponto(x, y, 0); // Ponto dentro do triângulo que vai ser pintado
			
			/* Calcula o pixel em coordenadas de visão */
			coords = Algebra.coordenadasBaricentricas(A, B, C, pixel);
			
			P1 = objeto.visao[A.id];
			P2 = objeto.visao[B.id];
			P3 = objeto.visao[C.id];
			
			a = coords[0]*(P1.x + P2.x + P3.x);
			b = coords[1]*(P1.y + P2.y + P3.y);
			c = coords[2]*(P1.z + P2.z + P3.z);
			
			P = new Ponto(a, b, c);
			
			/* Consulta o z-buffer*/
			if (x >= 0 & y >= 0 & x < zbuffer.length & y < zbuffer[0].length & P.z > 0 & P.z < zbuffer[(int) x][(int) y]) {
				zbuffer[(int) x][(int) y] = P.z;
				
				/*Normais dos vértices do triângulo*/
				N1 = objeto.normais_triangulos[A.id];
				N2 = objeto.normais_triangulos[B.id];
				N3 = objeto.normais_triangulos[C.id];
			
				/*Normal do ponto atual*/
				a = coords[0]*N1.x + coords[1]*N2.x + coords[2]*N3.x;
				b = coords[0]*N1.y + coords[1]*N2.y + coords[2]*N3.y;
				c = coords[0]*N1.z + coords[1]*N2.z + coords[2]*N3.z;
				
				N = new Ponto(a, b, c);
				Algebra.normalizar(N);
				
				V = new Ponto(-P.x, -P.y, -P.z);
				
				if (Algebra.produtoEscalar(N, V) < 0) { /*Garantir que aponta na direção do observador*/
					N = new Ponto(-N.x, -N.y, -N.z);
				}
				
				/*Determina a cor do pixel*/
				color = phong(P, N);
				
				/*Pinta o pixel*/
				graphics.setColor(new Color((int) color[0], (int)color[1], (int)color[2]));
				graphics.drawLine((int) x, (int) y, (int) x, (int) y);
			}
			
		}
		
	}
	
	/* Modelo de Iluminação de Phong. 
	* Referências: 
	* - http://titan.cs.ukzn.ac.za/opengl/opengl-d7/notes/lect14.pdf
	* - http://www.uio.no/studier/emner/matnat/ifi/INF3320/h03/undervisningsmateriale/lecture5.pdf */
	private double[] phong(Ponto pixel, Ponto normal) {
		
		double[] cor = new double[3];
		
		double ka = iluminacao.ka;
		
		/*Componente do Ambiente*/
		cor[0] = ka*iluminacao.Ia[0];
		cor[1] = ka*iluminacao.Ia[1];
		cor[2] = ka*iluminacao.Ia[2];
		
		/*Vetor L*/
		Ponto L = new Ponto(iluminacao.Pl.x - pixel.x, iluminacao.Pl.y - pixel.y, iluminacao.Pl.z - pixel.z);
		Algebra.normalizar(L);
		
		/*Componente difusa*/
		double escalarLNormal = Algebra.produtoEscalar(L, normal);
		
		if (escalarLNormal > 0) {
			double textura = Math.random()*fator + (1-fator); // Fator de atenuação entre [1-f, 1]	
			
			/*Altera o componente correspondente em Od para produzir a textura*/
			switch (componente) {
			case 'R':
				cor[0] += iluminacao.Od[0]*iluminacao.Il[0]*iluminacao.kd*escalarLNormal*textura;
				cor[1] += iluminacao.Od[1]*iluminacao.Il[1]*iluminacao.kd*escalarLNormal;
				cor[2] += iluminacao.Od[2]*iluminacao.Il[2]*iluminacao.kd*escalarLNormal;
				break;
			case 'G':
				cor[0] += iluminacao.Od[0]*iluminacao.Il[0]*iluminacao.kd*escalarLNormal;
				cor[1] += iluminacao.Od[1]*iluminacao.Il[1]*iluminacao.kd*escalarLNormal*textura;
				cor[2] += iluminacao.Od[2]*iluminacao.Il[2]*iluminacao.kd*escalarLNormal;
				break;
			case 'B':
				cor[0] += iluminacao.Od[0]*iluminacao.Il[0]*iluminacao.kd*escalarLNormal;
				cor[1] += iluminacao.Od[1]*iluminacao.Il[1]*iluminacao.kd*escalarLNormal;
				cor[2] += iluminacao.Od[2]*iluminacao.Il[2]*iluminacao.kd*escalarLNormal*textura;
				break;
			}
		}
		
		double aux = escalarLNormal / Algebra.produtoEscalar(normal, normal);
		
		/*Vetor R*/
		Ponto R = new Ponto(2*aux*normal.x - L.x, 2*aux*normal.y - L.y, 2*aux*normal.z - L.z);
		Algebra.normalizar(R);
		
		/*Vetor V*/
		Ponto p = new Ponto(-pixel.x, -pixel.y, -pixel.z);
		Algebra.normalizar(p);
		
		double escalarRp = Algebra.produtoEscalar(p, R);
		
		/*Componente Especular*/
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
	
	/* Scanline - Algoritmo de Preenchimento
	* Referências:
	* - http://www.cmpe.boun.edu.tr/~sahiner/cmpe460web/FALL2009/scanlinefill.pdf */
	private void scanline(Graphics g) {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		scanline(g);
	}
	
}
		
