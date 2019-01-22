@SmokeTest
Feature: send json request and get response

  @SmokeTest
  Scenario Outline: send json request for scenario 1 and get response
    Given Create JSON request using JSON template "Customer_Request" and connect data sheet "Customer" with testcase ID "TC01" 
    When I submit the JSON request
    Then Connect Execel "Customer_Assertions"
    Then Validating "Krishna Reddy" from "firstName" JSON response
    Then Validating "Manubolu" from "lastName" JSON response
    Then Validating "35" from "age" JSON response
    Then Validating "Nellore" from "city" JSON response
    Examples: 
      |URL                                   |userId 		|password |
      |http://adactin.com/HotelApp/index.php |kmanubolu     |India@123 |