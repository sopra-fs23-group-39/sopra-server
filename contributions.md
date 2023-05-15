Week 1 (27.03.2023-02.04.2023)


Shakirova Natalia:

User Story #45 (https://github.com/sopra-fs23-group-39/sopra-server/issues/45) consists of task #34. Task #34 Create Profile page (https://github.com/sopra-fs23-group-39/sopra-client/issues/34) consists of 2 tasks:

1. Display information from user database: https://github.com/sopra-fs23-group-39/sopra-client/issues/35: created a Profile component. When rendering this component, a useEffect hook is used to fetch data about a particular user (a GET request is sent to /users/{userId}). In the backend, a corresponding GET endpoint (in UserController) and a method getUserProfile (in UserService) are added.

2. Add "Change Credentials" and "Back" buttons: https://github.com/sopra-fs23-group-39/sopra-client/issues/36: created buttons, button Back redirects to /main, button ChangeCredentials - to /changes/{userId} (a corresponding function is invoked when each of the buttons is clicked). 

Commit for the client part (both tasks): 0a8131d

Commit for the server part: 547022f

Tasks #34, #35, #36 are marked as Done.
Task #45 is not marked as Done, as, for now, a user can explore only his/her profile, not others'. For this, we need to implement a Leaderbord from which a user can go to "foreign" profiles.


Hügi Florence:

As a previously registered but logged-out user, I want to be able to log in into the system with my chosen credentials (username and password) to use services and information that are available only to logged-in users: https://github.com/sopra-fs23-group-39/sopra-server/issues/4
Create log in functionality: https://github.com/sopra-fs23-group-39/sopra-server/issues/5
Create Log in page: https://github.com/sopra-fs23-group-39/sopra-client/issues/18


Niemack Markus:

Generate Game code on game creation: https://github.com/sopra-fs23-group-39/sopra-server/issues/27
Implement Game mode selection: https://github.com/sopra-fs23-group-39/sopra-server/issues/11
Add Create Game button (leads to game selection): https://github.com/sopra-fs23-group-39/sopra-client/issues/13
Add game mode menu: https://github.com/sopra-fs23-group-39/sopra-client/issues/7


Yannick Salzmann:

Creation of the main page: Added a main view, MainGuard, and MainRouter https://github.com/sopra-fs23-group-39/sopra-client/issues/39

Adding different buttons to the main page: Create Game, Join Game, Leaderboard, Profile, Logout 

Log out Button: https://github.com/sopra-fs23-group-39/sopra-client/issues/38

Routing to profile page: https://github.com/sopra-fs23-group-39/sopra-client/issues/12


Damjan Kuzmanovic:
    
-As an unregistered user, I want to be able to register as a user to use services available only to registered and logged-in users: https://github.com/sopra-fs23-group-39/sopra-server/issues/1
-Create registration functionality: https://github.com/sopra-fs23-group-39/sopra-server/issues/2
-Create Register Page: https://github.com/sopra-fs23-group-39/sopra-client/issues/17
-Route registered user to main lobby page: https://github.com/sopra-fs23-group-39/sopra-client/issues/4


    
Weeks 2-3 (03.04.2023-16.04.2023)


Niemack Markus: 

Create Game Database https://github.com/sopra-fs23-group-39/sopra-server/issues/54  

Create User Database https://github.com/sopra-fs23-group-39/sopra-server/issues/43  

Add join game functionality: https://github.com/sopra-fs23-group-39/sopra-server/issues/28  

Create Websocket Interface (read: config file and controller skeleton): https://github.com/sopra-fs23-group-39/sopra-server/issues/58  

Add dynamically updated list of players to waiting room (this is currently in Game.JS): https://github.com/sopra-fs23-group-39/sopra-server/issues/22  

Also tried to setup MySQL for a while, unsuccessfully...


Shakirova Natalia:

