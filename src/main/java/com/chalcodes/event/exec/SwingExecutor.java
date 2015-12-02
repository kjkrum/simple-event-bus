package com.chalcodes.event.exec;

import javax.swing.*;
import java.util.concurrent.Executor;

/**
 * Queues tasks in the AWT/Swing event dispatch thread.
 *
 * @author Kevin Krumwiede
 */
public class SwingExecutor implements Executor {
	@Override
	public void execute(Runnable command) {
		SwingUtilities.invokeLater(command);
	}
}
