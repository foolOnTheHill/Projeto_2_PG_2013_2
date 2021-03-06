import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.JPanel;


public class Window extends JPanel {

	private static final long serialVersionUID = 1L;

	/*Informações da cena*/
	private Objeto objeto;
	private Iluminacao iluminacao;

	/*Alg. do Pintor*/
	private double[][] zbuffer;
	private Graphics2D graphics;

	/*Textura*/
	private char componente; // Componente que será 'perturbado'
	private double fator; // Fator de aleatorização

	public Window(Objeto objeto, Iluminacao iluminacao, char componente, double fator) {
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

			a = coords[0]*P1.x + coords[1]*P2.x + coords[2]*P3.x;
			b = coords[0]*P1.y + coords[1]*P2.y + coords[2]*P3.y;
			c = coords[0]*P1.z + coords[1]*P2.z + coords[2]*P3.z;

			P = new Ponto(a, b, c);

			/* Consulta o z-buffer*/
			if (x >= 0 && y >= 0 && x < zbuffer.length && y < zbuffer[0].length && P.z > 0 && P.z < zbuffer[(int) x][(int) y]) {
				zbuffer[(int) x][(int) y] = P.z;

				/*Normais dos vértices do triângulo*/
				N1 = objeto.normais_vertices[A.id];
				N2 = objeto.normais_vertices[B.id];
				N3 = objeto.normais_vertices[C.id];

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

		double aux = escalarLNormal;

		/*Vetor R*/
		Ponto R = new Ponto(2*aux*normal.x - L.x, 2*aux*normal.y - L.y, 2*aux*normal.z - L.z);

		/*Vetor V*/
		Ponto V = new Ponto(-pixel.x, -pixel.y, -pixel.z);
		Algebra.normalizar(V);

		double escalarVR = Algebra.produtoEscalar(V, R);

		/*Componente Especular*/
		if (escalarVR > 0) {
			double rugosidade = iluminacao.ks*Math.pow(escalarVR, iluminacao.n);
			cor[0] += iluminacao.Il[0]*rugosidade;
			cor[1] += iluminacao.Il[1]*rugosidade;
			cor[2] += iluminacao.Il[2]*rugosidade;
		}

		cor[0] = Math.min(cor[0], 255);
		cor[1] = Math.min(cor[1], 255);
		cor[2] = Math.min(cor[2], 255);

		//System.out.println(cor[0] + " " + cor[1] + " " + cor[2]);

		return cor;
	}

	/* Scanline - Algoritmo de Preenchimento
	 * Referências:
	 * - http://www.cmpe.boun.edu.tr/~sahiner/cmpe460web/FALL2009/scanlinefill.pdf */
	private void scanline(Graphics g) {

		this.graphics = (Graphics2D) g;

		Dimension d = getSize();
		Insets i = getInsets();
		
		int v_height = d.height - i.top - i.bottom;
		int v_width = d.width - i.left - i.right;

		updateCoords(v_width, v_height);
		initZ_Buffer(v_width, v_height);

		for (int k = 0; k < objeto.triangulos.length; k++) {
			double xMin, xMax, yMin, yMax;
			double a1, a2, a3;
			Ponto[] P = new Ponto[3];
			Ponto A, B, C;
			boolean changed = false;

			P[0] = objeto.screen[objeto.triangulos[k][0]];
			P[1] = objeto.screen[objeto.triangulos[k][1]];
			P[2] = objeto.screen[objeto.triangulos[k][2]];

			Arrays.sort(P);

			A = P[0];

			int orient = Algebra.orientacao(A.x, A.y, P[1].x, P[1].y, P[2].x, P[2].y);

			if (orient < 0) { /*Sentido horário*/
				B = P[1];
				C = P[2];
			} else if (orient > 0) { /*Sentido anti-horário*/
				B = P[2];
				C = P[1];
			} else if (P[1].x < A.x && P[2].x < A.x) {
				B = P[1];
				C = P[2];
			} else if (P[1].x > A.x && P[2].x > A.x) {
				B = P[2];
				C = P[1];
			} else if (P[1].x < P[2].x) {
				B = P[1];
				C = P[2];
			} else {
				B = P[2];
				C = P[1];
			}

			if (Algebra.isTriangle(A, B, C)) {
				yMax = P[2].y;
				yMin = A.y;

				a1 = ((double) ((int) B.y - (int) A.y) / (double) ((int) B.x - (int) A.x));
				a2 = ((double) ((int) C.y - (int) A.y) / (double) ((int) C.x - (int) A.x));
				a3 = ((double) ((int) C.y - (int) B.y) / (double) ((int) C.x - (int) B.x));
				
				xMin = xMax = A.x;

				if (Math.abs(A.y - B.y) == 0) {
					xMin = Math.min(A.x, B.x);
					xMax = Math.max(A.x, B.x);
					a1 = a3;
				} else if (Math.abs(A.y - C.y) == 0) {
					changed = true;
					xMin = Math.min(A.x, C.x);
					xMax = Math.max(A.x, C.x);
					a2 = a3;
				}

				for (int y = (int) yMin; y <= (int) yMax; y++) {
					
					pintor(A, B, C, xMin, xMax, y);
					
					if (!changed && (y == (int) B.y || y == (int) C.y)) {
						if (Math.abs(y - B.y) == 0) {
							a1 = a3;
						} else {
							a2 = a3;
						}
						changed = true;
					}
					
					if (a1 != Double.POSITIVE_INFINITY && a1 != Double.NEGATIVE_INFINITY && a1 != 0 && a1 != Double.NaN) {
						xMin += 1 / a1;
					}

					if (a2 != Double.POSITIVE_INFINITY && a2 != Double.NEGATIVE_INFINITY && a2 != 0 && a2 != Double.NaN) {
						xMax += 1 / a2;
					}
				}
			}
		}	
	}

	/*Inicializa o Z-Buffer*/
	private void initZ_Buffer(int width, int height) {
		this.zbuffer = new double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.zbuffer[i][j] = Double.MAX_VALUE;
			}
		}
	}

	/*Coloca as coordenadas dos pontos em Coordenadas de Tela*/
	private void updateCoords(int width, int height) {
		for (int i = 0; i < objeto.screen.length; i++) {
			objeto.screen[i].x = (int) ((objeto.screen[i].x + 1) * width / 2);
			objeto.screen[i].y = (int) ((1 - objeto.screen[i].y) * height / 2);
		}
	}
	
	/*Tentativa de gerenciar melhor o uso de memória.*/
	public void free() throws Throwable {
		finalize();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		scanline(g);
	}

}

