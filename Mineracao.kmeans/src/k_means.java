import weka.core.Instance;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class k_means {

	private int k;
	private Instances dataSet;
	private int iterations = 0;
	private List<Element> set;
	private List<List<Double>> centroids; // Coordenadas das centroids
	private List<List<Instance>> centroidSet;

	public k_means() {
		super();
	}

	/* Função: run
	 * ---------------------------------------------------
	 * Algoritmo =>
	 * 1) Escolha um número k de grupos (clusters)
	 * 2) Escolha os centros dos grupos
	 * 3) Associe cada um dos pontos ao centro mais próximo
	 * 4) Quando todos os pontos foram assinalados, recalcule a posição dos centros
	 * 5) Repita os passos 3) e 4) até que os centros não se movam mais.
	 */
	public void run(String pathDataSet, int k) {
		set = new ArrayList<Element>();
		centroidSet = new ArrayList<List<Instance>>();
		iterations = 0;

		// Iniciar as listas das centroides.
		for (int n = 0; n < k; n++) {
			List<Instance> s = new ArrayList<Instance>();
			centroidSet.add(n, s);
		}

		// Lê a base de dados.
		this.dataSet = readArff(pathDataSet);

		centroids = getRandomCentroids(k);

		while (iterations < 3) {
			//TODO: Criterio de parada.
			//Associa cada ponto ao centro mais próximo.
			for (int n = 0; n < dataSet.numInstances(); n++) {
				Instance i = dataSet.instance(n);
				centroidSet.get(getLabel(i, centroids)).add(i);
			}
			//Recalcula os centros.
			centroids = getNewCentroids();
			iterations++;

		}
	}

	protected List<List<Double>> getRandomCentroids(int k) {
		// TODO: Casas decimais saindo incorretas. 
		List<List<Double>> centroids = new ArrayList<List<Double>>();

		for (int z = 0; z < dataSet.numAttributes() - 2; z++) {
			double max = 0;
			double min = 0;
			List<Double> centroid = new ArrayList<Double>();

			for (int i = 0; i <= dataSet.numAttributes() - 2; i++) {
				dataSet.sort(i);

				min = dataSet.firstInstance().value(i);
				max = dataSet.lastInstance().value(i);

				Random rand = new Random();

				Double seed = rand.nextDouble();
				centroid.add(i, decimalFormat(seed * (max - min)) + min);
			}
			centroids.add(z, centroid);
		}
		return centroids;
	}

	/*Função: getNewCentroids
	 * --------------------------
	 * Atualiza o valor das centroides para a média dos pontos da centroide antiga. 
	 * Se uma centroide não tem nenhum elemento é sorteado um valor.
	 */
	protected List<List<Double>> getNewCentroids() {

		List<List<Double>> NewCentroids = new ArrayList<List<Double>>();

		int count = 0;
		for (List<Instance> list : centroidSet) {
			Double[] soma = new Double[list.get(0).numAttributes() - 1];

			for (int n = 0; n < soma.length; n++) {
				soma[n] = 0.0;
			}

			for (Instance i : list) {
				// Faz a soma dos os atributos dsa instanciais que estão na mesma centroid.
				for (int n = 0; n <= i.numAttributes() - 2; n++) {
					soma[n] += i.value(n);
				}
			}

			List<Double> centroid = new ArrayList<Double>();

			for (int n = 0; n < soma.length; n++) {
				soma[n] = decimalFormat(soma[n] / list.size());
				centroid.add(n, soma[n]);				
			}
			
			NewCentroids.add(count, centroid);
			count++;
		}

		return NewCentroids;
	}

	/* Função: getCentroids
	 * ---------------------------
	 * Retorna centrids de teste (Menor, Média e Maior).
	 */

	protected List<List<Double>> getCentroids() {
		List<Double> c1 = new ArrayList<Double>();
		c1.add(0, 4.3);
		c1.add(1, 2.0);
		c1.add(2, 1.0);
		c1.add(3, 0.1);

		List<Double> c2 = new ArrayList<Double>();
		c2.add(0, 7.9);
		c2.add(1, 4.4);
		c2.add(2, 6.9);
		c2.add(3, 2.5);

		List<Double> c3 = new ArrayList<Double>();
		c3.add(0, 5.84);
		c3.add(1, 3.05);
		c3.add(2, 3.76);
		c3.add(3, 1.2);

		List<List<Double>> c = new ArrayList<List<Double>>();
		c.add(0, c1);
		c.add(1, c2);
		c.add(2, c3);

		return c;

	}

	/* Função: getDistance
	 * ---------------------------
	 * Retorna a distância euclidiana entre a Instance i e a centroid. Null se as dimensões diferentes.
	 */
	protected Double getDistance(Instance i, List<Double> centroid) {

		Double distance = 0.0;

		if (i.numAttributes() - 1 < centroid.size() | i.numAttributes() - 1 > centroid.size()) {
			return null;
		}

		for (int att = 0; att <= i.numAttributes() - 2; att++) {
			distance += Math.pow(i.value(att) - centroid.get(att), 2);
		}

		return decimalFormat(Math.sqrt(distance));
	}

	/* Função: getLabel
	 * --------------------
	 * Retorna a posição do rótulo, centroide mais próxima, da Instance i.
	 */
	protected int getLabel(Instance i, List<List<Double>> centroids) {
		int c = 0, smallest = 0;
		Double dist = Double.MAX_VALUE;

		for (List<Double> centroid : centroids) {
			if (getDistance(i, centroid) < dist) {
				dist = getDistance(i, centroid);
				smallest = c;
			}
			c++;
		}

		return smallest;
	}

	protected Instances readArff(String path) {
		// Lê o dado do arquivo arff e retorna o set de dados em data.

		try {
			Instances data = new Instances(new BufferedReader(new FileReader(path)));
			data.setClassIndex(data.numAttributes() - 1);

			return data;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Double decimalFormat(Double number) {

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');

		NumberFormat form = new DecimalFormat("0.###", dfs);

		return Double.valueOf(form.format(number));		

	}
}
