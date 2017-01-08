package workyfie.github.de.workyfie.application.bitalino.state;

import info.plux.pluxapi.bitalino.BITalinoCommunication;
import info.plux.pluxapi.bitalino.BITalinoException;

public class BitalinoStateConnecting implements IBitalinoState {
    private static String errText = "Bitalino is connecting";

    @Override
    public IBitalinoState start(BITalinoCommunication bitalino, int[] channels, int samplerate) throws EBitalinoStateException {
        throw  new EBitalinoStateException(errText);
    }

    @Override
    public IBitalinoState stop(BITalinoCommunication bitalino) throws EBitalinoStateException {
        throw new EBitalinoStateException(errText);
    }

    @Override
    public IBitalinoState connect(BITalinoCommunication bitalino, String macAdresse) throws EBitalinoStateException {
        throw  new EBitalinoStateException(errText);
    }

    @Override
    public IBitalinoState disconnect(BITalinoCommunication bitalino) throws EBitalinoStateException {
        try {
            bitalino.disconnect();
        } catch (BITalinoException e) {
            e.printStackTrace();
        }
        return new BitalinoStateDisconnected();
    }
}
