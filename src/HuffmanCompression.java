import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCompression {
    // Узел дерева Хаффмана
    private static class Node implements Comparable<Node> {
        char ch;
        int freq;
        Node left, right;

        Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }

        Node(Node left, Node right) {
            this.ch = '\0'; // внутренний узел
            this.freq = left.freq + right.freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.freq, o.freq);
        }

        boolean isLeaf() {
            return left == null && right == null;
        }
    }

    private final Map<Character, String> huffmanCode = new HashMap<>();
    private Node root;

    /**
     * Строит дерево Хаффмана и коды символов для данного текста.
     */
    public void buildTree(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be empty");
        }

        // 1️⃣ Подсчёт частот символов
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // 2️⃣ Очередь с приоритетом по частоте
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }

        // 3️⃣ Построение дерева Хаффмана
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(left, right);
            pq.offer(parent);
        }

        root = pq.poll();

        // 4️⃣ Генерация кодов
        generateCodes(root, "");
    }

    private void generateCodes(Node node, String code) {
        if (node == null) return;

        if (node.isLeaf()) {
            huffmanCode.put(node.ch, code.isEmpty() ? "0" : code);
        }

        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    /**
     * Кодирует входной текст в битовую строку.
     */
    public String encode(String text) {
        if (root == null) buildTree(text);

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(huffmanCode.get(c));
        }
        return encoded.toString();
    }

    /**
     * Декодирует битовую строку обратно в текст.
     */
    public String decode(String encodedText) {
        if (root == null) {
            throw new IllegalStateException("Huffman tree is not built");
        }

        StringBuilder decoded = new StringBuilder();
        Node node = root;

        for (char bit : encodedText.toCharArray()) {
            node = (bit == '0') ? node.left : node.right;

            if (node.isLeaf()) {
                decoded.append(node.ch);
                node = root;
            }
        }
        return decoded.toString();
    }

    /**
     * Возвращает карту символов и их двоичных кодов.
     */
    public Map<Character, String> getCodes() {
        return new HashMap<>(huffmanCode);
    }
}
