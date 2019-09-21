package spell;

import java.util.ArrayList;
import java.util.Objects;

public class Trie implements ITrie {
    private TrieNode root = new TrieNode();
    private ArrayList<String> allWords = new ArrayList<String>();
    private int numNodes = 1;
    private int numWords = 0;
    int hashTotal = 0;
    boolean isEqual = true;

    @Override
    public void add(String word) {
        TrieNode currentNode = root;
        // add word
        for (int i = 0; i < word.length(); i++) {
            if (currentNode.nodes[word.charAt(i) - 'a'] == null) {
                currentNode.nodes[word.charAt(i) - 'a'] = new TrieNode();
                currentNode.nodes[word.charAt(i) - 'a'].nodeChar = word.charAt(i);

                // set isVisited to true for the next node and increment numNodes
                currentNode.nodes[word.charAt(i) - 'a'].isVisited = true;
                numNodes++;

                // increment count, then numWords if count == 0
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
                // increment count, then numWords if count == 0
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
        allWords.add(word);

        // add to string's hash value to hashTotal
        hashTotal += word.hashCode();
    }

    @Override
    public INode find(String word) {
        TrieNode target = root;
        for (int i = 0; i < word.length(); i++) {
            // return early if there is no node equal to the current letter
            if (target.nodes[word.charAt(i) - 'a'] == null) {
                return null;
            }
            else {
                // at the end of the word, check if there is at least 1 in count
                if (i == (word.length() - 1)) {
                    if (target.nodes[word.charAt(i) - 'a'].count >= 1) {
                        return target.nodes[word.charAt(i) - 'a'];
                    }
                    else {
                        return null;
                    }
                }
            }
            // update target's current position
            target = target.nodes[word.charAt(i) - 'a'];
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

    private String toString(TrieNode node) {
        StringBuilder builder = new StringBuilder();
        for (TrieNode currentNode : node.nodes) {
            if (currentNode != null) {
                if (currentNode.count >= 1) {
                    builder.append(currentNode.completeWord).append("\n");
                }
                builder.append(toString(currentNode));
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        // shallow comparison
        if (this == o) return true;

        // check class of object
        if (o == null || getClass() != o.getClass()) return false;

        // deep comparison
        Trie compTrie = (Trie) o;
        if ((numNodes == compTrie.numNodes) && (numWords == compTrie.numWords)) {
            for (int i = 0; i < root.nodes.length; i++) {
                if (root.nodes[i] != null) {
                    if (compTrie.root.nodes[i] == null) {
                        return false;
                    }
                    compareNodes(root.nodes[i], compTrie.root.nodes[i]);
                }
            }
            return isEqual;
        }
        return false;
    }

    private boolean compareNodes(TrieNode thisNode, TrieNode compNode) {
        // check for child nodes
        for (int i = 0; i < thisNode.nodes.length; i++) {
            if (thisNode.nodes[i] != null) {
                if (compNode.nodes[i] == null) {
                    isEqual = false;
                    return false;
                }
                compareNodes(thisNode.nodes[i], compNode.nodes[i]);
            }
        }

        // compare node values
        if ((thisNode.count == compNode.count) && (thisNode.nodeChar == compNode.nodeChar)) {
            return true;
        }
        else {
            isEqual = false;
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashTotal;
    }
}
