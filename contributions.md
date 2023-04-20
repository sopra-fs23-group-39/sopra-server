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
