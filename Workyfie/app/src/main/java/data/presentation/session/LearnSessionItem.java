package data.presentation.session;

public class LearnSessionItem {
    public final SessionEnum state;

    public LearnSessionItem(SessionEnum state) {
        this.state = state;
    }

    public static LearnSessionItem setState(LearnSessionItem item, SessionEnum outSession) {
        return new LearnSessionItem(outSession);
    }
}
