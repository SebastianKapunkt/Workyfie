package sebastiankapuntk.github.de.escalation.data.view.models.deviation;

import java.util.List;

public class Deviation {
    public final String id;
    public final String deviationId;
    public final String printId;
    public final String url;
    public final String title;
    public final String category;
    public final String categoryPath;
    public final Boolean isFavourite;
    public final Boolean isDeleted;
    public final Author author;
    public final Stats stats;
    public final Integer publishedTime;
    public final Boolean allowsComments;
    public final Preview preview;
    public final Content content;
    public final List<Thumb> thumbList;
    public final Boolean isMature;
    public final Boolean isDownloadable;
    public final Integer downloadFileSize;
    public final DailyDeviation dailyDeviation;

    public Deviation(String id, String deviationId, String printId, String url, String title, String category, String categoryPath, Boolean isFavourite, Boolean isDeleted, Author author, Stats stats, Integer publishedTime, Boolean allowsComments, Preview preview, Content content, List<Thumb> thumbList, Boolean isMature, Boolean isDownloadable, Integer downloadFileSize, DailyDeviation dailyDeviation) {
        this.id = id;
        this.deviationId = deviationId;
        this.printId = printId;
        this.url = url;
        this.title = title;
        this.category = category;
        this.categoryPath = categoryPath;
        this.isFavourite = isFavourite;
        this.isDeleted = isDeleted;
        this.author = author;
        this.stats = stats;
        this.publishedTime = publishedTime;
        this.allowsComments = allowsComments;
        this.preview = preview;
        this.content = content;
        this.thumbList = thumbList;
        this.isMature = isMature;
        this.isDownloadable = isDownloadable;
        this.downloadFileSize = downloadFileSize;
        this.dailyDeviation = dailyDeviation;
    }

    @Override
    public String toString() {
        return "Deviation{" +
                "id='" + id + '\'' +
                ", deviationId='" + deviationId + '\'' +
                ", printId='" + printId + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", categoryPath='" + categoryPath + '\'' +
                ", isFavourite=" + isFavourite +
                ", isDeleted=" + isDeleted +
                ", author=" + author +
                ", stats=" + stats +
                ", publishedTime=" + publishedTime +
                ", allowsComments=" + allowsComments +
                ", preview=" + preview +
                ", content=" + content +
                ", thumbList=" + thumbList +
                ", isMature=" + isMature +
                ", isDownloadable=" + isDownloadable +
                ", downloadFileSize=" + downloadFileSize +
                ", dailyDeviation=" + dailyDeviation +
                '}';
    }

    public static Deviation setId(Deviation deviation, String id) {
        return new Deviation(
                id,
                deviation.deviationId,
                deviation.printId,
                deviation.url,
                deviation.title,
                deviation.category,
                deviation.categoryPath,
                deviation.isFavourite,
                deviation.isDeleted,
                deviation.author,
                deviation.stats,
                deviation.publishedTime,
                deviation.allowsComments,
                deviation.preview,
                deviation.content,
                deviation.thumbList,
                deviation.isMature,
                deviation.isDownloadable,
                deviation.downloadFileSize,
                deviation.dailyDeviation
        );
    }
}
