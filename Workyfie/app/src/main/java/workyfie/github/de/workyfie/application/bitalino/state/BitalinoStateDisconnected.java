package workyfie.github.de.workyfie.application.bitalino.state;

import info.plux.pluxapi.bitalino.BITalinoCommunication;
import info.plux.pluxapi.bitalino.BITalinoErrorTypes;
import info.plux.pluxapi.bitalino.BITalinoException;

public class BitalinoStateDisconnected implements IBitalinoState {
    private static String errText = "Bitalino is disconnected";

    @Override
    public IBitalinoState start(BITalinoCommunication bitalino, int[] channels, int samplerate) throws EBitalinoStateException {
        throw new EBitalinoStateException(errText);
    }

    @Override
    public IBitalinoState stop(BITalinoCommunication bitalino) throws EBitalinoStateException {
        throw  new EBitalinoStateException(errText);
    }

    @Override
    public IBitalinoState connect(BITalinoCommunication bitalino, String macAdresse) throws BITalinoException {
            if (bitalino.connect(macAdresse)) {
                return new BitalinoStateConnected();
            }else{
                throw new BITalinoException(BITalinoErrorTypes.BT_DEVICE_NOT_CONNECTED);
            }
    }

    @Override
    public IBitalinoState disconnect(BITalinoCommunication bitalino) throws EBitalinoStateException {
        return this;
    }
}
