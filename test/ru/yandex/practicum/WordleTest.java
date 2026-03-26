package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleGame wordleGame;
    private WordleDictionary mockDictionary;

    @BeforeEach
    void setUp() {
        mockDictionary = new WordleDictionary(Arrays.asList("аббат", "астра", "банан"));
        wordleGame = new WordleGame(mockDictionary);
    }
// успешное угадывание слова
    @Test
    void testSuccessfulGuess() throws IOException {
        String result = wordleGame.comparingWordWithAnswer(wordleGame.getAnswer());
        assertEquals("+++++", result);
        assertEquals(Status.COMPLETED, wordleGame.getStatus());
        assertEquals(6, wordleGame.getSteps()); // шаги не уменьшаются при победе
    }
//частичное совпадение букв
    @Test
    void testPartialMatch() throws IOException {
        wordleGame.setAnswer("аббат");
        String result1 = wordleGame.comparingWordWithAnswer("астра");
        assertEquals("+-^-^", result1);
        assertEquals(5, wordleGame.getSteps());

        String result2 = wordleGame.comparingWordWithAnswer("банан");
        assertEquals("+^^+^", result2);
        assertEquals(4, wordleGame.getSteps());
    }
    // получение подсказки
    @Test
    void testGetHelpFirstAttempt() throws IOException {
        String hint = wordleGame.getHelp();
        assertTrue(mockDictionary.getWords().contains(hint));
    }

    @Test
    void testGetHelpAfterAttempt() throws IOException {
        wordleGame.comparingWordWithAnswer("астра"); // создаём историю попыток
        String hint = wordleGame.getHelp();
        assertNotNull(hint);
        assertTrue(hint.length() == 5);
    }
//корректность логики подсказок
    @Test
    void testHelpLogicWithKnownLetters() throws IOException {
        // Даём попытку с частичным совпадением
        wordleGame.comparingWordWithAnswer("астра");

        // Подсказка должна учитывать, что первая буква — «а»
        String hint = wordleGame.getHelp();

        // Проверяем, что подсказка соответствует известным данным
        boolean matchesKnownInfo = true;
        if (wordleGame.getHintSymbols()[0] == '+') {
            matchesKnownInfo = hint.charAt(0) == 'а';
        }
        assertTrue(matchesKnownInfo);
    }
}
