package workyfie.github.de.workyfie.application;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoReceiveHandler;
import workyfie.github.de.workyfie.application.modules.DataModule;
import workyfie.github.de.workyfie.application.modules.ThreadingModule;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointRepository;
import workyfie.github.de.workyfie.data.repos.session.SessionRepository;

/**
 * hold the modules for the app
 */
public class ApplicationComponent {

    private DataModule dataModule;
    private ThreadingModule threadingModule;

    private GraphDataPointRepository graphDataPointRepository;
    private SessionRepository sessionRepository;
    private BitalinoReceiveHandler bitalinoReceiveHandler;
    private BitalinoProxy bitalinoProxy;

    public ApplicationComponent(App app) {
        threadingModule = new ThreadingModule();
        dataModule = new DataModule(app);
    }

    public DataModule getDataModule() {
        return dataModule;
    }

    public ThreadingModule getThreadingModule() {
        return threadingModule;
    }

    public BitalinoProxy getBitalinoProxy() {
        if (bitalinoProxy == null) {
            bitalinoProxy = dataModule.provideBitalinoProxy();
        }
        return bitalinoProxy;
    }

    public BitalinoReceiveHandler getBitalinoReceiveHandler() {
        if(bitalinoReceiveHandler == null){
            bitalinoReceiveHandler = dataModule.provideBitalinoReceiveDataHandler();
        }
        return bitalinoReceiveHandler;
    }

    public SessionRepository getSessionRepository() {
        if (sessionRepository == null) {
            sessionRepository = dataModule.provideSessionRepository();
        }
        return sessionRepository;
    }

    public GraphDataPointRepository getGraphDataPointRepository() {
        if (graphDataPointRepository == null) {
            graphDataPointRepository = dataModule.provideGrapDataPointRepository();
        }
        return graphDataPointRepository;
    }
}
