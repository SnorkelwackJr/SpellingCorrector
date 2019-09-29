package spell;

public class Trie implements ITrie {
    TrieNode root = new TrieNode();
    int numNodes = 1;
    int numWords = 0;
    int hashTotal = 0;
    boolean isEqual = true;

    @Override
    public void add(String word) {
        TrieNode currentNode = root;

        for (int i = 0; i < word.length(); i++) {
            if (currentNode.nodes[word.charAt(i) - 'a'] == null) {
                currentNode.nodes[word.charAt(i) - 'a'] = new TrieNode();
                currentNode.nodes[word.charAt(i) - 'a'].nodeChar = word.charAt(i);
                numNodes++;

                if (i == (word.length() - 1)) {
                    if (currentNode.nodes[word.charAt(i) - 'a'].count == 0) {
                        currentNode.nodes[word.charAt(i) - 'a'].completeWord = word;
                        numWords++;
                    }
                    currentNode.nodes[word.charAt(i) - 'a'].count++;
                }
                else {
                    currentNode = currentNode.nodes[word.charAt(i) - 'a'];
                }
            }
            else {
                if (i == (word.length() - 1)) {
                    if (currentNode.nodes[word.charAt(i) - 'a'].count == 0) {
                        currentNode.nodes[word.charAt(i) - 'a'].completeWord = word;
                        numWords++;
                    }
                    currentNode.nodes[word.charAt(i) - 'a'].count++;
                }
                else {
                    currentNode = currentNode.nodes[word.charAt(i) - 'a'];
                }
            }
        }

        hashTotal += word.hashCode();
    }

    @Override
    public INode find(String word) {
        TrieNode currentNode = root;

        for (int i = 0; i < word.length(); i++) {
            if (currentNode.nodes[word.charAt(i) - 'a'] != null) {
                if (i == (word.length() - 1)) {
                    if (currentNode.nodes[word.charAt(i) - 'a'].count >= 1) {
                        return currentNode.nodes[word.charAt(i) - 'a'];
                    }
                }

                // set currentNode to the next letter's node
                currentNode = currentNode.nodes[word.charAt(i) - 'a'];
            }
            else {
                return null;
            }
        }

        return null;
    }

    @Override
    public int getWordCount() {
        return numWords;
    }

    @Override
    public int getNodeCount() {
        return numNodes;
    }

    public String toString() { return toString(root); }

    private String toString(TrieNode root) {
        StringBuilder sb = new StringBuilder();

        for (TrieNode node : root.nodes) {
            if (node != null) {
                if (node.count >= 1) {
                    sb.append(node.completeWord).append("\n");
                }
                sb.append(toString(node));
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trie compTrie = (Trie) o;
        if ((numNodes == compTrie.numNodes) && (numWords == compTrie.numWords)) {
            for (int i = 0; i < 26; i++) {
                if (root.nodes[i] != null) {
                    if (compTrie.root.nodes[i] == null) {
                        return false;
                    }
                    else {
                        compareNodes(root.nodes[i], compTrie.root.nodes[i]);
                    }
                }
            }

            return isEqual;
        }
        return false;
    }

    private boolean compareNodes(TrieNode node, TrieNode compNode) {
        // run recursively
        for (int i = 0; i < 26; i++) {
            if (node.nodes[i] != null) {
                if (compNode.nodes[i] == null) {
                    isEqual = false;
                    return false;
                }
                else {
                    compareNodes(node.nodes[i], compNode.nodes[i]);
                }
            }
        }

        // compare count and nodeChar
        if ((node.count == compNode.count) && (node.nodeChar == compNode.nodeChar)) {
            return true;
        }
        else {
            isEqual = false;
            return false;
        }
    }

    @Override
    public int hashCode() { return hashTotal; }
}
