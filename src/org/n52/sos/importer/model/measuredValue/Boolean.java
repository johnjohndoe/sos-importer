package org.n52.sos.importer.model.measuredValue;

import org.n52.sos.importer.Parseable;

public class Boolean extends MeasuredValue implements Parseable {

	@Override
	public String toString() {
		return "Boolean";
	}
	
	@Override
	public Object parse(String s) {
		s = s.trim();
		if (s.equals("0") || s.equals("false") || s.equals("False")) return false;
		else if (s.equals("1") || s.equals("true") || s.equals("true")) return true;
		else throw new NumberFormatException();
	}
}