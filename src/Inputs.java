import java.io.BufferedReader;
import java.io.IOException;


public class Inputs {
	
	public static Camera getCamera(BufferedReader camera) throws IOException {
		Ponto C, V, N, U;
		double d = 0, hx = 0, hy = 0, norma;
		String x, y, z, linha;
		String[] temp;
		// pega as informaÃ§Ãµes da camera:
		if (camera.ready()) {
			// lÃª as coordenadas do vetor C:
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			C = new Ponto(Double.valueOf(x), Double.valueOf(y),
					Double.valueOf(z));
			// lÃª as coordenadas do vetor N:
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			N = new Ponto(Double.valueOf(x), Double.valueOf(y),
					Double.valueOf(z));
			// lÃª as coordenadas do vetor V:
			linha = camera.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			V = new Ponto(Double.valueOf(x), Double.valueOf(y),
					Double.valueOf(z));
			// lÃª d, hx e hy:
			linha = camera.readLine();
			temp = linha.split(" ");
			d = Double.valueOf(temp[0]);
			hx = Double.valueOf(temp[1]);
			hy = Double.valueOf(temp[2]);

			// fecha o arquivo:
			camera.close();

			// normaliza N:
			norma = Math.sqrt(Algebra.produtoEscalar(N, N));
			N.x = N.x / norma;
			N.y = N.y / norma;
			N.z = N.z / norma;
			// ortogonaliza V:
			double coef = (Algebra.produtoEscalar(V, N) / Algebra
					.produtoEscalar(N, N));

			V.x = V.x - (coef * N.x);
			V.y = V.y - (coef * N.y);
			V.z = V.z - (coef * N.z);
			// normaliza V:
			norma = Math.sqrt(Algebra.produtoEscalar(V, V));
			V.x = V.x / norma;
			V.y = V.y / norma;
			V.z = V.z / norma;
			// calcula U:
			U = Algebra.produtoVetorial(N, V);

			Camera cam = new Camera(C, V, N, U, hx, hy, d);

			return cam;
		} else {
			System.out.println("Erro ao ler arquivo!");
			return null;
		}

	}
	
	public static Iluminacao getIluminacao(BufferedReader iluminacao) throws NumberFormatException, IOException {
		String x, y, z, linha, temp[];
		
		if (iluminacao.ready()) {
			Ponto pl;
			double ka, kd, ks, n;
			double[] ia = new double[3];
			double[] od = new double[3];
			double[] il = new double[3];
			// lÃª as coordenadas do ponto de luz Pl:
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			pl = new Ponto(Double.valueOf(x), Double.valueOf(y),
					Double.valueOf(z));
			// lÃª Ka (constante de reflexao ambiental):
			linha = iluminacao.readLine();
			ka = Double.valueOf(linha);
			// lÃª o vetor de cor ambiental Ia:
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			ia[0] = Double.valueOf(x);
			ia[1] = Double.valueOf(y);
			ia[2] = Double.valueOf(z);
			// lÃª Kd (constante difusa):
			linha = iluminacao.readLine();
			kd = Double.valueOf(linha);
			// lÃª o vetor difuso Od:
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			od[0] = Double.valueOf(x);
			od[1] = Double.valueOf(y);
			od[2] = Double.valueOf(z);
			// lÃª Ks (parte especular):
			linha = iluminacao.readLine();
			ks = Double.valueOf(linha);
			// lÃª a cor da fonte de luz Il:
			linha = iluminacao.readLine();
			temp = linha.split(" ");
			x = temp[0];
			y = temp[1];
			z = temp[2];
			il[0] = Double.valueOf(x);
			il[1] = Double.valueOf(y);
			il[2] = Double.valueOf(z);
			// lÃª a constante de rugosidade n:
			linha = iluminacao.readLine();
			n = Double.valueOf(linha);

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
			// lÃª as quantidades de pontos e de triangulos:
			linha = objeto.readLine();
			qnt_pontos = Integer.valueOf(linha.substring(0, linha.indexOf(' ')));
			qnt_triangulos = Integer.valueOf(linha.substring(linha.indexOf(' ') + 1));

			// inicia os arrays adequados:
			pontos = new Ponto[qnt_pontos];
			pontos_visao = new Ponto[qnt_pontos];
			triangulos = new int[qnt_triangulos][3];
			
			// lÃª todos os pontos:
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
			// lÃª todos os triangulos:
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
				}// guarda o indice de cada vertice no array de pontos:
				triangulos[i][0] = Integer.valueOf(x) - 1;
				triangulos[i][1] = Integer.valueOf(y) - 1;
				triangulos[i][2] = Integer.valueOf(z) - 1;
			}

