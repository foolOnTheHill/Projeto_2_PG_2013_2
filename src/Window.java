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
		return null;
	}
	
	private void pintor(Graphics g) {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		pintor(g);
	}
	
}
