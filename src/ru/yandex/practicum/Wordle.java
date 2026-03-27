package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.InvalidWordLengthException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) throws IOException {
        boolean playAgain = false;
        WordleDictionary wordleDictionary = null;
        WordleGame wordleGame = null;

        String wordsFile = "resources/words_ru.txt";
        String logFile = "resources/Log.txt";

        FileWriter writer = new FileWriter(logFile, true);
        Scanner scanner = new Scanner(System.in);
        wordleDictionary = new WordleDictionary(
                WordleDictionaryLoader.createDictionaryForGame(wordsFile, writer));
        do {

            try {
                // Загрузка словаря

                wordleGame = new WordleGame(wordleDictionary);

                wordleGame.printRules();


                while (wordleGame.getSteps() > 0 && !wordleGame.getStatus().equals(Status.COMPLETED)) {
                    System.out.println("Введите слово (или нажмите Enter для подсказки):");

                    try {
                        String userInput = scanner.nextLine();
                        userInput = userInput.trim().toLowerCase();

                        if (userInput.isEmpty()) {
                            // Получение подсказки
                            String word = wordleGame.getHelp(writer);
                            System.out.println("Слово-подсказка: " + word);
                            System.out.println(wordleGame.comparingWordWithAnswer(word, writer));
                        } else {
                            // Обработка введённого слова
                            System.out.println(wordleGame.comparingWordWithAnswer(userInput, writer));
                        }

                    } catch (WordNotFoundInDictionary e) {
                        System.out.println("Данного слова нет в словаре. Попробуйте ввести другое слово.");

                        writer.write("Ошибка: " + e.getMessage() + "\n");

                    } catch (InvalidWordLengthException e) {

                        System.out.println("Слово должно содержать ровно 5 букв.");

                        writer.write("Ошибка: " + e.getMessage() + "\n");

                    } catch (Exception e) {
                        System.out.println("Неожиданная ошибка при обработке ввода");

                        writer.write("Неожиданная ошибка при обработке ввода: " + e.getMessage() + "\n");

                    }
                }

            } catch (IOException e) {

                writer.write("Ошибка загрузки словаря: файл не найден или недоступен. \n Проверьте путь к файлу: D:\\\\Java\\\\java-wordle4j\\\\words_ru.txt\" \n ");

                return;
            } catch (NullPointerException e) {

                writer.write("Ошибка инициализации игры: словарь не был загружен." + "\n");

                return;
            } catch (Exception e) {

                writer.write("Критическая ошибка при запуске игры: " + e.getMessage() + "\n");

                return;
            }

            // Вывод результата игры
            System.out.println("Игра закончена");
            if (wordleGame != null && wordleGame.getStatus().equals(Status.COMPLETED)) {
                System.out.println("Вы угадали слово: " + wordleGame.getAnswer());
            } else if (wordleGame != null) {
                System.out.println("Вы не угадали слово: " + wordleGame.getAnswer());
            }

            System.out.println("\nХотите сыграть ещё раз? (да/нет)");
            String choice = scanner.nextLine();
            playAgain = choice.equals("да") || choice.equals("yes") || choice.equals("y");

        } while (playAgain);
        System.out.println("Спасибо за игру!");
        writer.close();
    }

}