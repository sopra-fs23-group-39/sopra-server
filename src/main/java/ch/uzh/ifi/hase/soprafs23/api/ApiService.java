package ch.uzh.ifi.hase.soprafs23.api;

import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class ApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getJSONObject(String prefix, String itemId, String key) {
        return getJSONString(String.format("https://imdb-api.com/en/API/%s/%s/%s", prefix, key, itemId));
    }

    public List<String> getAllIds(String jsonObjectAsString) throws JsonProcessingException {
        List<String> listOfAllIds = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(jsonObjectAsString);
        JsonNode itemsNode = rootNode.path("items");

        for (JsonNode itemNode : itemsNode) {
            String movieId = itemNode.path("id").asText();
            listOfAllIds.add(movieId);
        }

        return listOfAllIds;
    }

    public String getRandomItem(String jsonObjectAsString) throws JsonProcessingException {
        List<String> listOfAllIds = getAllIds(jsonObjectAsString);
        Collections.shuffle(listOfAllIds);
        return listOfAllIds.get(0);
    }

    public String getJSONString(String externalAPI) {
        StringBuilder response = null;

        try {
            URL url = new URL(externalAPI);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        assert response != null;
        return response.toString();
    }

    public List<String> getFourItems(List<String> listToChooseFrom) {
        List<String> listSimilarItemsIds = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            listSimilarItemsIds.add(listToChooseFrom.get(i));
        }
        return listSimilarItemsIds;
    }

    public abstract String getImageLink(String jsonObjectAsString) throws JsonProcessingException;

    public abstract String getItemName(String jsonObjectAsString) throws JsonProcessingException;

}