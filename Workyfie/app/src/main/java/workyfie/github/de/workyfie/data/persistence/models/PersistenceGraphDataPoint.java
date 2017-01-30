package workyfie.github.de.workyfie.data.persistence.models;

import io.realm.RealmObject;

public class PersistenceGraphDataPoint extends RealmObject {
    private String id;
    private String sessionId;
    private Double x;
    private Double y;

    public PersistenceGraphDataPoint() {
    }

    public PersistenceGraphDataPoint(String id, String sessionId, Double x, Double y) {
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
