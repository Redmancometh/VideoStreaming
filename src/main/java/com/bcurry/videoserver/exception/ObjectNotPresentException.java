package com.bcurry.videoserver.exception;

public class ObjectNotPresentException extends Exception {

	private static final long serialVersionUID = 4976510636623464875L;

	public ObjectNotPresentException(String key, Class clazz) {
		super("Warning: Key " + key + " did not have a value in cache while trying to save an object of: " + clazz.getName());
	}
}
