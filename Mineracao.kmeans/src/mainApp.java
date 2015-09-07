import java.io.File;
import java.io.IOException;

public class mainApp {

	public static void main(String[] args) {
		k_means cluster = new k_means();
		//cluster.readArff("/home/peuss/Mineração/iris.arff");
		String path = null;
		try {
			path = new File("../Mineracao.kmeans/data/iris.arff").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cluster.run(path, 3, 10);

	}

}
