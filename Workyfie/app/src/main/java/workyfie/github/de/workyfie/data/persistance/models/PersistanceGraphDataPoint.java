package workyfie.github.de.workyfie.data.persistance.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PersistanceGraphDataPoint extends RealmObject {
    @PrimaryKey
    private int id;
    private String sessionId;
    private Double x;
    private Double y;

    public PersistanceGraphDataPoint() {
    }

    public PersistanceGraphDataPoint(int id, String sessionId, Double x, Double y) {
        this.id = id;
        this.sessionId = sessionId;
        this.x = x;
        this.y = y;
    }

    public int getId() {
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
