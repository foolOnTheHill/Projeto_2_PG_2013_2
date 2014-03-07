import java.io.BufferedReader;
import java.io.IOException;


public class Inputs {
	
	public static Camera getCamera(BufferedReader camera) throws IOException {
		Ponto C, V, N, U;
		double d = 0, hx = 0, hy = 0, norma;
		String x, y, z, linha;
		String[] temp;

		if (camera.ready()) {
			
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			C = new Ponto(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
			
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			N = new Ponto(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
			
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			V = new Ponto(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
			
			linha = camera.readLine();
			temp = linha.split(" ");
			d = Double.valueOf(temp[0]);
			hx = Double.valueOf(temp[1]);
			hy = Double.valueOf(temp[2]);

			camera.close();

			norma = Math.sqrt(Algebra.produtoEscalar(N, N));
			N.x = N.x / norma;
			N.y = N.y / norma;
			N.z = N.z / norma;
			
			double coef = (Algebra.produtoEscalar(V, N) / Algebra.produtoEscalar(N, N));

			V.x = V.x - (coef * N.x);
			V.y = V.y - (coef * N.y);
			V.z = V.z - (coef * N.z);
			
			norma = Math.sqrt(Algebra.produtoEscalar(V, V));
			V.x = V.x / norma;
			V.y = V.y / norma;
			V.z = V.z / norma;
			
			U = Algebra.produtoVetorial(N, V);

			Camera cam = new Camera(C, V, N, U, hx, hy, d);

			return cam;
		} else {
			System.out.println("Erro ao ler arquivo!");
			return null;
		}

	}
	
	public static Iluminacao getIluminacao(Camera camera, BufferedReader iluminacao) throws NumberFormatException, IOException {
		String x, y, z, linha, temp[];
		
		if (iluminacao.ready()) {
			Ponto pl;
			double ka, kd, ks, n;
			double[] ia = new double[3];
			double[] od = new double[3];
			double[] il = new double[3];
			
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			
			pl = new Ponto(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
			
			linha = iluminacao.readLine();
			ka = Double.valueOf(linha);
			
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			
			ia[0] = Double.valueOf(x);
			ia[1] = Double.valueOf(y);
			ia[2] = Double.valueOf(z);
			
			linha = iluminacao.readLine();
			kd = Double.valueOf(linha);
			
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			
			od[0] = Double.valueOf(x);
			od[1] = Double.valueOf(y);
			od[2] = Double.valueOf(z);
			
			linha = iluminacao.readLine();
			ks = Double.valueOf(linha);
			
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			
			il[0] = Double.valueOf(x);
			il[1] = Double.valueOf(y);
			il[2] = Double.valueOf(z);
			
			linha = iluminacao.readLine();
			n = Double.valueOf(linha);

			pl.x -= camera.C.x;
			pl.y -= camera.C.y;
			pl.z -= camera.C.z;

			double x_visao = (pl.x * camera.U.x) + (pl.y * camera.U.y) + (pl.z * camera.U.z);
			double y_visao = (pl.x * camera.V.x) + (pl.y * camera.V.y) + (pl.z * camera.V.z);
			double z_visao = (pl.x * camera.N.x) + (pl.y * camera.N.y) + (pl.z * camera.N.z);

			pl.x = x_visao;
			pl.y = y_visao;
			pl.z = z_visao;
			
			Iluminacao ilum = new Iluminacao(ka, kd, ks, n, pl, ia, od, il);
			return ilum;
		} else {
			System.out.println("Erro ao abrir a iluminacao");
			return null;
		}
	}
	
	public static Objeto getObjeto(Camera camera, BufferedReader objeto) throws IOException {

		Ponto pontos_visao [] = null, pontos[] = null;
		int qnt_pontos, qnt_triangulos, triangulos[][] = null;
		Ponto normais_triangulos[] = null, normais_vertices[] = null;
		double left, right, up, down, next, far;
		String x, y, z, linha, temp[];
		if (objeto.ready()) {
			
			linha = objeto.readLine();
			qnt_pontos = Integer.valueOf(linha.substring(0, linha.indexOf(' ')));
			qnt_triangulos = Integer.valueOf(linha.substring(linha.indexOf(' ') + 1));

			pontos = new Ponto[qnt_pontos];
			pontos_visao = new Ponto[qnt_pontos];
			triangulos = new int[qnt_triangulos][3];
			
			for (int i = 0; i < qnt_pontos; i++) {
				linha = objeto.readLine();
				temp = linha.split(" ");
				if (linha.charAt(0) == ' ') {
					x = temp[1];
					y = temp[2];
					z = temp[3];
				} else {
					x = temp[0];
					y = temp[1];
					z = temp[2];
				}
				pontos[i] = new Ponto(Double.valueOf(x), Double.valueOf(y),	Double.valueOf(z));
				pontos[i].id = i;
			}
			
			for (int i = 0; i < qnt_triangulos; i++) {
				do {
					linha = objeto.readLine();
				} while (linha.isEmpty());

				temp = linha.split(" ");
				if (linha.charAt(0) == ' ') {
					x = temp[1];
					y = temp[2];
					z = temp[3];
				} else {
					x = temp[0];
					y = temp[1];
					z = temp[2];
				}
				triangulos[i][0] = Integer.valueOf(x) - 1;
				triangulos[i][1] = Integer.valueOf(y) - 1;
				triangulos[i][2] = Integer.valueOf(z) - 1;
			}

			for (int i = 0; i < qnt_pontos; i++) {
				pontos[i].x -= camera.C.x;
				pontos[i].y -= camera.C.y;
				pontos[i].z -= camera.C.z;

				double x_visao = (pontos[i].x * camera.U.x) + (pontos[i].y * camera.U.y) + (pontos[i].z * camera.U.z);
				double y_visao = (pontos[i].x * camera.V.x) + (pontos[i].y * camera.V.y) + (pontos[i].z * camera.V.z);
				double z_visao = (pontos[i].x * camera.N.x) + (pontos[i].y * camera.N.y) + (pontos[i].z * camera.N.z);

				pontos_visao[i] = new Ponto(x_visao, y_visao, z_visao);
				pontos_visao[i].id = i;
			}

			left = pontos_visao[0].x;
			right = left;
			up = pontos_visao[0].y;
			down = up;
			next = pontos_visao[0].z;
			far = next;

			for (int i = 0; i < qnt_pontos; i++) {
				if (pontos_visao[i].x < left) {
					left = pontos_visao[i].x;
				} else if (pontos_visao[i].x > right) {
					right = pontos_visao[i].x;
				} else if (pontos_visao[i].y < down) {
					down = pontos_visao[i].y;
				} else if (pontos_visao[i].y > up) {
					up = pontos_visao[i].y;
				} else if (pontos_visao[i].z < next) {
					next = pontos_visao[i].z;
				} else if (pontos_visao[i].z > far) {
					far = pontos_visao[i].z;
				}
			}	

			for (int i = 0; i < qnt_pontos; i++) {
				double x_temp, y_temp, z_temp;
				
				x_temp = pontos_visao[i].x;
				y_temp = pontos_visao[i].y;
				z_temp = pontos_visao[i].z;

				pontos[i].x = (camera.d / camera.hx) * (x_temp / z_temp);
				pontos[i].y = (camera.d / camera.hy) * (y_temp / z_temp);
				pontos[i].z = 0;
				
				pontos[i].id = i;
			}

			objeto.close();

			Ponto v1 = new Ponto(0.0, 0.0, 0.0), v2 = new Ponto(1.0, 1.0, 1.0);
			normais_triangulos = new Ponto[qnt_triangulos];
			normais_vertices = new Ponto[qnt_pontos];

			for (int i = 0; i < qnt_pontos; i++) {
				normais_vertices[i] = new Ponto(0.0, 0.0, 0.0);
			}

			for (int i = 0; i < qnt_triangulos; i++) {
				Ponto p1 = pontos_visao[triangulos[i][0]];
				Ponto p2 = pontos_visao[triangulos[i][1]];
				Ponto p3 = pontos_visao[triangulos[i][2]];

				v1.x = p2.x - p1.x;
				v1.y = p2.y - p1.y;
				v1.z = p2.z - p1.z;
				
				v2.x = p3.x - p1.x;
				v2.y = p3.y - p1.y;
				v2.z = p3.z - p1.z;

				normais_triangulos[i] = Algebra.produtoVetorial(v1, v2);
				
				Algebra.normalizar(normais_triangulos[i]);
			
				normais_vertices[triangulos[i][0]].x += normais_triangulos[i].x;
				normais_vertices[triangulos[i][0]].y += normais_triangulos[i].y;
				normais_vertices[triangulos[i][0]].z += normais_triangulos[i].z;
			
				normais_vertices[triangulos[i][1]].x += normais_triangulos[i].x;
				normais_vertices[triangulos[i][1]].y += normais_triangulos[i].y;
				normais_vertices[triangulos[i][1]].z += normais_triangulos[i].z;

				normais_vertices[triangulos[i][2]].x += normais_triangulos[i].x;
				normais_vertices[triangulos[i][2]].y += normais_triangulos[i].y;
				normais_vertices[triangulos[i][2]].z += normais_triangulos[i].z;
			
			}

			for (int i = 0; i < qnt_pontos; i++) {
				Algebra.normalizar(normais_vertices[i]);
			}

			Objeto obj = new Objeto(pontos_visao, pontos, normais_triangulos, normais_vertices, triangulos);
			return obj;
		} else {
			System.out.println("Erro ao abrir o Objeto!");
			return null;
		}
	}
	
}
