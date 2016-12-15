package workyfie.github.de.workyfie.application.bitalino.state;

import info.plux.pluxapi.bitalino.BITalinoCommunication;
import info.plux.pluxapi.bitalino.BITalinoException;

public class BitalinoStateRecording implements IBitalinoState {
    private static String errText = "Bitalino is recording";

    @Override
    public IBitalinoState start(BITalinoCommunication bitalino, int[] channels, int samplerate) throws EBitalinoStateException {
        return this;
    }

    @Override
    public IBitalinoState stop(BITalinoCommunication bitalino) throws EBitalinoStateException {
        try {
            if(bitalino.stop())
                return new BitalinoStateConnected();
        } catch (BITalinoException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public IBitalinoState connect(BITalinoCommunication bitalino, String macAdresse) throws EBitalinoStateException {
        throw new EBitalinoStateException(errText);
    }

    @Override
    public IBitalinoState disconnect(BITalinoCommunication bitalino) throws EBitalinoStateException {
        try {
            bitalino.disconnect();
            return new BitalinoStateDisconnected();
        } catch (BITalinoException e) {
            e.printStackTrace();
        }
        return this;
    }
}
