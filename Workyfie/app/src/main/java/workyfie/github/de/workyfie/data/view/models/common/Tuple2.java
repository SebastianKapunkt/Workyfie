package workyfie.github.de.workyfie.data.view.models.common;

public class Tuple2<S, T> {

    public final S item1;
    public final T item2;

    public Tuple2(S item1, T item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Tuple2)) {
            return false;
        } else {
            Tuple2 other = (Tuple2) obj;
            return this.item1.equals(other.item1) && this.item2.equals(other.item2);
        }
    }

    public int hashCode() {
        byte hash = 5;
        int hash1 = 79 * hash + (this.item1 != null ? this.item1.hashCode() : 0);
        hash1 = 79 * hash1 + (this.item2 != null ? this.item2.hashCode() : 0);
        return hash1;
    }

    public String toString() {
        return String.format("(%s, %s)", this.item1, this.item2);
    }
}
