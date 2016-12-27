package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

public class Content {
    public final String id;
    public final String src;
    public final Integer height;
    public final Integer width;
    public final boolean transparency;
    public final Integer fileSize;

    public Content(String id, String src, Integer height, Integer width, boolean transparency, Integer fileSize) {
        this.id = id;
        this.src = src;
        this.height = height;
        this.width = width;
        this.transparency = transparency;
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "Content{" +
                "id='" + id + '\'' +
                ", src='" + src + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", transparency=" + transparency +
                ", fileSize=" + fileSize +
                '}';
    }

    public static Content EMPTY() {
        return new Content(
                "",
                "",
                0,
                0,
                false,
                0
        );
    }
}
