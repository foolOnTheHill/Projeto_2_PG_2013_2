
public class Camera {
	
	Ponto C; // Ponto da câmera (ou foco)
	
	/*Sistema de vista*/
	Ponto V; // eixo oy
	Ponto N; // eixo oz
	Ponto U; // eixo ox (igual a VxN) 
	
	/*Área do sistema de vista*/
	double hx;
	double hy;
	
	double d; // distância do foco até o plano de vista
	
	public Camera(Ponto c, Ponto v, Ponto n, Ponto u, double hx, double hy, double d) {		
		this.C = c;
		this.V = v;
		this.N = n;
		this.U = u;
		this.hx = hx;
		this.hy = hy;
		this.d = d;
	}
	
}
