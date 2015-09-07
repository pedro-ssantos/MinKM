
public class mainApp {

	public static void main(String[] args) {
		k_means cluster = new k_means();
		//cluster.readArff("/home/peuss/Mineração/iris.arff");
		cluster.run("/home/peuss/Mineração/iris.arff", 3, 10);

	}

}
