package workyfie.github.de.workyfie.data.persistence.models.converter;

import java.util.List;

import workyfie.github.de.workyfie.data.converter.Converter;
import workyfie.github.de.workyfie.data.converter.ConverterUtils;
import workyfie.github.de.workyfie.data.persistence.models.PersistenceGraphDataPoint;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GraphDataPointPersistenceViewConverter implements Converter<PersistenceGraphDataPoint, GraphDataPoint> {
    @Override
    public GraphDataPoint from(PersistenceGraphDataPoint value) {
        return new GraphDataPoint(
                value.getId(),
                value.getSessionId(),
                value.getX(),
                value.getY()
        );
    }

    @Override
    public PersistenceGraphDataPoint to(GraphDataPoint value) {
        return new PersistenceGraphDataPoint(
                value.id,
                value.sessionId,
                value.x,
                value.y
        );
    }

    public List<GraphDataPoint> from(List<PersistenceGraphDataPoint> values) {
        return ConverterUtils.from(values, this);
    }

    public List<PersistenceGraphDataPoint> to(List<GraphDataPoint> values) {
        return ConverterUtils.to(values, this);
    }
}
