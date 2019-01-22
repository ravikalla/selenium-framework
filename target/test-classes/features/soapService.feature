@SmokeTest
Feature: send xml request and get response

  @SmokeTest
  Scenario Outline: send xml request for scenario 1 and get response
    Given Create xml request using xml template "FharenheitToCensius_Request" and connect data sheet "Fahrenheit" with testcase ID "TC01" 
    When I submit the xml request
    Then Connect Execel "Fahrenheit_Assertions"
    Then Validating tag "FahrenheitToCelsiusResponse" of "FahrenheitToCelsiusResult" as "51.1111111111111" from XML Response
    Examples: 
      |URL                                   |userId 		|password |
      |http://adactin.com/HotelApp/index.php |kmanubolu     |India@123 |