package workyfie.github.de.workyfie.application.bitalino.state;

import info.plux.pluxapi.bitalino.BITalinoCommunication;

public interface IBitalinoState {
    IBitalinoState start(BITalinoCommunication bitalino, int[] channels, int samplerate) throws EBitalinoStateException;
    IBitalinoState stop(BITalinoCommunication bitalino) throws  EBitalinoStateException;
    IBitalinoState connect(BITalinoCommunication bitalino, String macAdresse) throws  EBitalinoStateException;
    IBitalinoState disconnect(BITalinoCommunication bitalino) throws EBitalinoStateException;
}
