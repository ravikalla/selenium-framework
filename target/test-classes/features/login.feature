@SmokeTest
Feature: login and search room in hotel application
  As a user I want to login and search for the room

  @SmokeTest
  Scenario Outline: login to the application and search room
    Given User opens the browser
    Then User is able Launch the hotel application using "<URL>"
    When User enters the "<userId>" and "<password>" 
    And User clicks the Log in button 
    Then User naviaged to home page 
	And LogOut application
    Examples: 
      |URL                                   |userId 		|password |
      |http://adactin.com/HotelApp/index.php |kmanubolu     |India@123 |