			// converte os pontos para coordenadas de visÃ£o:
			for (int i = 0; i < qnt_pontos; i++) {
				// calcula P - C:
				pontos[i].x -= camera.C.x;
				pontos[i].y -= camera.C.y;
				pontos[i].z -= camera.C.z;

				// multiplica P-C pela matriz de mudanÃ§a de base:
				double x_visao = (pontos[i].x * camera.U.x)
						+ (pontos[i].y * camera.U.y)
						+ (pontos[i].z * camera.U.z);
				double y_visao = (pontos[i].x * camera.V.x)
						+ (pontos[i].y * camera.V.y)
						+ (pontos[i].z * camera.V.z);
				double z_visao = (pontos[i].x * camera.N.x)
						+ (pontos[i].y * camera.N.y)
						+ (pontos[i].z * camera.N.z);

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

			// converte os pontos para coordenadas de tela:
			for (int i = 0; i < qnt_pontos; i++) {
				double x_temp, y_temp, z_temp;
				x_temp = pontos_visao[i].x;
				y_temp = pontos_visao[i].y;
				z_temp = pontos_visao[i].z;

				pontos[i].x = (camera.d / camera.hx) * (x_temp / z_temp);
				pontos[i].y = (camera.d / camera.hy) * (y_temp / z_temp);
				// estamos com coordenadas em 2D, entao assumimos z = 0:
				pontos[i].z = 0;
				pontos[i].id = i;
			}

			objeto.close();

			Ponto v1 = new Ponto(0.0, 0.0, 0.0), v2 = new Ponto(1.0, 1.0, 1.0);
			normais_triangulos = new Ponto[qnt_triangulos];
			normais_vertices = new Ponto[qnt_pontos];
			// inicia as normais de todos os vertices como 0:
			for (int i = 0; i < qnt_pontos; i++) {
				normais_vertices[i] = new Ponto(0.0, 0.0, 0.0);
			}

			// calcula a normal de todos os triangulos:
			for (int i = 0; i < qnt_triangulos; i++) {
				Ponto p1 = pontos_visao[triangulos[i][0]];
				Ponto p2 = pontos_visao[triangulos[i][1]];
				Ponto p3 = pontos_visao[triangulos[i][2]];

				// calcula o vetor v1:
				v1.x = p2.x - p1.x;
				v1.y = p2.y - p1.y;
				v1.z = p2.z - p1.z;
				
				// calcula o vetor v2:
				v2.x = p3.x - p1.x;
				v2.y = p3.y - p1.y;
				v2.z = p3.z - p1.z;

				// calcula a normal, que Ã© o produto vetorial entre v1 e v2:
				normais_triangulos[i] = Algebra.produtoVetorial(v1, v2);
				

				// normaliza a normal do triangulo:
				Algebra.normalizar(normais_triangulos[i]);
			
				// soma a normal do triangulo Ã s normais dos vertices que o
				// formam:
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
			// normaliza as normais dos vÃ©rtices:
			for (int i = 0; i < qnt_pontos; i++) {
				Algebra.normalizar(normais_vertices[i]);
			}

			Objeto obj = new Objeto(pontos_visao, pontos, normais_triangulos, normais_vertices, triangulos);
			return obj;
		} else {
			System.out.println("Erro ao abrir objeto");
			return null;
		}
	}
	
}
