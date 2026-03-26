package ru.yandex.practicum;


import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */


public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return words;
    }

    public void addNewWord(String word){
        if(!(word.length() >5)){
            this.words.add(word.trim().toLowerCase().replace('ё', 'е'));
        }else{
            System.out.println("Новое слово должно состоять из 5ти букв!");
        }
    }

    public void deleteWord(String word){
        String line= word.trim().toLowerCase().replace('ё', 'е');
        if(words.contains(line)){
            words.remove(line);
        } else {
            System.out.println("Слово уже удалено");
        }


    }

    public String getRandomWord() {
        Random random = new Random();
        return  words.get(random.nextInt(words.size()));
    }


}
