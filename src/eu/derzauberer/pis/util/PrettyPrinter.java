package eu.derzauberer.pis.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class PrettyPrinter extends DefaultPrettyPrinter {

	private static final long serialVersionUID = 1L;
	
	public PrettyPrinter() {
		final DefaultIndenter indenter = new DefaultIndenter("\t", "\n");
		indentArraysWith(indenter);
		indentObjectsWith(indenter);
	}

	@Override
    public DefaultPrettyPrinter createInstance() {
        return new PrettyPrinter();
        
    }

    @Override
    public void writeObjectFieldValueSeparator(JsonGenerator generator) throws IOException {
        generator.writeRaw(": ");
    }
	
}
