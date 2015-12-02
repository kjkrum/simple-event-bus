package com.chalcodes.event.exec;

import javafx.application.Platform;

import java.util.concurrent.Executor;

/**
 * Queues tasks in the JavaFX application thread.
 *
 * @author Kevin Krumwiede
 */
public class JavaFxExecutor implements Executor {
	@Override
	public void execute(final Runnable command) {
		Platform.runLater(command);
	}
}
