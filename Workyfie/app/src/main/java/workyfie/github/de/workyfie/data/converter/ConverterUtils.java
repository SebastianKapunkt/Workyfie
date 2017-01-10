package workyfie.github.de.workyfie.data.converter;

import java.util.ArrayList;
import java.util.List;

public class ConverterUtils {
    private ConverterUtils() {

    }

    public static <D, A> List<D> from(List<A> aList, Converter<A, D> converter) {
        List<D> tList = new ArrayList<>();

        for (A a : aList) {
            tList.add(converter.from(a));
        }

        return tList;
    }

    public static <D, A> List<A> to(List<D> dList, Converter<A, D> converter) {
        List<A> aList = new ArrayList<>();

        for (D d : dList) {
            aList.add(converter.to(d));
        }

        return aList;
    }
}
