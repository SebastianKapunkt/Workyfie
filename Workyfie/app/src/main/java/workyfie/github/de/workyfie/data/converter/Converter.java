package workyfie.github.de.workyfie.data.converter;

public interface Converter<A, D> {

    D from(A value);

    A to(D value);
}
