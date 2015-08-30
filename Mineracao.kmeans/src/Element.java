import weka.core.Instance;

public class Element {
	
	private Instance instance;
	private int nearestCentroid;

	public Element() {
		
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public int getNearestCentroid() {
		return nearestCentroid;
	}

	public void setNearestCentroid(int nearestCentroid) {
		this.nearestCentroid = nearestCentroid;
	}
}
