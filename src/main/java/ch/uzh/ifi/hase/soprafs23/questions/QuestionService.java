package ch.uzh.ifi.hase.soprafs23.questions;

import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QuestionService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    //private final String key = "k_9tbfzilr";

    public ArrayList<String> getAllMovieIds() throws JsonProcessingException {
        ArrayList<String> listOfMovieIds = new ArrayList<>();
        String response = getJSONString("https://imdb-api.com/en/API/Top250Movies/k_9tbfzilr");

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode itemsNode = rootNode.path("items");

        for (JsonNode itemNode : itemsNode) {
            String movieId = itemNode.path("id").asText();
            listOfMovieIds.add(movieId);
        }

        return listOfMovieIds;
    }

    public ArrayList<String> getImageLinks (String movieId) throws JsonProcessingException {
        ArrayList<String> listOfImageLinks = new ArrayList<>();
        String response = getJSONString("https://imdb-api.com/API/Images/k_9tbfzilr/" + movieId);

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode itemsNode = rootNode.path("items");

        for (JsonNode itemNode : itemsNode) {
            String imageLink = itemNode.path("image").asText();
            listOfImageLinks.add(imageLink);
        }

        return listOfImageLinks;
    }

    public String getRandomItem (ArrayList < String > listOfItems) {
        Random random = new Random();
        int index = random.nextInt(listOfItems.size());
        System.out.println(index);
        return listOfItems.get(index);
    }

    public ArrayList<String> getSimilarMovies (String movieId) throws JsonProcessingException {
        ArrayList<String> listOfSimilarMovies = new ArrayList<>();
        String response = getJSONMovieByTitle(movieId);

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode itemsNode = rootNode.path("similars");

        for (JsonNode itemNode : itemsNode) {
            String title = itemNode.path("title").asText();
            listOfSimilarMovies.add(title);
        }

        Collections.shuffle(listOfSimilarMovies);

        ArrayList<String> threeRandomMovies = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            threeRandomMovies.add(listOfSimilarMovies.get(i));
        }

        return threeRandomMovies;
    }

    public String getMovieTitle(String movieId) throws JsonProcessingException {
        String response = getJSONMovieByTitle(movieId);

        JsonNode rootNode = objectMapper.readTree(response);

        return rootNode.path("title").asText();
    }

    public String getJSONMovieByTitle(String movieId) {
        return getJSONString("https://imdb-api.com/en/API/Title/k_9tbfzilr/" + movieId);
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
}