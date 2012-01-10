package org.encog.examples.proben;

import org.encog.EncogError;

public class ProBenError extends EncogError {

	public ProBenError(String msg, Throwable t) {
		super(msg, t);
	}

	public ProBenError(Throwable t) {
		super(t);
	}

	public ProBenError(String msg) {
		super(msg);
	}

}
