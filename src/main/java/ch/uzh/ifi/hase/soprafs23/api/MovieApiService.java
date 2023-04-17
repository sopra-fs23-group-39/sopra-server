package ch.uzh.ifi.hase.soprafs23.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieApiService extends ApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getJSONItemById(String itemId, String key) {
        return getJSONString(String.format("https://imdb-api.com/en/API/Title/%s/%s", key, itemId));
    }

    @Override
    public String getImageLink(String itemId, String key) throws JsonProcessingException {
        List<String> listOfImageLinks = new ArrayList<>();
        String response = getJSONString(String.format("https://imdb-api.com/API/Images/%s/%s", key, itemId));

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode itemsNode = rootNode.path("items");

        for (JsonNode itemNode : itemsNode) {
            String imageLink = itemNode.path("image").asText();
            listOfImageLinks.add(imageLink);
        }

        Collections.shuffle(listOfImageLinks.subList(0, 5));
        return listOfImageLinks.get(0);
    }

    @Override
    public String getItemName(String itemId, String key) throws JsonProcessingException {
        String response = getJSONItemById(itemId, key);

        JsonNode rootNode = objectMapper.readTree(response);

        return rootNode.path("title").asText();
    }

    public List<String> getSimilarItems(String itemId, String key) throws JsonProcessingException {
        List<String> listOfSimilarMovies = new ArrayList<>();
        String response = getJSONItemById(itemId, key);

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode itemsNode = rootNode.path("similars");

        for (JsonNode itemNode : itemsNode) {
            String title = itemNode.path("title").asText();
            listOfSimilarMovies.add(title);
        }

        Collections.shuffle(listOfSimilarMovies);

        List<String> threeRandomMovies = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            threeRandomMovies.add(listOfSimilarMovies.get(i));
        }

        return threeRandomMovies;
    }
}
