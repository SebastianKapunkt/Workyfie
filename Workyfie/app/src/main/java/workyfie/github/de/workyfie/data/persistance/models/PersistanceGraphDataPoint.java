package workyfie.github.de.workyfie.data.persistance.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PersistanceGraphDataPoint extends RealmObject {
    private String id;
    private String sessionId;
    private Double x;
    private Double y;

    public PersistanceGraphDataPoint() {
    }

    public PersistanceGraphDataPoint(String id, String sessionId, Double x, Double y) {
        this.id = id;
        this.sessionId = sessionId;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
