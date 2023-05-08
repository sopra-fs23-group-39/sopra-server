package ch.uzh.ifi.hase.soprafs23.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActorApiService extends ApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getImageLink(String itemId, String key) throws JsonProcessingException {
        String response = getJSONString(String.format("https://imdb-api.com/API/Name/%s/%s", key, itemId));
        JsonNode rootNode = objectMapper.readTree(response);

        return rootNode.path("image").asText();
    }

    @Override
    public String getItemName(String itemId, String key) throws JsonProcessingException {
        String response = getJSONItemById(itemId, key);

        JsonNode rootNode = objectMapper.readTree(response);

        return rootNode.path("name").asText();
    }

    @Override
    public String getJSONItemById(String itemId, String key) {
        return getJSONString(String.format("https://imdb-api.com/API/Name/%s/%s", key, itemId));
    }

    public List<String> getSimilarItems(String key, List<String> listToChooseFrom) throws JsonProcessingException {
        List<String> listToChooseFrom2 = new ArrayList<>(listToChooseFrom);
        List<String> listSimilarItemsIds = new ArrayList<>();
        List<String> listSimilarItemsNames = new ArrayList<>();

        Collections.shuffle(listToChooseFrom2);

        for (int i = 0; i < 3; i++) {
            listSimilarItemsIds.add(listToChooseFrom2.get(i));
        }

        for (String id : listSimilarItemsIds) {
            String itemName = getItemName(id, key);
            listSimilarItemsNames.add(itemName);
        }
        return listSimilarItemsNames;
    }

}