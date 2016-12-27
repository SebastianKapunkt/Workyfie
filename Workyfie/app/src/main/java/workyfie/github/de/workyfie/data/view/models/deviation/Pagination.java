package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

import java.util.List;

public class Pagination {
    public final String id;
    public final Boolean hasMore;
    public final Integer nextOffset;
    public final List<Deviation> resultList;

    public Pagination(String id, Boolean hasMore, Integer nextOffset, List<Deviation> deviationList) {
        this.id = id;
        this.hasMore = hasMore;
        this.nextOffset = nextOffset;
        this.resultList = deviationList;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "id='" + id + '\'' +
                ", hasMore=" + hasMore +
                ", nextOffset=" + nextOffset +
                ", resultList=" + resultList +
                '}';
    }
}
