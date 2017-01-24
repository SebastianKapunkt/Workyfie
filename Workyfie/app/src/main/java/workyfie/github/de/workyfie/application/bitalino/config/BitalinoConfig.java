package workyfie.github.de.workyfie.application.bitalino.config;

public class BitalinoConfig {
    public final int CHANNEL;
    public final String MAC_ADRESS;
    public final int SAMPLE_RATE;

    public BitalinoConfig(int CHANNEL, String MAC_ADRESS, int SAMPLE_RATE) {
        this.CHANNEL = CHANNEL;
        this.MAC_ADRESS = MAC_ADRESS;
        this.SAMPLE_RATE = SAMPLE_RATE;
    }
}
