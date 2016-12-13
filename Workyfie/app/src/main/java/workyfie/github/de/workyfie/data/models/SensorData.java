package workyfie.github.de.workyfie.data.models;

public class SensorData {
    public final String id;
    public final int data;
    public final long timestamp;

    public SensorData(String id, int data, long timestamp) {
        this.id = id;
        this.data = data;
        this.timestamp = timestamp;
    }
}
