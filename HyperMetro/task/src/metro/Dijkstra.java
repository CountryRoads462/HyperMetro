package metro;

import java.util.Comparator;

public class Dijkstra {
    private final Station station;
    private int weight;

    public Dijkstra(Station station, int weight) {
        this.station = station;
        this.weight = weight;
    }

    public Station getStation() {
        return station;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

class DijkstraWeightComparator implements Comparator<Dijkstra> {

    @Override
    public int compare(Dijkstra t1, Dijkstra t2) {
        return Integer.compare(t1.getWeight(), t2.getWeight());
    }
}
