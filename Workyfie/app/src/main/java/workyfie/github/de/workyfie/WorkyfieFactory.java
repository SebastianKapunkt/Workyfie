package workyfie.github.de.workyfie;

import workyfie.github.de.workyfie.application.ApplicationFactory;
import workyfie.github.de.workyfie.data.repository.RepositoryFactory;

/**
 * Singleton Pattern
 */
public class WorkyfieFactory {
    private static WorkyfieFactory instance;

    private RepositoryFactory repoFactory;
    private ApplicationFactory applicationFactory;

    private WorkyfieFactory(){
        repoFactory = RepositoryFactory.getInstance();
        applicationFactory = ApplicationFactory.getInstance();
    }

    public static WorkyfieFactory getInstance() {
        if (instance == null) {
            instance = new WorkyfieFactory();
        }
        return instance;
    }

    public RepositoryFactory getRepoFactory() {
        return repoFactory;
    }

    public ApplicationFactory getApplicationFactory() {
        return applicationFactory;
    }
}
