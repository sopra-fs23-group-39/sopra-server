package ch.uzh.ifi.hase.soprafs23;

import ch.uzh.ifi.hase.soprafs23.questions.Question;
import ch.uzh.ifi.hase.soprafs23.questions.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.storage.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileInputStream;

@RestController
@SpringBootApplication
public class Application {

  public static void main(String[] args) throws JsonProcessingException {
      String projectId = "sopra-fs23-group-39-server";
      Storage storage = StorageOptions.getDefaultInstance().getService();
      String bucketName = "sopra-fs23-group-39-server_testdb";
      String filePath = "testdb.mv.db";
      BlobId blobId = BlobId.of(bucketName, filePath);
      Blob blob = storage.get(blobId);
      if(blob == null || blob.exists()){
          BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
          storage.create(blobInfo, new byte[0]);
      }


      /*String dbName = "testdb";
      String dbUser = "sa";
      String dbPass = "";
      Storage storage2 = StorageOptions.getDefaultInstance().getService();
      Bucket bucket = storage.get(bucketName);
      if(bucket == null){
          storage2.create(BucketInfo.of(bucketName));
      }

      String jdbcUrl = String.format("jdbc:j2:gs://%s/%s", bucketName, dbName);
      String dbUrl = String.format("gs://%s/%s", bucketName, dbName);*/


      SpringApplication.run(Application.class, args);

      // The following code is to check if a random question is correctly chosen from the external API
      // Later, this will be deleted

//      QuestionService qs = new QuestionService();
//
//      for (int i = 0; i < 10; i++) {
//          Question movieQuestion = qs.getMovieQuestion();
//          System.out.println();
//          System.out.println(movieQuestion.getQuestionText());
//          System.out.println(movieQuestion.getQuestionLink());
//          System.out.println(movieQuestion.getCorrectAnswer());
//          System.out.println(movieQuestion.getAnswer1());
//            System.out.println(movieQuestion.getAnswer2());
//            System.out.println(movieQuestion.getAnswer3());
//            System.out.println(movieQuestion.getAnswer4());
//      }

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
