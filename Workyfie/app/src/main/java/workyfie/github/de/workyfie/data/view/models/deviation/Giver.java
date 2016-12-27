package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

public class Giver {
    public final String id;
    public final String userId;
    public final String userName;
    public final String userIcon;
    public final String type;

    public Giver(String id, String userId, String userName, String userIcon, String type) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userIcon = userIcon;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Giver{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static Giver EMPTY() {
        return new Giver(
                "",
                "",
                "",
                "",
                ""
        );
    }
}
