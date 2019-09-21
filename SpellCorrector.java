package spell;

import java.io.IOException;
import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    Trie myDictionary = new Trie();
    int delDistance;
    int transDistance;
    int alterDistance;
    int insertDistance;

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner scanner = new Scanner(new File(dictionaryFileName));
        while (scanner.hasNext()) {
            myDictionary.add(scanner.next());
        }
        scanner.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        // indicate that the word is spelled correctly if the Trie finds inputWord
        inputWord = inputWord.toLowerCase();
        if (myDictionary.find(inputWord) != null) {
            return inputWord;
        }
        else {
            // run check on all words 1 distance away from inputWord
            List<String> distOneWords = new ArrayList<String>();
            distOneWords.addAll(getDeletion(inputWord));
            distOneWords.addAll(getTransposition(inputWord));
            distOneWords.addAll(getAlteration(inputWord));
            distOneWords.addAll(getInsertion(inputWord));

            List<String> foundOneWords = new ArrayList<String>();
            List<Integer> foundOneCounts = new ArrayList<Integer>();
            for (String word : distOneWords) {
                if (myDictionary.find(word) != null) {
                    foundOneWords.add(word);
                    foundOneCounts.add(myDictionary.find(word).getValue());
                }
            }
            if (!foundOneWords.isEmpty()) {
                // check if a word has more counts than the rest
                int max = 0;
                int numMaxes = 1;
                String frequentWord = new String();
                for (int i = 0; i < foundOneCounts.size(); i++) {
                    if (foundOneCounts.get(i) > max) {
                        max = foundOneCounts.get(i);
                        numMaxes = 1;
                        frequentWord = foundOneWords.get(i);
                    }
                    else if (foundOneCounts.get(i) == max) {
                        if (!foundOneWords.get(i).equals(frequentWord)) {
                            numMaxes++;
                        }
                    }
                }
                if (numMaxes == 1) {
                    return frequentWord;
                }

                // sort alphabetically and return first
                java.util.Collections.sort(foundOneWords);
                return foundOneWords.get(0);
            }

            // run check on all words 2 distance away from inputWord
            List<String> distTwoWords = new ArrayList<String>();
            for (String word : distOneWords) {
                distTwoWords.addAll(getDeletion(word));
                distTwoWords.addAll(getTransposition(word));
                distTwoWords.addAll(getAlteration(word));
                distTwoWords.addAll(getInsertion(word));
            }

            List<String> foundTwoWords = new ArrayList<String>();
            List<Integer> foundTwoCounts = new ArrayList<Integer>();
            for (String word : distTwoWords) {
                if (myDictionary.find(word) != null) {
                    foundTwoWords.add(word);
                    foundTwoCounts.add(myDictionary.find(word).getValue());
                }
            }
            if (!foundTwoWords.isEmpty()) {
                // check if a word has more counts
                int max = 0;
                int numMaxes = 1;
                String frequentWord = new String();
                for (int i = 0; i < foundTwoCounts.size(); i++) {
                    if (foundTwoCounts.get(i) > max) {
                        max = foundTwoCounts.get(i);
                        numMaxes = 1;
                        frequentWord = foundTwoWords.get(i);
                    }
                    else if (foundTwoCounts.get(i) == max) {
                        if (!foundTwoWords.get(i).equals(frequentWord)) {
                            numMaxes++;
                        }
                    }
                }
                if (numMaxes == 1) {
                    return frequentWord;
                }

                // sort alphabetically and return first
                java.util.Collections.sort(foundTwoWords);
                return foundTwoWords.get(0);
            }
            else {
                return null;
            }
        }
    }

    private List<String> getDeletion(String input) {
        List<String> delList = new ArrayList<String>();
        for (int i = 0; i < input.length(); i++) {
            StringBuilder sb = new StringBuilder(input);
            sb.deleteCharAt(i);
            delList.add(sb.toString());
        }
        return delList;
    }

    private List<String> getTransposition(String input) {
        List<String> transList = new ArrayList<String>();
        for (int i = 0; i < (input.length() - 1); i++) {
            StringBuilder sb = new StringBuilder(input);
            char tempChar = sb.charAt(i);
            sb.deleteCharAt(i);
            sb.insert((i + 1), tempChar);
            transList.add(sb.toString());
        }
        return transList;
    }

    private List<String> getAlteration(String input) {
        List<String> alterList = new ArrayList<String>();
        for (int i = 0; i < input.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder sb = new StringBuilder(input);
                char currentLetter = (char) (j + 97);
                if (currentLetter != input.charAt(i)) {
                    sb.deleteCharAt(i);
                    sb.insert(i, currentLetter);
                    alterList.add(sb.toString());
                }
            }
        }
        return alterList;
    }

    private List<String> getInsertion(String input) {
        List<String> insertList = new ArrayList<String>();
        for (int i = 0; i <= input.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder sb = new StringBuilder(input);
                char currentLetter = (char) (j + 97);
                sb.insert(i, currentLetter);
                insertList.add(sb.toString());
            }
        }
        return insertList;
    }
}
