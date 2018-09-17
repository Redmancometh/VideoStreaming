package com.bcurry.videoserver.util;

import com.bcurry.videoserver.VideoServer;
import com.google.common.base.Function;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpecialFuture<T> {
	private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);
	private static ScheduledExecutorService main = VideoServer.main;
	private List<Consumer<T>> asyncTasks = new CopyOnWriteArrayList<>();
	private AtomicReference<T> cache = new AtomicReference<>();
	private List<Consumer<Exception>> exHandlers = new CopyOnWriteArrayList<>();
	private AtomicReference<Exception> exception = new AtomicReference<Exception>();
	private List<Consumer<T>> tasks = new CopyOnWriteArrayList<>();

	public SpecialFuture(Supplier<T> s) {
		supply(s);
	}

	private SpecialFuture() {

	}

	public static SpecialFuture<?> delayAsync(Runnable r, long ticks) {
		SpecialFuture<Class<Void>> sf = new SpecialFuture<>();
		main.schedule(() -> {
			runAsync(r);
			sf.supply(() -> void.class);
		}, ticks, TimeUnit.MILLISECONDS);
		return sf;
	}

	public static SpecialFuture<?> runAsync(Runnable r) {
		return supplyAsync(() -> {
			r.run();
			return void.class;
		});
	}

	private void supply(Supplier<T> s) {
		Future<?> handler = pool.submit(() -> {
			try {
				T t = s.get();
				cache.set(t);
				for (Consumer<T> task : asyncTasks) {
					pool.submit(() -> task.accept(t));
				}
				for (Consumer<T> task : tasks) {
					main.execute(() -> {
						try {
							task.accept(t);
						} catch (Exception e) {
							System.out.println("SpecialFuture encountered an error while executing a task!");
							e.printStackTrace();
						}
					});
				}
			} catch (Exception e) {
				exception.set(e);
				main.execute(() -> {
					if (exHandlers.size() == 0) {
						e.printStackTrace();
						return;
					}
					for (Consumer<Exception> ce : exHandlers) {
						ce.accept(e);
					}
				});
			}
		});
		pool.schedule(new Runnable() {
			public void run() {
				if (!handler.isDone()) {
					handler.cancel(true);
					RuntimeException ex = new RuntimeException("Your task is taking way too fucking long. Fix it.");
					exception.set(ex);
					if (exHandlers.size() == 0) {
						ex.printStackTrace();
						return;
					}
					for (Consumer<Exception> ce : exHandlers) {
						ce.accept(ex);
					}
				}
			}
		}, 10000, TimeUnit.MILLISECONDS);
	}

	public static <T> SpecialFuture<T> supplyAsync(Supplier<T> s) {
		return new SpecialFuture<>(s);
	}

	public static SpecialFuture<?> delaySync(Runnable r, long ticks) {
		SpecialFuture<Class<Void>> sf = new SpecialFuture<>();
		main.schedule(() -> {
			r.run();
			sf.supply(() -> void.class);
		}, ticks, TimeUnit.MILLISECONDS);
		return sf;
	}

	public static SpecialFuture<?> delaySync(Runnable r, long t, TimeUnit u) {
		SpecialFuture<Class<Void>> sf = new SpecialFuture<>();
		delayAsync(() -> {
			runSync(r);
			sf.supply(() -> void.class);
		}, t, u);
		return sf;
	}

	public static SpecialFuture<?> delayAsync(Runnable r, long t, TimeUnit u) {
		SpecialFuture<Class<Void>> sf = new SpecialFuture<>();
		pool.schedule(() -> {
			r.run();
			sf.supply(() -> void.class);
		}, t, u);
		return sf;
	}

	public static SpecialFuture<?> runSync(Runnable r) {
		return runAsync(() -> {
		}).thenRun(r);
	}

	public SpecialFuture<T> thenRunOn(Runnable r, Executor exec) {
		thenAccept(c -> r.run());
		return this;
	}

	public SpecialFuture<T> thenRun(Runnable r) {
		thenAccept(c -> r.run());
		return this;
	}

	/**
	 * This is a blocking task that will wait on the result of the object.
	 *
	 * @return
	 */
	public T get() {
		T t = cache.get();
		if (t != null) {
			return t;
		}
		BlockingQueue<T> queue = new ArrayBlockingQueue<>(1);
		thenAccept(queue::add);
		try {
			t = queue.poll(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		if (t == null) {
			throw new RuntimeException("Blocking task timed out!");
		}
		return t;
	}

	public SpecialFuture<T> thenAccept(Consumer<T> c) {
		if (cache.get() == null) {
			asyncTasks.add(c);
			return this;
		}
		c.accept(cache.get());
		return this;
	}

	public SpecialFuture<T> thenAcceptOn(Consumer<T> c, ExecutorService exec) {
		if (cache.get() == null) {
			asyncTasks.add(c);
			return this;
		}
		exec.execute(() -> c.accept(cache.get()));
		return this;
	}

	public SpecialFuture<T> handleExceptionOn(Consumer<Exception> handler, ExecutorService exec) {
		if (cache.get() != null) {
			return this;
		}
		Exception ex = exception.get();
		if (ex != null) {
			exec.execute(() -> handler.accept(ex));
			return this;
		}
		if (ex == null) {
			exHandlers.add(handler);
		}
		return this;
	}

	public SpecialFuture<T> handleException(Consumer<Exception> handler) {
		if (cache.get() != null) {
			return this;
		}
		Exception ex = exception.get();
		if (ex != null) {
			handler.accept(ex);
			return this;
		}
		if (ex == null) {
			exHandlers.add(handler);
		}
		return this;
	}

	public SpecialFuture<T> thenRunAsync(Runnable c) {
		if (cache.get() == null) {
			asyncTasks.add((t) -> c.run());
			return this;
		}
		pool.submit(() -> c.run());
		return this;
	}

	public SpecialFuture<T> thenAcceptAsync(Consumer<T> c) {
		if (cache.get() == null) {
			asyncTasks.add(c);
			return this;
		}
		pool.submit(() -> c.accept(cache.get()));
		return this;
	}

	public <U> SpecialFuture<U> thenApply(Function<T, U> func) {
		return SpecialFuture.supplyAsync(() -> {
			BlockingQueue<U> queue = new ArrayBlockingQueue<>(1);
			SpecialFuture.this.thenAccept((t) -> {
				queue.add(func.apply(t));
			});
			U u = null;
			try {
				u = queue.poll(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (u == null) {
				RuntimeException ex = new RuntimeException(
						"Function could not apply because the previous future timed out! Check above for errors");
				main.execute(() -> {
					ex.printStackTrace();
				});
				throw ex;
			}
			return u;
		});
	}
}
