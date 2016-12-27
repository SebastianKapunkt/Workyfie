package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

public class Preview {
    public final String id;
    public final String src;
    public final Integer height;
    public final Integer width;
    public final Boolean transparency;

    public Preview(String id, String src, Integer height, Integer width, Boolean transparency) {
        this.id = id;
        this.src = src;
        this.height = height;
        this.width = width;
        this.transparency = transparency;
    }

    @Override
    public String toString() {
        return "Preview{" +
                "id='" + id + '\'' +
                ", src='" + src + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", transparency=" + transparency +
                '}';
    }

    public static Preview EMPTY() {
        return new Preview(
                "",
                "",
                0,
                0,
                false
        );
    }
}
