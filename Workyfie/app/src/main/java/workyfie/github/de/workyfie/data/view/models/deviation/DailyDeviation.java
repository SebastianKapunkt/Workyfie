package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

public class DailyDeviation {
    public final String id;
    public final String body;
    public final String time;
    public final Giver giver;

    public DailyDeviation(String id, String body, String time, Giver giver) {
        this.id = id;
        this.body = body;
        this.time = time;
        this.giver = giver;
    }

    @Override
    public String toString() {
        return "DailyDeviation{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", time='" + time + '\'' +
                ", giver=" + giver +
                '}';
    }

    public static DailyDeviation EMPTY() {
        return new DailyDeviation(
                "",
                "",
                "",
                Giver.EMPTY()
        );
    }
}
