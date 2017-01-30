package workyfie.github.de.workyfie.data.persistence.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PersistenceSession extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private String startTime;
    private String endTime;

    public PersistenceSession() {
    }

    public PersistenceSession(int id, String name, String startTime, String endTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
