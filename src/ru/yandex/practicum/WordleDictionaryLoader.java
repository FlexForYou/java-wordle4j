package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {


    public static List<String> createDictionaryForGame(String filename) throws IOException {
       // "D:\\Java\\java-wordle4j\\words_ru.txt"
        List<String> dictionaryForGame = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() == 5) {
                    dictionaryForGame.add(line.replace('ё', 'е').trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            FileWriter writer = new FileWriter("D:\\Java\\java-wordle4j\\Log.txt", true);
            writer.write("Произошла ошибка во время чтения файла "+ filename +":"+  e.getMessage() + "\n");
            writer.close();
        }
        return dictionaryForGame;
    }

}


