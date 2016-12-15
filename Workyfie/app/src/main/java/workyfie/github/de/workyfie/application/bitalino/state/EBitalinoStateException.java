package workyfie.github.de.workyfie.application.bitalino.state;

public class EBitalinoStateException extends Exception {
    private static final String TAG = EBitalinoStateException.class.getSimpleName();

    public EBitalinoStateException(String text) {
        super(TAG + ": " + text);
    }
}
