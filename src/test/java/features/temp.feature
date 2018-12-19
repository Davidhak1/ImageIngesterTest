Feature: E2E Testing image ingester logic for bmw & aoa including rabbitMQ and SQL Table

  Background:
    Given Initialization

  @sn1
  Scenario: Get Locations
    Given the apis are up and running for http://cmapi.bananaappscenter.com/
    When adding api path for get request api/Location/LocationDetails
    And adding following headers
      | user     | user     |
      | password | password |
    And perform the request
    Then the response code should be 200
    And I should see json response with pairs on the filtered Msg node
      | Message    | Success Location Details |
      | StatusCode | 200                      |
      | isError    | false                    |
      | isSuccess  | true                     |


  Scenario: Check the movie details
    Given the apis are up and running for http://cmapi.bananaappscenter.com/
    When adding api path and body for post requst api/MovieBooking/MovieBooking with below details
      | Mov_ID      | 3 |
      | Location_ID | 5 |
    And perform the post request
    Then the response code should be 200
    And I should see json response with pairs on the filtered Msg node
      | Message    | Success Moviebooking Details |
      | StatusCode | 200                          |
      | isError    | false                        |
      | isSuccess  | true                         |


  Scenario: Test api test
    Given the server endpoint is https://dsd-int.bmwgroup.com/InventoryServer/cosyEndpoint
    When adding api path for get request /5UXKR2C56J0Z21267
    And adding following headers
      | user         | user             |
      | password     | password         |
      | Content-Type | application/json |
    And adding following parameters
      | angle   | 45,90,135,180,225,270,315,360 |
      | pov     | driverdoor,dashboard          |
      | bkgnd   | transparent                   |
      | imgtype | png                           |
      | height  | 960                           |
      | width   | 1280                          |
    And perform the request
    Then the response code should be 200
    And I should see json response with the array of equal than 10 items on the filtered $ node

