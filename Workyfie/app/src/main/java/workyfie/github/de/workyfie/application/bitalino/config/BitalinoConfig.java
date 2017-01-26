package workyfie.github.de.workyfie.application.bitalino.config;

public enum BitalinoConfig {
    BITALINO_CONFIG_EEG(0, 1000);

    public final int CHANNEL;
    public final int SAMPLE_RATE;

    BitalinoConfig(int CHANNEL, int SAMPLE_RATE) {
        this.CHANNEL = CHANNEL;
        this.SAMPLE_RATE = SAMPLE_RATE;
    }
}
