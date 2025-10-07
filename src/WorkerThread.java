import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkerThread {
    private final BlockingQueue<Runnable> taskQueue;
    private Thread worker;
    private volatile boolean running = false;

    public WorkerThread() {
        taskQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Запуск рабочего потока.
     */
    public synchronized void start() {
        if (running) return;
        running = true;

        worker = new Thread(() -> {
            try {
                while (running) {
                    // take() блокирует поток, пока нет задач
                    Runnable task = taskQueue.take();
                    try {
                        task.run();
                    } catch (Throwable t) {
                        t.printStackTrace(); // обработка исключений в задаче
                    }
                }
            } catch (InterruptedException e) {
                // поток прерван при остановке — выходим из цикла
                Thread.currentThread().interrupt();
            }
        }, "WorkerThread");

        worker.start();
    }

    /**
     * Добавление задачи в очередь.
     * @param task Задача, которую нужно выполнить.
     */
    public void post(Runnable task) {
        if (!running) {
            throw new IllegalStateException("WorkerThread not started");
        }
        taskQueue.offer(task);
    }

    /**
     * Остановка рабочего потока.
     */
    public synchronized void stop() {
        if (!running) return;
        running = false;
        worker.interrupt(); // разбудим, если он спит
    }

    /**
     * Статический метод для быстрого запуска WorkerThread.
     */
    public static WorkerThread runWorker() {
        WorkerThread worker = new WorkerThread();
        worker.start();
        return worker;
    }
}
