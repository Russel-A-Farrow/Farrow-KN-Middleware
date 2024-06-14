package com.farrow.knmiddleware.queuerunners;

public abstract class AbstractQueueRunner {
	public abstract String getTypeName();
	
	public abstract Object convert();
	
	public abstract void send();
}
