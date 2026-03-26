package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WordleGame {


    private WordleDictionary dictionary;

    private Status status = Status.IN_PROCESS;
    private String answer;
    private int steps = 6;

    private List<String> userWords = new ArrayList<>();
    private char[] hintSymbols = {'-', '-', '-', '-', '-'};
    private boolean[] locationGuessedLetters = {false, false, false, false, false};

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
//        this.answer = dictionary.getRandomWord();
        this.answer = "аббат";

    }

    public char[] getHintSymbols() {
        return hintSymbols;
    }

    public Status getStatus() {
        return status;
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String comparingWordWithAnswer(String userWord) throws IOException {
        try {
            if (!dictionary.getWords().contains(userWord)) {
                throw new WordNotFoundInDictionary("Слова нет в словаре");
            }

            //Сначала точные совпадения
            for (int i = 0; i < 5; i++) {
                if (userWord.charAt(i) == (this.answer.charAt(i))) {
                    hintSymbols[i] = '+';
                    this.locationGuessedLetters[i] = true;
                }
            }
            //Проверка на победу
            if (new String(hintSymbols).equals("+++++")) {
                this.status = Status.COMPLETED;
                return new String(hintSymbols);
            }

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (!locationGuessedLetters[j] && userWord.charAt(j) == (this.answer.charAt(i))) {

                        hintSymbols[j] = '^';
                        this.locationGuessedLetters[j] = true;
                        break;
                    }
                }
            }
            steps--;
            this.userWords.add(userWord);
            return new String(hintSymbols);

        } catch (WordNotFoundInDictionary e) {
            FileWriter writer = new FileWriter("D:\\Java\\java-wordle4j\\Log.txt", true);
            writer.write("Ошибка словаря: " + userWord + ":" + e.getMessage() + "\n");
            writer.close();
            throw e;
        } catch (IllegalArgumentException e) {
            FileWriter writer = new FileWriter("D:\\Java\\java-wordle4j\\Log.txt", true);
            writer.write("Некорректный ввод: " + e.getMessage() + "\n");
            writer.close();
            throw e;
        } catch (Exception e) {
            FileWriter writer = new FileWriter("D:\\Java\\java-wordle4j\\Log.txt", true);
            writer.write("Неожиданная ошибка: " + e.getMessage() + "\n");
            writer.close();
            throw new RuntimeException("Произошла непредвиденная ошибка в процессе сравнения слов", e);
        }


    }


    public void printRules() {
        System.out.println("Добро пожаловать в игру Wordle");
        System.out.println("Угадайте слово из 5 букв!");
        System.out.println("Правила маркировки:");
        System.out.println("- — буквы нет в загаданном слове");
        System.out.println("+ — буква есть и на правильной позиции");
        System.out.println("^ — буква есть, но на другой позиции");
        System.out.println("Для вызова подсказки нажмите ENTER");
    }


    public String getHelp() throws IOException {
        try {
            if (userWords.isEmpty()) {
                return dictionary.getRandomWord();

            }
            char[] correctPositions = new char[5]; // '+' позиции
            Set<Character> presentLetters = new HashSet<>(); // '+' и '^'
            Set<Character> absentLetters = new HashSet<>(); // '-'

            for (int i = 0; i < 5; i++) {
                char symbol = hintSymbols[i];

                if (symbol == '+') {
                    correctPositions[i] = userWords.get(userWords.size() - 1).charAt(i);
                    presentLetters.add(correctPositions[i]);
                } else if (symbol == '^') {
                    char c = userWords.get(userWords.size() - 1).charAt(i);
                    presentLetters.add(c);
                } else {
                    char c = userWords.get(userWords.size() - 1).charAt(i);
                    if (!presentLetters.contains(c)) {
                        absentLetters.add(c);
                    }
                }
            }

            List<String> candidates = new ArrayList<>();

            for (String word : dictionary.getWords()) {
                boolean rightWord = true;

                // Проверка '+'
                for (int i = 0; i < 5; i++) {
                    if (correctPositions[i] != '\0' &&
                            word.charAt(i) != correctPositions[i]) {
                        rightWord = false;
                        break;
                    }
                }

                if (!rightWord) continue;

                // Проверка '^'
                for (int i = 0; i < 5; i++) {
                    if (hintSymbols[i] == '^') {
                        char c = userWords.get(userWords.size() - 1).charAt(i);

                        // буква должна быть, но не здесь
                        if (word.indexOf(c) == -1 || word.charAt(i) == c) {
                            rightWord = false;
                            break;
                        }
                    }
                }

                if (!rightWord) continue;

                // Проверка отсутствующих букв
                for (char c : absentLetters) {
                    if (word.indexOf(c) != -1) {
                        rightWord = false;
                        break;
                    }
                }

                if (!rightWord) continue;

                candidates.add(word);


            }
            Random random = new Random();
            return candidates.get(random.nextInt(candidates.size()));

        } catch (RuntimeException e) {

            FileWriter writer = new FileWriter("D:\\Java\\java-wordle4j\\Log.txt", true);
            writer.write("Ошибка при получении подсказки: " + e.getMessage() + "\n");
            writer.close();
            throw e;
        } catch (Exception e) {
            FileWriter writer = new FileWriter("D:\\Java\\java-wordle4j\\Log.txt", true);
            writer.write("Неожиданная ошибка при получении подсказки: " + e.getMessage() + "\n");
            writer.close();
            throw new RuntimeException("Произошла непредвиденная ошибка при генерации подсказки", e);
        }
    }


}
