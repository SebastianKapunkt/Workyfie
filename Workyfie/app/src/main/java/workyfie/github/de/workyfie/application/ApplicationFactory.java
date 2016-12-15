package workyfie.github.de.workyfie.application;

import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;

/**
 * Singleton Pattern
 */
public class ApplicationFactory {
    private static ApplicationFactory instance;

    private BitalinoProxy bitalinoProxy;

    private ApplicationFactory(){
        bitalinoProxy = new BitalinoProxy();
    }

    public static ApplicationFactory getInstance() {
        if (instance == null) {
            instance = new ApplicationFactory();
        }
        return instance;
    }

    public BitalinoProxy getBitalino() {
        return bitalinoProxy;
    }
}
