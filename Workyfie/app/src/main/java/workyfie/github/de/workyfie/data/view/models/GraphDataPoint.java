package workyfie.github.de.workyfie.data.view.models;

import com.jjoe64.graphview.series.DataPointInterface;

public class GraphDataPoint implements DataPointInterface{
    public final String id;
    public final String sessionId;
    public final Double x;
    public final Double y;

    public GraphDataPoint(String id, String sessionId, Double x, Double y) {
        this.id = id;
        this.sessionId = sessionId;
        this.x = x;
        this.y = y;
    }

    public static GraphDataPoint setId(GraphDataPoint graphDataPoint, String nextKey) {
        return new GraphDataPoint(
                nextKey,
                graphDataPoint.sessionId,
                graphDataPoint.x,
                graphDataPoint.y
        );
    }
    public String toString(){
        return "x: "+ x + " y: " + y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }
}
