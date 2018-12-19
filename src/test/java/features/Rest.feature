Feature: Controller Test

  Background:
    Given Initialization
    And Queries Init in mix

  @bmw_end2end
  @Rest_Assured
  @end2end_9
  Scenario: Test Sulzer Service returning 10 urls for a bmw vehicle
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
#    And I should see json response with the array of 10 items on the filtered imageUrls node


  @bmw_end2end
  @Rest_Assured
  @Endpoint_1
  Scenario Outline: Getting image urls with uuid
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/images/provider/
    When adding api path for get request <path>
    And perform the request
    Then the response code should be 200
    And I should see json response with the array of equal than <length> items on the filtered $ node
    Examples:
      | path                                                   | length |
      | AOA_STOCK_IMAGES/uuid/782f56cb0a0e0ae830512b4b435b0958 | 0      |
      | BMW_STOCK_IMAGES/uuid/03990f9f0a0d04fe4d6e7a0b383beeb1 | 10     |


  @bmw_end2end
  @Rest_Assured
  @Endpoint_2.1
  Scenario Outline: Getting list of vehicles with account and oem /images/provider/{provider}/uuid/{uuid}
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/management/listOfVehicles/accountId/
    When adding api path for get request <path>
    And perform the request
    Then the response code should be 200
    And I should see json response with the array of <comparison> than <length> items on the filtered $ node
    Examples:
      | path                      | comparison | length |
      | audiburlingtonaoa/oem/bmw | equal      | 0      |
      | bertsmithbmw/oem/aoa      | equal      | 0      |


  @bmw_end2end
  @Rest_Assured
  @Endpoint_2.2
  Scenario Outline: Getting list of vehicles with account and oem /images/provider/{provider}/uuid/{uuid}
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/management/listOfVehicles/accountId/
    When adding api path for get request <path>
    And perform the request
    Then the response code should be 200
    Then vehicle table should have equal number of vehicles for account in the <path> as the server returns the filtered $ node
    Examples:
      | path                        |
      | audiburlingtonaoa/oem/aoa   |
      | bertsmithbmw/oem/bmw        |
      | commonwealthaudiaoa/oem/aoa |
      | bmwofdallas/oem/bmw         |
      | audisandiegoaoa/oem/aoa     |
      | bmwoffremont/oem/bmw        |


  @bmw_end2end
  @Rest_Assured
  @Endpoint_3
  Scenario Outline: Getting list of accounts for a particular oem
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/management/
    When adding api path for get request accounts/oem/
    And adding api path for get request <oem>
    And perform the request
    Then the response code should be 200
    Then vehicle table should have equal number of accounts in the db as in the response at the filtered $ node for <oem>
    Examples:
      | oem |
      | bmw |
      | aoa |


  @bmw_end2end
  @Rest_Assured
  @Endpoint_4
  Scenario: Removing items from vehicle table
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/management/removeVehicle/
    When fetching 5 random not removed vehicles to a list
    And adding to the body the array of the uuids
    And perform the post request sending an array
    Then the response code should be 200
    And all the vehicles in the list should have removed set to true
    And set removed to false for all the vehicles and validate it


  @bmw_end2end
  @Rest_Assured
  @Endpoint_5
  Scenario Outline: Directly Calling Sulzer positive expecting 10 images in the response
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/image/download/accountId/
    When adding api path for get request <accountId>
    When adding following parameters
      | uuid     | <uuid>     |
      | provider | <provider> |
      | vin      | <vin>      |
    And perform the request
    Then the response code should be 200
    And I should see json response with the array of equal than 10 items on the filtered imageUrls node
    And I should see json response with pairs on the filtered $ node
      | uuid          | <uuid>      |
      | imageProvider | <provider>  |
      | vin           | <vin>       |
      | accountId     | <accountId> |
    Examples:
      | accountId       | uuid                             | provider         | vin               |
      | bmwofsanantonio | 0103a9e20a0d0c1469a45a769f3f9b53 | BMW_STOCK_IMAGES | 5UXKR2C56J0Z21267 |
      | bmwofdallas     | 039910790a0d04fe4d6e7a0b0526cc36 | BMW_STOCK_IMAGES | 5UXTR7C54KLF33720 |

#     TODO Add data for audi, too


  @bmw_end2end
  @Rest_Assured
  @Endpoint_5.1
  Scenario Outline: Verifying priorities of the images are in right order
    #firs step -  refreshing the vehicle
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/image/download/accountId/
    When adding api path for get request <accountId>
    When adding following parameters
      | uuid     | <uuid>     |
      | provider | <provider> |
      | vin      | <vin>      |
    And perform the request
    Then the response code should be 200
    And I should see json response with the array of equal than 10 items on the filtered imageUrls node
    #second step - verifying the priorities
    And Query all downloaded images of the vehicle in the db with <uuid>
    And The priorities of the images should be correct
    Examples:
      | accountId       | uuid                             | provider         | vin               |
      | bmwofsanantonio | 0103a9e20a0d0c1469a45a769f3f9b53 | BMW_STOCK_IMAGES | 5UXKR2C56J0Z21267 |
      | bmwofdallas     | 039910790a0d04fe4d6e7a0b0526cc36 | BMW_STOCK_IMAGES | 5UXTR7C54KLF33720 |


  @bmw_end2end
  @Rest_Assured
  @Endpoint_6
  Scenario Outline: Directly Calling to the service to get images [expected 10] in the response
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/image/queue-download/accountId/
    When adding api path for get request <accountId>
    When adding following parameters
      | uuid     | <uuid>     |
      | provider | <provider> |
      | vin      | <vin>      |
    And perform the request
    Then the response code should be 200
    And I should see json response with pairs on the filtered $ node
      | uuid          | <uuid>      |
      | imageProvider | <provider>  |
      | vin           | <vin>       |
      | accountId     | <accountId> |
    Examples:
      | accountId       | uuid                             | provider         | vin               |
      | bmwofsanantonio | 0103a9e20a0d0c1469a45a769f3f9b53 | BMW_STOCK_IMAGES | 5UXKR2C56J0Z21267 |
      | bmwofdallas     | 039910790a0d04fe4d6e7a0b0526cc36 | BMW_STOCK_IMAGES | 5UXTR7C54KLF33720 |

#     TODO Add data for audi, too

