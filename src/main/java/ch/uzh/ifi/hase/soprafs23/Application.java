package ch.uzh.ifi.hase.soprafs23;

import ch.uzh.ifi.hase.soprafs23.questions.Question;
import ch.uzh.ifi.hase.soprafs23.questions.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@SpringBootApplication
public class Application {

  public static void main(String[] args) throws JsonProcessingException {

      SpringApplication.run(Application.class, args);

      // The following code is to check if a random question is correctly chosen from the external API
      // Later, this will be deleted

      QuestionService qs = new QuestionService();

      for (int i = 0; i < 10; i++) {
          Question movieQuestion = qs.getMovieQuestion();
          System.out.println();
          System.out.println(movieQuestion.getQuestionText());
          System.out.println(movieQuestion.getQuestionLink());
          System.out.println(movieQuestion.getCorrectAnswer());
          System.out.println(movieQuestion.getWrongAnswers());
      }

//      Question movieQuestion = qs.getMovieQuestion();
//      System.out.println();
//      System.out.println(movieQuestion.getQuestionText());
//      System.out.println(movieQuestion.getQuestionLink());
//      System.out.println(movieQuestion.getCorrectAnswer());
//      System.out.println(movieQuestion.getWrongAnswers());

//      Question actorQuestion = qs.getActorQuestion();
//      System.out.println();
//      System.out.println(actorQuestion.getQuestionText());
//      System.out.println(actorQuestion.getQuestionLink());
//      System.out.println(actorQuestion.getCorrectAnswer());
//      System.out.println(actorQuestion.getWrongAnswers());

  }

  @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String helloWorld() {
    return "The application is running.";
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
      }
    };
  }
}
