package com.chalcodes.event.exec;

import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Looper;

/**
 * Queues tasks in the Android main thread.
 *
 * @author Kevin Krumwiede
 */
public class AndroidExecutor implements Executor {
	private final Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	public void execute(final Runnable command) {
		mHandler.post(command);		
	}
}
