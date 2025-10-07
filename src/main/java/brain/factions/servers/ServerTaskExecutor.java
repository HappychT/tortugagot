package brain.factions.servers;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerTaskExecutor {

    private static final ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();


    public static void addScheduledTask(Runnable task) {
        taskQueue.add(task);
    }

    public static void executeQueuedTasks() {
        while (!taskQueue.isEmpty()) {
            Runnable task = taskQueue.poll();
            if (task != null) {
                try {
                    task.run();
                } catch (Exception e) {
                    System.err.println("Error executing scheduled task");
                    e.printStackTrace();
                }
            }
        }
    }
}