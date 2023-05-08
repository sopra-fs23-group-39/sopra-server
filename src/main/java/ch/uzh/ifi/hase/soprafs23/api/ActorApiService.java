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
    public String getImageLink(String JSONObjectAsString) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(JSONObjectAsString);
        return rootNode.path("image").asText();
    }

    @Override
    public String getItemName(String JSONObjectAsString) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(JSONObjectAsString);
        return rootNode.path("name").asText();
    }

    private List<String> getSimilarItemsIds(List<String> listToChooseFrom) {
        List<String> listToChooseFrom2 = new ArrayList<>(listToChooseFrom);
        List<String> listSimilarItemsIds = new ArrayList<>();

        Collections.shuffle(listToChooseFrom2);

        for (int i = 0; i < 3; i++) {
            listSimilarItemsIds.add(listToChooseFrom2.get(i));
        }
        return listSimilarItemsIds;
    }

    public List<String> getSimilarItems(List<String> listToChooseFrom, String key) throws JsonProcessingException {
        List<String> listSimilarItemsIds = getSimilarItemsIds(listToChooseFrom);
        List<String> listSimilarItemsNames = new ArrayList<>();
        for (String id : listSimilarItemsIds) {
            String JSONObject = getJSONObject("Name", id, key);
            String itemName = getItemName(JSONObject);
            listSimilarItemsNames.add(itemName);
        }
        return listSimilarItemsNames;
    }

}