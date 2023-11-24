package eu.derzauberer.pis.converter;


public interface FormConverter<M, F> {
	
	F convertToForm(M model);
	
	M convertToModel(F form);
	
	M convertToModel(M model, F form);

}
