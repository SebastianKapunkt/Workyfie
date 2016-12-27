package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

public class Thumb {
    public final String id;
    public final String src;
    public final Integer height;
    public final Integer width;
    public final Boolean transparency;

    public Thumb(String id, String src, Integer height, Integer width, Boolean transparency) {
        this.id = id;
        this.src = src;
        this.height = height;
        this.width = width;
        this.transparency = transparency;
    }

    @Override
    public String toString() {
        return "Thumb{" +
                "id='" + id + '\'' +
                ", src='" + src + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", transparency=" + transparency +
                '}';
    }
}
