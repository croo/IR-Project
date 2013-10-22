package database.sentimental;

import java.util.ArrayList;
import java.util.List;

public class Weight {
	public List<Double> positive = new ArrayList<>();
	public List<Double> negative = new ArrayList<>();

	public Weight() {
	}
	
	public Weight(double positive, double negative) {
		this.positive.add(positive);
		this.negative.add(negative);
	}
	
	public void add(Weight w) {
		positive.addAll(w.positive);
		negative.addAll(w.negative);
	}
}
