import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main extends JFrame {
	static Main info;

	private static final long serialVersionUID = 1L;

	public Main(Camera camera, Objeto objeto, Iluminacao lux, char componente, double fator) {
		initUI(camera, objeto, lux, componente, fator);
	}

	private void initUI(Camera camera, Objeto objeto, Iluminacao lux, char componente, double fator) {
		setTitle("Projeto 2 - PG");
		add(new Window(objeto, lux, componente, fator));
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		final Camera camera = null;
		final Objeto objeto = null;
		final Iluminacao lux = null;
		final BufferedReader pCamera = null;
		final BufferedReader pIluminacao = null;
		final BufferedReader pObjeto = null;
		
		JFrame novo = new JFrame();
		novo.setTitle("Entradas");
		novo.setSize(300, 140);
		novo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		novo.setLocationRelativeTo(null);
		novo.setResizable(false);
		novo.setLocation(5, 5);
		
		JPanel panel = new JPanel();
		JButton but = new JButton("Visualizar");
		JLabel cam = new JLabel("Camera");
		JLabel objet = new JLabel("Objeto  ");
		JLabel comp = new JLabel("Componente");
		JLabel fator = new JLabel("Fator");
		final JTextField compTxt = new JTextField(5);
		final JTextField fatorTxt = new JTextField(5);
		final JTextField cameraTxt = new JTextField(20);
		final JTextField objTxt = new JTextField(20);
		
		cameraTxt.setSize(100, 100);
		panel.add(cam);
		panel.add(cameraTxt);
		panel.add(objet);
		panel.add(objTxt);
		panel.add(comp);
		panel.add(compTxt);
		panel.add(fator);
		panel.add(fatorTxt);
		panel.add(but);
		novo.add(panel);

		novo.setVisible(true);

		but.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					botao1(pCamera, pIluminacao, pObjeto, cameraTxt.getText(), objTxt.getText(), compTxt.getText().charAt(0), fatorTxt.getText(), camera, objeto, lux);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	public static void botao1(BufferedReader pCamera,
			BufferedReader pIluminacao, BufferedReader pObjeto,
			String cameraEntrada, String objetoEntrada, char comp, String fator, Camera camera,
			Objeto objeto, Iluminacao lux) throws NumberFormatException, IOException {

		String entradaCamera = "entradas\\Cameras\\" + cameraEntrada + ".cfg";
		String entradaObjeto = "entradas\\Objetos\\" + objetoEntrada + ".byu";

		pCamera = new BufferedReader(new FileReader(entradaCamera));
		pIluminacao = new BufferedReader(new FileReader("entradas\\iluminacao.txt"));
		pObjeto = new BufferedReader(new FileReader(entradaObjeto));
		
		camera = Inputs.getCamera(pCamera);
		lux = Inputs.getIluminacao(camera, pIluminacao);
		objeto = Inputs.getObjeto(camera, pObjeto);

		double fat = Double.parseDouble(fator);
		
		if (info != null) {
			info.dispose();
		}
		
		info = new Main(camera, objeto, lux, comp, fat);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				info.setLocation(300, 0);
				info.setVisible(true);
			}
		});
		
	}

}