Task #37 Create "Change credentials" fields to profile page of user (https://github.com/sopra-fs23-group-39/sopra-client/issues/37): commit d185205

Task #49 Add Change credentials functionality (https://github.com/sopra-fs23-group-39/sopra-server/issues/49): commit 2a4a25d

Task #55 Create back-end classes to randomly get questions "Guess movie by image" from external API (https://github.com/sopra-fs23-group-39/sopra-server/issues/55): commit 6d1843c 

Task #56 Create back-end classes to randomly get questions "Guess actor by image" from external API (https://github.com/sopra-fs23-group-39/sopra-server/issues/56): commit fa1fe4e

As a result of tasks #55-56, we can create random questions using our external API. Each Question object has 4 attributes:
- questionText;
- questionLink (a link to an image of an actor/movie randomly taken from the API);
- correctAnswer (movie title/actor name);
- wrongAnswers (list of 3 answers; in case of a movie question, 3 titles of similar movies are shown, in case of an actor question, 3 names of other actors/actresses (depending on the gender of an actor in question) are displayed. 

When getting a question, calls are sent directly to the API and their results are then processed. To prevent exhaustion of API calls limit while developing (when running an application multiple times to test functionality), an additional class ApiServiceSubstitute was created, it mocks our real API (a list of all movies (their ids) and a list of all actors (their ids) are stores in variables, instead of accessing them each time directly from the API).


Yannick Salzmann:

creating the log out functionality: https://github.com/sopra-fs23-group-39/sopra-server/issues/52

routing to the global leaderboard: https://github.com/sopra-fs23-group-39/sopra-client/issues/1

routing to the join game page: https://github.com/sopra-fs23-group-39/sopra-client/issues/2

routing to the new game page: https://github.com/sopra-fs23-group-39/sopra-client/issues/3

completion of the user story respective to the main page: https://github.com/sopra-fs23-group-39/sopra-server/issues/6  

adding a working exit button in the waiting room: https://github.com/sopra-fs23-group-39/sopra-client/issues/10

added the "isReady" property for players: https://github.com/sopra-fs23-group-39/sopra-server/issues/33

every player in the waiting room can press the ready button, such that the property is successfully changed: https://github.com/sopra-fs23-group-39/sopra-server/issues/32


Florence Hügi:

Create join lobby page: https://github.com/sopra-fs23-group-39/sopra-client/issues/14

Add "Enter lobby code field" and properly route it: https://github.com/sopra-fs23-group-39/sopra-client/issues/15

Add "Join" and "Back" Buttons: https://github.com/sopra-fs23-group-39/sopra-client/issues/16


Damjan Kuzmanovic:

Added timer to GameSelection page: https://github.com/sopra-fs23-group-39/sopra-client/issues/5

Added timer selection to functionality: https://github.com/sopra-fs23-group-39/sopra-server/issues/15

Fixed the registration page



Week 4 (17.04.2023-23.04.2023)


Shakirova Natalia:

Tasks #20 Create GameQuestion Page (https://github.com/sopra-fs23-group-39/sopra-client/issues/20), #21 Add snippet display (https://github.com/sopra-fs23-group-39/sopra-client/issues/21), #22 Add Answer Buttons & Question (https://github.com/sopra-fs23-group-39/sopra-client/issues/22), #24 After Timer runs out route to game result page (https://github.com/sopra-fs23-group-39/sopra-client/issues/24): commits:  95a90c6 (client), 3620d95 (server)

Result: Question page displays a quiz question with answers (1 correct and 3 wrong). After a user clicks on one of the answers (he can click only once), a corresponding JSON object is sent to the back-end for evaluation. After a fixed amount of time, a user is routed to Standings page (intermediate results).

Task https://github.com/sopra-fs23-group-39/sopra-server/issues/60 Add functionality to connect a concrete game and questions taken from the API, commit: 2c4a8b6 (server)

Result: when a Game object is created, a list of questions is added to it, according to the chosen game mode and number of questions (equality of questions is prevented by overridden equals method). Number of questions sent to the front-end is controlled by the back-end that counts game rounds.  

Task #45 Create a Question route protection (QuestionGuard), commit: 3eb54cc (client)


Yannick Salzmann


https://github.com/sopra-fs23-group-39/sopra-client/issues/41
storing game mode property when clicking on a game setting 

only open the lobby when clicking on create game (only one lobby that is either accessed through creating a game or join game)

created a method in the back end to retrieve the game settings

display the game settings properly in the waiting room

creating the leaderboard page https://github.com/sopra-fs23-group-39/sopra-client/issues/30

having a back button and route it accordingly https://github.com/sopra-fs23-group-39/sopra-client/issues/32

display a list of all registered players https://github.com/sopra-fs23-group-39/sopra-client/issues/31

order them on their rank

route to a user profile when clicking on its name https://github.com/sopra-fs23-group-39/sopra-client/issues/33

disable the change credentials button in the profile page, when its not the own profile

working on the start game functionality (so far no simultaneous start for all players)  
  
  
  
Markus Niemack  
  
https://github.com/sopra-fs23-group-39/sopra-server/issues/62  

Migrated entire server side of project to google app engine flexible, which caused the database to break completely, therefore I pulled this forward:  
  
https://github.com/sopra-fs23-group-39/sopra-server/issues/57  
  
migrated entire database to MySQL, set up a schema, deployed to google SQL, set up connection with credentials etc. Had to rewrite some entity code due to hard to find errors.
  

Florence Hügi:

Create Game results page: https://github.com/sopra-fs23-group-39/sopra-client/issues/25

Display the correct answer: https://github.com/sopra-fs23-group-39/sopra-client/issues/26

Display current player standings: https://github.com/sopra-fs23-group-39/sopra-client/issues/27

check if the last question of the game and route accordingly: https://github.com/sopra-fs23-group-39/sopra-client/issues/28

As a logged-in user participating in a game and having answered a question, I want to see the right answer to the question and the scores of all the users playing this game (including my score): https://github.com/sopra-fs23-group-39/sopra-server/issues/40



Week 5 (24.04.2023-30.04.2023)

Shakirova Natalia:

Task #46 (client) Make a button "Select mode" on GameSelection page "chosen" when pressed, disable other buttons (https://github.com/sopra-fs23-group-39/sopra-client/issues/46):
- commit: https://github.com/sopra-fs23-group-39/sopra-client/commit/8f2e22b8062309617188a3a5fae7040b5d428f89;
- commit (merged to main, together with task #53): https://github.com/sopra-fs23-group-39/sopra-client/commit/8a0d80b6b4baeb8a5c1759390157425db06d76a4

Task #53 (client) Fix bugs in Standings.js (https://github.com/sopra-fs23-group-39/sopra-client/issues/53):
- commit: https://github.com/sopra-fs23-group-39/sopra-client/commit/6b170ee2e72574c45f467b3f521bfad5cac1e35a;
- commit (merged to main, together with task #46): https://github.com/sopra-fs23-group-39/sopra-client/commit/8a0d80b6b4baeb8a5c1759390157425db06d76a4

Task #70 (server) Fix bugs in the server part connected to displaying current standings (https://github.com/sopra-fs23-group-39/sopra-server/issues/70):
- commit: https://github.com/sopra-fs23-group-39/sopra-server/commit/23c64b7ee4cc83cf9c321ed6d5640a298f532739;
- commit (merged to main): https://github.com/sopra-fs23-group-39/sopra-server/commit/5c5bef0ad863081c5a65cf5061c3fe1973bfee52

Task #48 (client): Make the correct answer turn "green" on Question page: https://github.com/sopra-fs23-group-39/sopra-client/issues/48,
 commit: https://github.com/sopra-fs23-group-39/sopra-client/commit/c2de55620d0de3eca8c5d9493627a08ff32ab174

Task #79 Add functionality to compute global score for all games, to update ranks accordingly and to display it correctly on Profile and Leaderboard pages (https://github.com/sopra-fs23-group-39/sopra-server/issues/79):

- commit (server): https://github.com/sopra-fs23-group-39/sopra-server/commit/e7a3436f85dbe4a8024b627dabf26ec4c6167d05; 

- commit (client): https://github.com/sopra-fs23-group-39/sopra-client/commit/ae271276172e54ed88ab44d0e0dca2e54bb2a86e, https://github.com/sopra-fs23-group-39/sopra-client/commit/25e2201537352e743e8dae843da53fd0c3df5770, https://github.com/sopra-fs23-group-39/sopra-client/commit/290dfacfc9dbdedc41b6a4598189ab7fde77ddf7 
  
Without a task: added some basic CSS to all the pages so that they look more or less fine for the presentation


Markus Niemack:  
  
https://github.com/sopra-fs23-group-39/sopra-server/issues/66  

Rewrite Websocket with different library, both in back- and frontend since they are currently timing out.

https://github.com/sopra-fs23-group-39/sopra-server/issues/35  

Synchronized start, host now clicks start game and everyone else is pulled with. includes https://github.com/sopra-fs23-group-39/sopra-client/issues/11

https://github.com/sopra-fs23-group-39/sopra-server/issues/68  

Set up timer slider to be connected to how long users have to answer a question

https://github.com/sopra-fs23-group-39/sopra-server/issues/82  

added to game settings


Yannick Salzmann:

Login/Registration issues:

Correctly handle exceptions for invalid user input (f. e. invalid username != null) https://github.com/sopra-fs23-group-39/sopra-server/issues/67

Currently, a user is locked on the Registration page if a user wants to login https://github.com/sopra-fs23-group-39/sopra-client/issues/49

CSS for the Game: Assuring that the buttons for answering a question are not invisible (pushed to the bottom) in the quiz. https://github.com/sopra-fs23-group-39/sopra-client/issues/47 


Florence Hügi:

display winner if the game is over https://github.com/sopra-fs23-group-39/sopra-client/issues/29

create server functions corresponding to winnerpage https://github.com/sopra-fs23-group-39/sopra-server/issues/69



Damjan Kuzmanovic:

-Prvent the Start game button being pressed multiple times https://github.com/sopra-fs23-group-39/sopra-client/issues/51

-Ensure that the application always starts from the login page https://github.com/sopra-fs23-group-39/sopra-client/issues/52

-Add Game Counter functionality https://github.com/sopra-fs23-group-39/sopra-server/issues/74



Week 6 (01.05.2023-07.05.2023)

Shakirova Natalia:

User story #95: As a player I want to play a game where the prompt is from a tv show (https://github.com/sopra-fs23-group-39/sopra-server/issues/95), including:
- task #98 (server): Expand API calls to include tv show (https://github.com/sopra-fs23-group-39/sopra-server/issues/98);
- task #97 (server): Implement tv shows gamemode (https://github.com/sopra-fs23-group-39/sopra-server/issues/97);
- task #67 (client): Add tv show gamemode selection to gameSelection (https://github.com/sopra-fs23-group-39/sopra-client/issues/67).

Commit (server part): https://github.com/sopra-fs23-group-39/sopra-server/commit/d04c0eceb5f2528eaded0cd1d4ce4b2b37847b70

Commit (client part): https://github.com/sopra-fs23-group-39/sopra-client/commit/98bd0b77ccc8187f1ff9afb91c36bae6ef639f88

Without a task: optimized api calls for "Actor" questions. 


Yannick Salzmann

I will add a new game format for blitz games. https://github.com/sopra-fs23-group-39/sopra-server/issues/92

This consists of:

1. setting up the back end: creating new game format specific global variables to each player. Adding new methods to determine how many points player receive by playing the game format. There is a seperate ranking for this game format.  https://github.com/sopra-fs23-group-39/sopra-server/issues/94

2. Display the new content/settings in the corresponding screens in the front end (profile, game_selection, game)


Damjan Kuzmanovic

- Implement Rapid game format
    - Create a standardized Rapid game format https://github.com/sopra-fs23-group-39/sopra-server/issues/100
    - Create a separate evaluation function for rapid format https://github.com/sopra-fs23-group-39/sopra-server/issues/102
    - Create a global rank and global leaderboard for rapid format https://github.com/sopra-fs23-group-39/sopra-server/issues/101
- Fix minor bugs


Florence Hügi:

Add scorllbar to the leaderboard page to better see all the players https://github.com/sopra-fs23-group-39/sopra-client/issues/69
Add custom styling to Login page https://github.com/sopra-fs23-group-39/sopra-client/issues/71
Add custom styling to Register page https://github.com/sopra-fs23-group-39/sopra-client/issues/72  
  
Markus Niemack:  
Synchronize loading question page between participating players: https://github.com/sopra-fs23-group-39/sopra-server/issues/103  
Adjust scoring to use dynamic time (more precise description in issue link)  https://github.com/sopra-fs23-group-39/sopra-server/issues/104


Week 7 (08.05.2023-14.05.2023)

Shakirova Natalia:

Task #76 Add custom styling to WaitingRoom page (https://github.com/sopra-fs23-group-39/sopra-client/issues/76)

Task #75 Add custom styling to JoinGame page (https://github.com/sopra-fs23-group-39/sopra-client/issues/75)

Commit for both tasks #75 and #76 (client): https://github.com/sopra-fs23-group-39/sopra-client/commit/2d7fa42e9e74b57cf446ab6344f392249e9008f6

Task #106 Create tests for back-end (part 1, up to 50% coverage) (https://github.com/sopra-fs23-group-39/sopra-server/issues/106): test coverage increased from 11% to 50%



Yannick Salzmann

- Finishing points distribution for blitz games https://github.com/sopra-fs23-group-39/sopra-server/issues/93
- Fixing profile page, where currently an error is thrown when clicking on player stats, as well as improve the design https://github.com/sopra-fs23-group-39/sopra-client/issues/81
- Improving the change profile page https://github.com/sopra-fs23-group-39/sopra-client/issues/82  
  
Markus Niemack:  
  
Implement trailer gamemode: https://github.com/sopra-fs23-group-39/sopra-server/issues/87
Extend (external) API calls accordingly: https://github.com/sopra-fs23-group-39/sopra-server/issues/89  
+ rework how questions are fetched so that they are not persisted in the database. (not listed) 


Florence Hügi:

Add custom styling to Main page https://github.com/sopra-fs23-group-39/sopra-client/issues/73

Add custom styling to GameSelection page https://github.com/sopra-fs23-group-39/sopra-client/issues/74

Damjan Kuzmanovic:

Add custom styling to Questions page https://github.com/sopra-fs23-group-39/sopra-client/issues/77

Recheck routing and guards https://github.com/sopra-fs23-group-39/sopra-client/issues/68

Add IsStarted Flag to not allow the users to join the game that has started https://github.com/sopra-fs23-group-39/sopra-server/issues/105

Week 8 (15.05.2023-21.05.2023)

Yannick Salzmann

- Redesign the Winner page https://github.com/sopra-fs23-group-39/sopra-client/issues/79
- Redesign and extend the leaderboard for multiple game formats https://github.com/sopra-fs23-group-39/sopra-client/issues/80

Natalia Shakirova

Task #107 Create tests for back-end (part 2, up to 75% coverage) (https://github.com/sopra-fs23-group-39/sopra-server/issues/107)

Without a task:
- add uniqueness constraint for questions (prevent having 2 same answers in a Question object);
- testing application from a user point of view, finding bugs, fixing them;
- Sonar: clean up

