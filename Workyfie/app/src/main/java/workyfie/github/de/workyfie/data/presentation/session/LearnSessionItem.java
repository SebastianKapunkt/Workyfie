package workyfie.github.de.workyfie.data.presentation.session;

public class LearnSessionItem {
    public final SessionEnum state;
    public final long sessionStartTime;
    public final long lastBreak;

    public LearnSessionItem(SessionEnum state, long sessionStartTime, long lastBreak) {
        this.state = state;
        this.sessionStartTime = sessionStartTime;
        this.lastBreak = lastBreak;
    }

    public static LearnSessionItem setState(LearnSessionItem item, SessionEnum sessionState) {
        return new LearnSessionItem(
                sessionState,
                item.sessionStartTime,
                item.lastBreak);
    }

    public static LearnSessionItem setSessionStartTime(LearnSessionItem item, long sessionStart) {
        return new LearnSessionItem(
                item.state,
                sessionStart,
                item.lastBreak);
    }

    public static LearnSessionItem setLastBreak(LearnSessionItem item, long lastBreak) {
        return new LearnSessionItem(
                item.state,
                item.sessionStartTime,
                lastBreak
        );
    }

    public static LearnSessionItem reset() {
        return new LearnSessionItem(
                SessionEnum.OUT_SESSION,
                0,
                0
        );
    }
}
