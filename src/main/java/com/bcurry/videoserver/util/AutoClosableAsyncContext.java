package com.bcurry.videoserver.util;

import java.lang.ref.WeakReference;

import javax.servlet.AsyncContext;

public class AutoClosableAsyncContext implements AutoCloseable {
	private WeakReference<AsyncContext> contextRef;

	public AutoClosableAsyncContext(AsyncContext context) {
		this.contextRef = new WeakReference(context);
	}

	public AsyncContext get() {
		return contextRef.get();
	}

	@Override
	public void close() throws Exception {
		contextRef.get().complete();
	}
}
