//package ch.uzh.ifi.hase.soprafs23.api;
//
//import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
//import ch.uzh.ifi.hase.soprafs23.service.GameService;
//import ch.uzh.ifi.hase.soprafs23.service.UserService;
//import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//class MovieApiServiceTest {
//    @MockBean
//    private GameService gameService;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private DTOMapper dtoMapper;

//
//    @Test
//    void testGetSimilarItems() throws JsonProcessingException {
//        String jsonObjectAsString = "{\"similars\":[{\"title\":\"Movie A\"},{\"title\":\"Movie B\"},{\"title\":\"Movie C\"},{\"title\":\"Movie D\"},{\"title\":\"Movie E\"}]}";
//        MovieApiService myClass = Mockito.spy(new MovieApiService());
//
//        List<String> mockSimilarMovies = new ArrayList<>();
//        mockSimilarMovies.add("Movie A");
//        mockSimilarMovies.add("Movie B");
//        mockSimilarMovies.add("Movie C");
//        mockSimilarMovies.add("Movie D");
//        mockSimilarMovies.add("Movie E");
//
//        Mockito.doReturn(mockSimilarMovies).when(myClass).getSimilarItemsArray(jsonObjectAsString);
//
//        List<String> expectedMovies = myClass.getSimilarItems(jsonObjectAsString);
//
//        assertEquals(expectedMovies.size(), 3);
//        assertTrue(mockSimilarMovies.containsAll(expectedMovies));
//    }
//
//
//    @Test
//    void testGetItemName() throws JsonProcessingException {
//        String jsonObjectAsString = "{\"title\":\"The Lord of the Rings\"}";
//        MovieApiService myClass = new MovieApiService();
//        ObjectMapper mockedObjectMapper = Mockito.mock(ObjectMapper.class);
//        myClass.setObjectMapper(mockedObjectMapper);
//
//        JsonNode mockedJsonNode = Mockito.mock(JsonNode.class);
//        Mockito.when(mockedObjectMapper.readTree(jsonObjectAsString)).thenReturn(mockedJsonNode);
//        Mockito.when(mockedJsonNode.path("title")).thenReturn(mockedJsonNode);
//        Mockito.when(mockedJsonNode.asText()).thenReturn("The Lord of the Rings");
//
//        String expectedName = myClass.getItemName(jsonObjectAsString);
//
//        assertEquals(expectedName, "The Lord of the Rings");
//    }

//}