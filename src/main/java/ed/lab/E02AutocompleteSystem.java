package ed.lab; //Kevin Palencia

import java.util.*;

public class E02AutocompleteSystem {

    private class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> sentenceFreq = new HashMap<>();
    }

    private final TrieNode root;
    private TrieNode currentNode;
    private final StringBuilder currentSearch;

    public E02AutocompleteSystem(String[] sentences, int[] times) {
        root = new TrieNode();
        currentNode = root;
        currentSearch = new StringBuilder();

        for (int i = 0; i < sentences.length; i++) {
            insert(sentences[i], times[i]);
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            String sentence = currentSearch.toString();
            insert(sentence, 1);
            currentSearch.setLength(0);
            currentNode = root;
            return new ArrayList<>();
        }

        currentSearch.append(c);
        if (currentNode != null) {
            currentNode = currentNode.children.get(c);
        }

        if (currentNode == null) {
            return new ArrayList<>();
        }

        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(
                (a, b) -> a.getValue().equals(b.getValue()) ?
                        a.getKey().compareTo(b.getKey()) :
                        b.getValue() - a.getValue()
        );

        for (Map.Entry<String, Integer> entry : currentNode.sentenceFreq.entrySet()) {
            heap.offer(entry);
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < 3 && !heap.isEmpty(); i++) {
            result.add(heap.poll().getKey());
        }

        return result;
    }

    private void insert(String sentence, int count) {
        TrieNode node = root;
        for (char c : sentence.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            node.sentenceFreq.put(sentence, node.sentenceFreq.getOrDefault(sentence, 0) + count);
        }
    }
}
