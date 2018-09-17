package com.bcurry.videoserver.util;

public interface Defaultable<T> {
	public default void setDefaults(T e) {
		setId(e);
	}

	public void setId(T e);
}
