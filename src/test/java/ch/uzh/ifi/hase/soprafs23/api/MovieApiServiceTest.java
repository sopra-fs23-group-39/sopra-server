package ch.uzh.ifi.hase.soprafs23.api;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This class contains only several additional tests, since most of the methods of the class are tested together with
// QuestionService class's methods (in QuestionServiceTest)
class MovieApiServiceTest {

    private final MovieApiService movieApiService = new MovieApiService();

    @Test
    void testCheckIfFourItemsInList() {
        List<String> additionalItems = new ArrayList<>(Arrays.asList("Gangs of New York", "Aviator", "Interstellar"));

        // Case "<4"
        List<String> listToCheck1 = new ArrayList<>(Arrays.asList("Interstellar", "Shutter Island", "Django Unchained"));
        List<String> result1 = movieApiService.checkIfFourItemsInList(listToCheck1, additionalItems);
        assertEquals(5, result1.size());
        assertTrue(result1.containsAll(additionalItems));
        assertTrue(result1.containsAll(listToCheck1));

        // Case ">=4"
        List<String> listToCheck2 = new ArrayList<>(Arrays.asList("Interstellar", "Shutter Island", "Django Unchained", "Inception"));
        List<String> result2 = movieApiService.checkIfFourItemsInList(listToCheck2, additionalItems);
        assertEquals(listToCheck2, result2);
    }
}
