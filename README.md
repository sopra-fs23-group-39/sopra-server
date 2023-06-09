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
- Database Entities: [GAME](src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java), [USER](src/main/java/ch/uzh/ifi/hase/soprafs23/entity/User.java). These two entities form the two tables in the database, containing all important information about the Game and User respectively.
- Entity Services : [GameService](src/main/java/ch/uzh/ifi/hase/soprafs23/service/GameService.java), [UserService](src/main/java/ch/uzh/ifi/hase/soprafs23/service/UserService.java). These services hold all logic affecting the above mentioned User and Game entities. They are responsible for creating and handling the Game/User entities respectively. 
- REST controller: [GameController](src/main/java/ch/uzh/ifi/hase/soprafs23/controller/GameController.java), [UserController](src/main/java/ch/uzh/ifi/hase/soprafs23/controller/UserController.java). The Rest controllers for the two most important entities are responsible for handling asynchronous Backend/Frontend communication. These endpoints can be called from the frontend to manage the Games and Users.
- Websocket config and controllers: [WebSocketConfig](src/main/java/ch/uzh/ifi/hase/soprafs23/websocket/WebSocketConfig.java), [WebSocketController](src/main/java/ch/uzh/ifi/hase/soprafs23/websocket/WebSocketController.java). The websockets handle all synchronous Backend/Frontend communication, like handling game sessions or delivering questions to the frontend.
- API service: [ApiService](src/main/java/ch/uzh/ifi/hase/soprafs23/api/ApiService.java). This service and its children are responsible for handling incoming information from the imdb-api API.

## Launch & Deployment

The project is built using gradle, all dependencies are listed in the `build.gradle` file.  
To build and run the project locally within your IDE (we recommend IntelliJ IDEA), import the project as a gradle project, then follow these steps:

```bash
# with tests:
./gradlew build
./gradlew bootrun

# to skip tests:
./gradlew build -x test
./gradlew bootrun

# to only run tests:
./gradlew test
```
Additionally, in `application.properties`, define what database you want to use. The project is designed so it can be run locally using an in-memory database like h2, or alternatively, using a MySQL database on a server that you are running either locally or remotely. Please configure to fit your needs.  
  
Deployment uses GitHub workflows to deploy the project to google cloud. See `main.yaml` and `app.yaml`. Set up your google cloud project and create a service account with the editor role, download the keys and use github's secret manager to manage them. When done correctly, the code should be automatically deployed when pushing or merging with the main branch.

## Illustration  

The user lands on the login page, where they can either log in, or register. After either of these, they switch to the main page, where they can navigate to game creation, game joining, the leaderboards, their profile, or they can log out again. When choosing to create a game, the user first has to select which game mode they would like to play, and depending on the choice, they navigate to the corresponding waiting room, or a further options page if choosing to create a custom game. They can also read the rules of each mode here. When choosing to create a custom game, they can select from various themes, like pictures from movies, trailer snippets, a mix between pictures of movies and actors, and so on. They are also able to choose how many questions the game should have, and how much time they are given to answer them. After clicking on create game, they will enter a waiting room, where they can wait for other players to join using the game's id, or start the game. After starting the game, every player in the lobby enters the game loop, where they are served question prompts, which they can answer using predetermined answer buttons. In between questions, they are shown the standings, and finally when the game ends, the winner is shown, and the users can navigate back to the main page.

## Roadmap

Future features to implement include a centrally synchronized question timer, more game modes, including different ways to answer (like a text box, instead of predetermined buttons), a friend's list and more.
  

## Authors & Acknowledgement  

### Authors  
- [Natalia Shakirova](https://github.com/orgs/sopra-fs23-group-39/people/NattiShakira)
- [Yannick Salzmann](https://github.com/orgs/sopra-fs23-group-39/people/yasalz)
- [Damjan Kuzmanovic](https://github.com/orgs/sopra-fs23-group-39/people/dkuzma1)
- [Florence Hügi](https://github.com/orgs/sopra-fs23-group-39/people/florencehuegi)
- [Markus Niemack](https://github.com/orgs/sopra-fs23-group-39/people/NieMark)  
Additionally, we would like to thank our teaching assistants [Sheena Lang](https://github.com/SheenaGit) and [Roy Rutishauser](https://github.com/royru) for helping with this project.

## License

[GNU GPLv3]([https://choosealicense.com/licenses/mit/](https://choosealicense.com/licenses/gpl-3.0/))
