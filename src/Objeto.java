public class Objeto {
	
	Ponto[] visao; // Pontos do objeto em coordenadas de vista
	Ponto[] screen; // Pontos do objeto em coordenadas de tela (i.e., 2D)

	int[][] triangulos;
	
	/*Pré-processamento*/
	Ponto[] normais_triangulos;
	Ponto[] normais_vertices;
	//

	public Objeto(Ponto[] visao, Ponto[] screen, Ponto[] normais_triangulos, Ponto[] normais_vertices, double[][] triangulos) {
		this.visao = visao;
		this.screen = screen;
		this.normais_triangulos = normais_triangulos;
		this.normais_vertices = normais_vertices;
		this.triangulos = triangulos;
	}
	
}
