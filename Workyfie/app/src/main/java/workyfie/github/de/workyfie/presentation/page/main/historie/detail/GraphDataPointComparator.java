package workyfie.github.de.workyfie.presentation.page.main.historie.detail;

import java.util.Comparator;

import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GraphDataPointComparator implements Comparator<GraphDataPoint> {
    @Override
    public int compare(GraphDataPoint o1, GraphDataPoint o2) {
        if (o1.x > o2.x) {
            return 1;
        }
        if (o1.x < o2.x) {
            return -1;
        }
        return 0;
    }
}
