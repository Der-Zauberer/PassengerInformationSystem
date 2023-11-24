package eu.derzauberer.pis.converter;

public interface DataConverter<M, D> {
	
	D convert(M model);

}
