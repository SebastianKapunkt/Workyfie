package workyfie.github.de.workyfie.application;

import workyfie.github.de.workyfie.data.repository.RepositoryFactory;

public class WorkyfieFactory {
    private static WorkyfieFactory instance;

    private RepositoryFactory repoFactory;

    public static WorkyfieFactory newInstance() {
        if (instance == null) {
            instance = new WorkyfieFactory();
        }
        return instance;
    }
}
