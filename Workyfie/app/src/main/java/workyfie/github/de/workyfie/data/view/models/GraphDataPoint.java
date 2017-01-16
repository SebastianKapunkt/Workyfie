package workyfie.github.de.workyfie.data.view.models;

public class GraphDataPoint {
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
}
