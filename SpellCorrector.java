package spell;

import java.io.IOException;
import java.io.File;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    Trie myDictionary = new Trie();

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
        inputWord = inputWord.toLowerCase();
        if (myDictionary.find(inputWord) != null) {
            return inputWord;
        }
        else {
            // run for distOneWords
            List<String> distOneWords = new ArrayList<>();
            distOneWords.addAll(getDeletion(inputWord));
            distOneWords.addAll(getTransposition(inputWord));
            distOneWords.addAll(getAlteration(inputWord));
            distOneWords.addAll(getInsertion(inputWord));

            List<String> foundOneWords = new ArrayList<>();
            List<Integer> foundOneCounts = new ArrayList<>();
            for (String word : distOneWords) {
                if (myDictionary.find(word) != null) {
                    foundOneWords.add(word);
                    foundOneCounts.add(myDictionary.find(word).getValue());
                }
            }
            if (!foundOneWords.isEmpty()) {
                // check for most frequency
                int max = 0;
                int currentIndex = 0;
                ArrayList<Integer> maxIndices = new ArrayList<>();
                for (String word : foundOneWords) {
                    if (foundOneCounts.get(currentIndex) > max) {
                        maxIndices.clear();
                        max = foundOneCounts.get(currentIndex);
                        maxIndices.add(currentIndex);
                    }
                    else if (foundOneCounts.get(currentIndex) == max) {
                        maxIndices.add(currentIndex);
                    }
                    currentIndex++;
                }
                if (maxIndices.size() == 1) {
                    return foundOneWords.get(maxIndices.get(0));
                }

                // if need be, return first alphabetical
                java.util.Collections.sort(foundOneWords);
                return foundOneWords.get(0);
            }

            // run for distTwoWords
            List<String> distTwoWords = new ArrayList<>();
            for (String word : distOneWords) {
                distTwoWords.addAll(getDeletion(word));
                distTwoWords.addAll(getTransposition(word));
                distTwoWords.addAll(getAlteration(word));
                distTwoWords.addAll(getInsertion(word));
            }

            List<String> foundTwoWords = new ArrayList<>();
            List<Integer> foundTwoCounts = new ArrayList<>();
            for (String word : distTwoWords) {
                if (myDictionary.find(word) != null) {
                    foundTwoWords.add(word);
                    foundTwoCounts.add(myDictionary.find(word).getValue());
                }
            }
            if (!foundTwoWords.isEmpty()) {
                // check for most frequency
                int max = 0;
                int currentIndex = 0;
                ArrayList<Integer> maxIndices = new ArrayList<>();
                ArrayList<String> maxValues = new ArrayList<>();
                for (String word : foundTwoWords) {
                    if (foundTwoCounts.get(currentIndex) > max) {
                        maxIndices.clear();
                        max = foundTwoCounts.get(currentIndex);
                        maxIndices.add(currentIndex);
                        maxValues.add(foundTwoWords.get(currentIndex));
                    }
                    else if (foundTwoCounts.get(currentIndex) == max) {
                        if (!maxValues.contains(foundTwoWords.get(currentIndex))) {
                            maxIndices.add(currentIndex);
                        }
                    }
                    currentIndex++;
                }
                if (maxIndices.size() == 1) {
                    return foundTwoWords.get(maxIndices.get(0));
                }

                // if need be, return first alphabetical
                java.util.Collections.sort(foundTwoWords);
                return foundTwoWords.get(0);
            }
        }

        return null;
    }

    private List<String> getDeletion(String input) {
        List<String> delWords = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            StringBuilder sb = new StringBuilder(input);
            sb.deleteCharAt(i);
            delWords.add(sb.toString());
        }

        return delWords;
    }

    private List<String> getTransposition(String input) {
        List<String> transWords = new ArrayList<>();
        for (int i = 0; i < (input.length() - 1); i++) {
            StringBuilder sb = new StringBuilder(input);
            char moveChar = sb.charAt(i);
            sb.insert((i + 2), moveChar);
            sb.deleteCharAt(i);
            transWords.add(sb.toString());
        }

        return transWords;
    }

    private List<String> getAlteration(String input) {
        List<String> altWords = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder sb = new StringBuilder(input);
                char newChar = (char) (j + 97);
                sb.deleteCharAt(i);
                sb.insert(i, newChar);
                altWords.add(sb.toString());
            }
        }

        return altWords;
    }

    private List<String> getInsertion(String input) {
        List<String> insertWords = new ArrayList<>();
        for (int i = 0; i <= input.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder sb = new StringBuilder(input);
                char newChar = (char) (j + 97);
                sb.insert(i, newChar);
                insertWords.add(sb.toString());
            }
        }

        return insertWords;
    }
}
