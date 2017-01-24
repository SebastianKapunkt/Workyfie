package workyfie.github.de.workyfie.data.persistance.models.converter;

import java.util.List;

import workyfie.github.de.workyfie.data.converter.Converter;
import workyfie.github.de.workyfie.data.converter.ConverterUtils;
import workyfie.github.de.workyfie.data.persistance.models.PersistanceGraphDataPoint;
import workyfie.github.de.workyfie.data.persistance.models.PersistanceSession;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;
import workyfie.github.de.workyfie.data.view.models.Session;

public class GraphDataPointPersistanceViewConverter implements Converter<PersistanceGraphDataPoint, GraphDataPoint> {
    @Override
    public GraphDataPoint from(PersistanceGraphDataPoint value) {
        return new GraphDataPoint(
                value.getId(),
                value.getSessionId(),
                value.getX(),
                value.getY()
        );
    }

    @Override
    public PersistanceGraphDataPoint to(GraphDataPoint value) {
        return new PersistanceGraphDataPoint(
                value.id,
                value.sessionId,
                value.x,
                value.y
        );
    }

    public List<GraphDataPoint> from(List<PersistanceGraphDataPoint> values) {
        return ConverterUtils.from(values, this);
    }

    public List<PersistanceGraphDataPoint> to(List<GraphDataPoint> values) {
        return ConverterUtils.to(values, this);
    }
}
