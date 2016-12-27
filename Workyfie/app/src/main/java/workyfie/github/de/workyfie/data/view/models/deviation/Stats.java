package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

public class Stats {
    public final String id;
    public final Integer comments;
    public final Integer favourites;

    public Stats(String id, Integer comments, Integer favourites) {
        this.id = id;
        this.comments = comments;
        this.favourites = favourites;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "id='" + id + '\'' +
                ", comments=" + comments +
                ", favourites=" + favourites +
                '}';
    }

    public static Stats EMPTY() {
        return new Stats(
                "",
                0,
                0
        );
    }
}
