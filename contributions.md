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

    
