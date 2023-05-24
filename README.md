# The Movie Monster

The Movie Monster is a movie trivia game offering multiple game modes to users wishing to play a movie quiz game. The modes include various prompts, like still frames from movies and tv shows, images of actors and trailer snippets. Additionally, the user can either create a custom game or choose from two preset game modes. The game is able to be played in multiplayer or alone and offers ranked leaderboards.

## Technologies used  

Written in [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html). Please make sure that the `JAVA_HOME` environment variable is set to the correct version of Java.
  
Built with:  
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Gradle](https://gradle.org/)
- REST
- [Hibernate](https://hibernate.org/)
- [STOMP](https://stomp-js.github.io/stomp-websocket/)
- [SockJS](https://github.com/sockjs/sockjs-client)

On top of this, we heavily rely on the https://imdb-api.com/ API.  


## High-level components

The most important part of the backend are:
- Database Entities: [GAME](src/main/entity/Game.java), [USER](src/main/entity/User.java). These two entities form the two tables in the database, containing all important information about the Game and User respectively.
- Entity Services : [GameService](src/main/service/GameService.java), [UserService](src/main/service/UserService.java). These services hold all logic affecting the above mentioned User and Game entities. They are responsible for creating and handling the Game/User entities respectively. 
- REST controller: [GameController](src/main/controller/GameController.java), [UserController](src/main/controller/GameController.java). The Rest controllers for the two most important entities are responsible for handling asynchronous Backend/Frontend communication. These endpoints can be called from the frontend to manage the Games and Users.
- Websocket config and controllers: [WebSocketConfig](src/main/websocket/WebSocketConfig.java), [WebSocketController](src/main/websocket/WebSocketController.java). The websockets handle all synchronous Backend/Frontend communication, like handling game sessions or delivering questions to the frontend.

## Launch & Deployment

The project is built using gradle, all dependencies are listed in the `build.gradle` file.  
To build and run the project locally within your IDE (we recommend IntelliJ IDEA), import the project as a gradle project, then follow these steps:

```bash
# with tests:
./gradlew build
./gradlew bootrun

# to skip tests:
./gradlew build -x tests
./gradlew bootrun
```
Additionally, in `application.properties`, define what database you want to use. The project is designed so it can be run locally using an in-memory database like h2, or alternatively, using a MySQL database on a server that you are running either locally or remotely. Please configure to fit your needs.  
  
Deployment uses GitHub workflows to deploy the project to google cloud. See `main.yaml` and `app.yaml`. Set up your google cloud project and create a service account with the editor role, download the keys and use github's secret manager to manage them. When done correctly, the code should be automatically deployed when pushing or merging with the main branch.

## Roadmap

Future features to implement include a centrally synchronized question timer, more game modes, including different ways to answer (like a text box, instead of predetermined buttons), a friend's list and more.

Please make sure to update tests as appropriate.  

## Authors & Acknowledgement  

### Authors  
- Natalia Shakirova
- Yannick Salzmann
- Damjan Kuzmanovic
- Florence Hügi
- Markus Niemack

## License

[MIT](https://choosealicense.com/licenses/mit/)
