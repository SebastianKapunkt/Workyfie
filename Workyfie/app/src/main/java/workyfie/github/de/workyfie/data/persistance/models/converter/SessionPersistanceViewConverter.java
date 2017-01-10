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
                Instant.parse(value.getStartTime()),
                Instant.parse(value.getEndTime())
        );
    }

    @Override
    public PersistanceSession to(Session value) {
        return new PersistanceSession(
                Integer.valueOf(value.id),
                value.name,
                value.startTime.toString(),
                value.endTime.toString()
        );
    }

    public List<Session> from(List<PersistanceSession> values) {
        return ConverterUtils.from(values, this);
    }

    public List<PersistanceSession> to(List<Session> values) {
        return ConverterUtils.to(values, this);
    }
}
