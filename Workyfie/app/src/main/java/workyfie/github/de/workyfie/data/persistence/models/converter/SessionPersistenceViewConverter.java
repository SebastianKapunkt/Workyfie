package workyfie.github.de.workyfie.data.persistence.models.converter;

import org.threeten.bp.Instant;

import java.util.List;

import workyfie.github.de.workyfie.data.converter.Converter;
import workyfie.github.de.workyfie.data.converter.ConverterUtils;
import workyfie.github.de.workyfie.data.persistence.models.PersistenceSession;
import workyfie.github.de.workyfie.data.view.models.Session;

public class SessionPersistenceViewConverter implements Converter<PersistenceSession, Session> {
    @Override
    public Session from(PersistenceSession value) {
        return new Session(
                String.valueOf(value.getId()),
                value.getName(),
                value.getStartTime().isEmpty() ? null : Instant.parse(value.getStartTime()),
                value.getEndTime().isEmpty() ? null : Instant.parse(value.getEndTime())
        );
    }

    @Override
    public PersistenceSession to(Session value) {
        return new PersistenceSession(
                Integer.valueOf(value.id),
                value.name,
                value.startTime == null ? "" : value.startTime.toString(),
                value.endTime == null ? "" : value.endTime.toString()
        );
    }

    public List<Session> from(List<PersistenceSession> values) {
        return ConverterUtils.from(values, this);
    }

    public List<PersistenceSession> to(List<Session> values) {
        return ConverterUtils.to(values, this);
    }
}
