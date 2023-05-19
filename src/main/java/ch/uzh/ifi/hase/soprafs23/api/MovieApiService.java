package ch.uzh.ifi.hase.soprafs23.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MovieApiService extends ApiService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<String> getImageArray(String jsonObjectAsString) throws JsonProcessingException {
        List<String> listOfImageLinks = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(jsonObjectAsString);
        JsonNode itemsNode = rootNode.path("items");

        for (JsonNode itemNode : itemsNode) {
            String imageLink = itemNode.path("image").asText();
            listOfImageLinks.add(imageLink);
        }

        return listOfImageLinks;
    }

    @Override
    public String getImageLink(String jsonObjectAsString) throws JsonProcessingException {
        List<String> listOfImageLinks = getImageArray(jsonObjectAsString);
        Collections.shuffle(listOfImageLinks.subList(0, 5));
        return listOfImageLinks.get(0);
    }

    public String getEmbedLink(String jsonObjectAsString) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonObjectAsString);
        return rootNode.path("videoId").asText();
    }

    @Override
    public String getItemName(String jsonObjectAsString) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonObjectAsString);
        return rootNode.path("title").asText();
    }

    public List<String> getSimilarItemsArray(String jsonObjectAsString) throws JsonProcessingException {
        List<String> listOfSimilarMovies = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(jsonObjectAsString);
        // "similars" here - it's not a typo
        JsonNode itemsNode = rootNode.path("similars");

        for (JsonNode itemNode : itemsNode) {
            String title = itemNode.path("title").asText();
            listOfSimilarMovies.add(title);
        }

        return listOfSimilarMovies;
    }

    public List<String> getSimilarItems(String jsonObjectAsString, List<String> additionalItems) throws JsonProcessingException {
        List<String> listOfSimilarMovies = getSimilarItemsArray(jsonObjectAsString);
        HashSet<String> hashSet = new HashSet<>(listOfSimilarMovies);
        List<String> listOfSimilarMoviesNoDuplicates = new ArrayList<>(hashSet);

        int listSize = listOfSimilarMoviesNoDuplicates.size();
        if (listSize < 4) {
            listOfSimilarMoviesNoDuplicates.addAll(additionalItems);
            HashSet<String> hashSet2 = new HashSet<>(listOfSimilarMoviesNoDuplicates);
            listOfSimilarMoviesNoDuplicates = new ArrayList<>(hashSet2);
        }

        return getFourItems(listOfSimilarMoviesNoDuplicates);
    }

}