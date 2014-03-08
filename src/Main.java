import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main extends JFrame {
	static Main info;

	private static Camera camera;
	private static Objeto objeto;
	private static Iluminacao lux;
	private static BufferedReader pCamera;
	private static BufferedReader pIluminacao;
	private static BufferedReader pObjeto;
	private static JPanel panel;
	private static JButton but;
	private static JLabel cam;
	private static JLabel objet;
	private static JLabel comp;
	private static JLabel fator;
	private static JTextField compTxt;
	private static JTextField fatorTxt;
	private static JTextField cameraTxt;
	private static JTextField objTxt;
	private static JFrame novo;
	
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
		camera = null;
		objeto = null;
		lux = null;
		pCamera = null;
		pIluminacao = null;
		pObjeto = null;
		
		novo = new JFrame();
		novo.setTitle("Configurações");
		novo.setSize(300, 140);
		novo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		novo.setLocationRelativeTo(null);
		novo.setResizable(false);
		novo.setLocation(5, 5);
		
		panel = new JPanel();
		but = new JButton("Visualizar");
		cam = new JLabel("Camera");
		objet = new JLabel("Objeto  ");
		comp = new JLabel("Componente");
		fator = new JLabel("Fator");
		
		compTxt = new JTextField(5);
		fatorTxt = new JTextField(5);
		cameraTxt = new JTextField(20);
		objTxt = new JTextField(20);
		
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
		novo.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		but.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				double f;
				
				try {
					f = Double.parseDouble(fatorTxt.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Digite um fator válido! (entre 0 e 1)");
					return;
				}
				
				botao1(pCamera, pIluminacao, pObjeto, cameraTxt.getText(), objTxt.getText(), f, camera, objeto, lux);
			}
		});

	}

	public static void botao1(BufferedReader pCamera, BufferedReader pIluminacao, BufferedReader pObjeto, String cameraEntrada, String objetoEntrada, double fator, Camera camera, Objeto objeto, Iluminacao lux) {

		String entradaCamera = "entradas\\Cameras\\" + cameraEntrada + ".cfg";
		String entradaObjeto = "entradas\\Objetos\\" + objetoEntrada + ".byu";

		try {
			pCamera = new BufferedReader(new FileReader(entradaCamera));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "O Arquivo da câmera não foi encontrado!");
			return;
		}
		
		try {
			pIluminacao = new BufferedReader(new FileReader("entradas\\iluminacao.txt"));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "O Arquivo da iluminação não foi encontrado!");
			return;
		}
		
		try {
			pObjeto = new BufferedReader(new FileReader(entradaObjeto));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "O Arquivo do objeto não foi encontrado!");
			return;
		}
		
		char comp;
		
		if (compTxt.getText().equals("") || compTxt.getText().length() > 1 || (compTxt.getText().toUpperCase().charAt(0) != 'R' && compTxt.getText().toUpperCase().charAt(0) != 'G' && compTxt.getText().toUpperCase().charAt(0) != 'B')) {
			JOptionPane.showMessageDialog(null, "Informe o componente que deve ser perturbado! (R, G, ou B)");
			return;
		} else if (cameraTxt.getText().equals("") || objTxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Informe os nomes dos arquivos da cÃ¢mera e do objeto!");
			return;
		} 
		
		comp = compTxt.getText().toUpperCase().charAt(0);
		
		try {
			camera = Inputs.getCamera(pCamera);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo da câmera!");
			return;
		}
		
		try {
			lux = Inputs.getIluminacao(camera, pIluminacao);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo da iluminação!");
			return;
		}
		
		try {
			objeto = Inputs.getObjeto(camera, pObjeto);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo do objeto!");
			return;
		}
		
		if (info != null) {
			info.dispose();
		}
				
		info = new Main(camera, objeto, lux, comp, fator);
		info.setLocation(300, 0);
		info.setVisible(true);
		
	}

}
