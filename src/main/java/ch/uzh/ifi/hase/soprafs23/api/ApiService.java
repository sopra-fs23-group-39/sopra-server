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

    public List<String> getAllMovieIds(String key) throws JsonProcessingException {
        List<String> listOfMovieIds = new ArrayList<>();
        String response = getJSONString(String.format("https://imdb-api.com/en/API/Top250Movies/%s", key));

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode itemsNode = rootNode.path("items");

        int year = 1989;
        for (JsonNode itemNode : itemsNode) {
            if (Integer.parseInt(itemNode.path("year").asText()) > year) {
                String movieId = itemNode.path("id").asText();
                listOfMovieIds.add(movieId);
            }
        }

        return listOfMovieIds;
    }

    public String getRandomItem (List <String> listOfItems) {
        Collections.shuffle(listOfItems);
        return listOfItems.get(0);
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

    public abstract String getImageLink(String itemId, String key) throws JsonProcessingException;

    public abstract String getItemName(String itemId, String key) throws JsonProcessingException;

    public abstract String getJSONItemById(String itemId, String key) throws JsonProcessingException;

}