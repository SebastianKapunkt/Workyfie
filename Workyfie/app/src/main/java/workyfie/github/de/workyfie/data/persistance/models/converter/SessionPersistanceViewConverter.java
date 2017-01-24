package workyfie.github.de.workyfie.data.persistance.models.converter;

import org.threeten.bp.Instant;

import java.util.List;

import workyfie.github.de.workyfie.data.converter.Converter;
import workyfie.github.de.workyfie.data.converter.ConverterUtils;
import workyfie.github.de.workyfie.data.persistance.models.PersistanceSession;
import workyfie.github.de.workyfie.data.view.models.Session;

public class SessionPersistanceViewConverter implements Converter<PersistanceSession, Session> {
    @Override
    public Session from(PersistanceSession value) {
        return new Session(
                String.valueOf(value.getId()),
                value.getName(),
                value.getStartTime().isEmpty() ? null : Instant.parse(value.getStartTime()),
                value.getEndTime().isEmpty() ? null : Instant.parse(value.getEndTime())
        );
    }

    @Override
    public PersistanceSession to(Session value) {
        return new PersistanceSession(
                Integer.valueOf(value.id),
                value.name,
                value.startTime == null ? "" : value.startTime.toString(),
                value.endTime == null ? "" : value.endTime.toString()
        );
    }

    public List<Session> from(List<PersistanceSession> values) {
        return ConverterUtils.from(values, this);
    }

    public List<PersistanceSession> to(List<Session> values) {
        return ConverterUtils.to(values, this);
    }
}
