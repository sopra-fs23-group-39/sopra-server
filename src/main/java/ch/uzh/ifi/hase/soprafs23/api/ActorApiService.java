package ch.uzh.ifi.hase.soprafs23.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActorApiService extends ApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ACTOR = "Actor";
    private static final String ACTRESS = "Actress";

    // This list can contain actors without pictures and not really actors
    public List<String> getListOfActorIds(String key, List<String> listOfMovieIds) throws JsonProcessingException {
        List<String> listOfActorIds = new ArrayList<>();
        for (String movieId : listOfMovieIds) {
            String response = getJSONString(String.format("https://imdb-api.com/API/Title/%s/%s", key, movieId));

            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemsNode = rootNode.path("starList");

            for (JsonNode itemNode : itemsNode) {
                String actorId = itemNode.path("id").asText();

                // To prevent duplicates of actorIds in a list
                if (!listOfActorIds.contains(actorId)) {
                    listOfActorIds.add(actorId);
                }
            }
        }

        return listOfActorIds;
    }

    public List<String> getListOfValidActorIds(List<String> listOfActors, List<String> listOfActresses) {
        List<String> listOfValidActorIds = new ArrayList<>(listOfActors);
        listOfValidActorIds.addAll(listOfActresses);
        return listOfValidActorIds;
    }

    public List<List<String>> getSortedListOfAllActors(String key, List<String> listOfActorIds) throws JsonProcessingException {
        List<List<String>> listActorsSorted = new ArrayList<>();
        List<String> listOfActors = new ArrayList<>();
        List<String> listOfActresses = new ArrayList<>();
        List<String> listOfOthers = new ArrayList<>();
        List<String> listOfNoImage = new ArrayList<>();

        for (String actorId : listOfActorIds) {
            String gender = getItemGender(actorId, key);

            if (gender.equals(ACTOR)) {
                listOfActors.add(actorId);
            } else if (gender.equals(ACTRESS)) {
                listOfActresses.add(actorId);
            } else {
                listOfOthers.add(actorId);
            }

            // To get a list of actor ids without pictures
            String response = getJSONItemById(actorId, key);
            JsonNode rootNode = objectMapper.readTree(response);
            String image = rootNode.path("image").asText();
            if (image.contains("nopicture.jpg")) {
                listOfNoImage.add(actorId);
            }
        }

        listActorsSorted.add(listOfActors);
        listActorsSorted.add(listOfActresses);
        listActorsSorted.add(listOfOthers);
        listActorsSorted.add(listOfNoImage);

        return listActorsSorted;
    }

    public List<String> getListOfActors(List<List<String>> listActorsSorted) {
        List<String> list = new ArrayList<>();
        for (String id : listActorsSorted.get(0)) {
            if (!listActorsSorted.get(3).contains(id)) {
                list.add(id);
            }
        }
        return list;
    }

    public List<String> getListOfActresses(List<List<String>> listActorsSorted) {
        List<String> list = new ArrayList<>();
        for (String id : listActorsSorted.get(1)) {
            if (!listActorsSorted.get(3).contains(id)) {
                list.add(id);
            }
        }
        return list;
    }

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

    public List<String> getSimilarItems(String itemId, String key, List<String> listOfActors, List<String> listOfActresses) throws JsonProcessingException {
        List<String> listToChooseFrom = new ArrayList<>();
        List<String> listSimilarItemsIds = new ArrayList<>();
        List<String> listSimilarItemsNames = new ArrayList<>();

        String itemGender = getItemGender(itemId, key);

        if (itemGender.equals(ACTOR)) {
            listToChooseFrom = listOfActors;
        } else if (itemGender.equals(ACTRESS)) {
            listToChooseFrom = listOfActresses;
        }

        Collections.shuffle(listToChooseFrom);

        for (int i = 0; i < 3; i++) {
            listSimilarItemsIds.add(listToChooseFrom.get(i));
        }

        for (String id : listSimilarItemsIds) {
            String itemName = getItemName(id, key);
            listSimilarItemsNames.add(itemName);

        }
        return listSimilarItemsNames;
    }

    public String getItemGender(String itemId, String key) throws JsonProcessingException {
        String response = getJSONItemById(itemId, key);
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode itemsNode = rootNode.path("castMovies");

        String gender = null;
        JsonNode itemNode = itemsNode.get(0);

        String role = itemNode.path("role").asText();
        if (role.contains(ACTOR)) {
            gender = ACTOR;
        }
        else if (role.contains(ACTRESS)) {
            gender = ACTRESS;
        }
        else {
            String roleNode = rootNode.path("role").asText();
            if (roleNode != null && roleNode.contains(ACTOR)) {
                gender = ACTOR;
            }
            else if (roleNode != null && roleNode.contains(ACTRESS)) {
                gender = ACTRESS;
            }
            else {
                gender = "Other";
            }
        }

        return gender;
    }
}