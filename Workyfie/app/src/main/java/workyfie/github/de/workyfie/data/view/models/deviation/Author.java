package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

public class Author {
    public final String id;
    public final String userId;
    public final String username;
    public final String userIcon;
    public final String type;

    public Author(String id, String userId, String username, String userIcon, String type) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.userIcon = userIcon;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static Author EMPTY() {
        return new Author(
                "",
                "",
                "",
                "",
                ""
        );
    }
}
