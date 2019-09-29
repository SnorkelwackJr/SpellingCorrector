package spell;

public class TrieNode implements INode {
    TrieNode[] nodes = new TrieNode[26];
    int count = 0;
    char nodeChar;
    String completeWord = "";

    @Override
    public int getValue() { return count; }
}
