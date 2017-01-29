package workyfie.github.de.workyfie.data.view.models;

import org.threeten.bp.Instant;

public class Session {
    public final String id;
    public final String name;
    public final Instant startTime;
    public final Instant endTime;

    public Session(String id, String name, Instant startTime, Instant endTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Session setStartTime(Session session, Instant startTime) {
        return new Session(
                session.id,
                session.name,
                startTime,
                session.endTime
        );
    }

    public static Session setEndTime(Session session, Instant endTime) {
        return new Session(
                session.id,
                session.name,
                session.startTime,
                endTime
        );
    }

    public static Session setId(Session session, String nextKey) {
        return new Session(
                nextKey,
                session.name,
                session.startTime,
                session.endTime
        );
    }

    public static Session setName(Session session, String name) {
        return new Session(
                session.id,
                name,
                session.startTime,
                session.endTime
        );
    }
}
