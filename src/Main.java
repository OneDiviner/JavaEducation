

public class Main {

    static void quickSort() {
        int[] arr = {10, 7, 8, 9, 1, 5};
        System.out.println("До сортировки:");
        QuickSort.printArray(arr);

        QuickSort.quickSort(arr, 0, arr.length - 1);

        System.out.println("\nПосле сортировки:");
        QuickSort.printArray(arr);
    }

    static void workerThread() {
        WorkerThread worker = WorkerThread.runWorker();

        worker.post(() -> System.out.println("Задача 1 — " + Thread.currentThread().getName()));
        worker.post(() -> System.out.println("Задача 2 — " + Thread.currentThread().getName()));
        worker.post(() -> {
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                sum += i;
            }
            System.out.println("Задача 3: сумма = " + sum + " (" + Thread.currentThread().getName() + ")");
        });

        try {
            Thread.sleep(2000); // ждём завершения задач
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.stop();
    }

    static void huffmanCompression() {
        String text = "hello huffman";

        HuffmanCompression huffman = new HuffmanCompression();
        huffman.buildTree(text);

        String encoded = huffman.encode(text);
        String decoded = huffman.decode(encoded);

        System.out.println("Original: " + text);
        System.out.println("Encoded: " + encoded);
        System.out.println("Decoded: " + decoded);
        System.out.println("Codes: " + huffman.getCodes());
    }

    public static void main(String[] args) {
        quickSort();
        System.out.println("\n\n");
        workerThread();
        System.out.println("\n\n");
        huffmanCompression();
    }
}