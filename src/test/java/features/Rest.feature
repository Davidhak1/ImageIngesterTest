Feature: Controller Test

  Background:
    Given Initialization
    And Queries Init in mix

  @bmw_end2end
  @Rest_Assured
  @end2end_9
  Scenario: Test Sulzer Service returning 10 urls for a bmw vehicle
    Given the server endpoint is https://dsd-int.bmwgroup.com/InventoryServer/cosyEndpoint
    When get a random vehicle of bmw with complete status and 10 images mapped to it
    When adding api endpoint for get request as /vin
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
    And I should see json response with the array of equal than 10 items on the filtered imageUrls node


  @bmw_end2end
  @Rest_Assured
  @Endpoint_1
  Scenario Outline: Getting image urls with uuid
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/images/provider/
    When get a random vehicle of <oem> with complete status and <length> images mapped to it
    When adding api endpoint for get request with path based on provider <oem>
    And perform the request
    Then the response code should be 200
    And I should see json response with the array of equal than <length> items on the filtered $ node
    Examples:
      | oem | length |
      | bmw | 10     |
      | aoa | 7      |


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
    When get a random vhicle with <accountId> and complete status not removed
    When adding api path for get request <accountId>
    When adding uuid, provider, vin parameters for vehicle
    And perform the request
    Then the response code should be 200
    And I should see json response with the array of equal than 10 items on the filtered imageUrls node
    And I should see json response with keys on the filtered $ node
      | uuid          |
      | imageProvider |
      | vin           |
      | accountId     |

    Examples:
      | accountId       |
      | bmwofsanantonio |
      | bmwofdallas     |

#     TODO Add data for audi, too


  @bmw_end2end
  @Rest_Assured
  @Endpoint_5.1
  Scenario Outline: Verifying priorities of the images are in right order
    #firs step -  refreshing the vehicle
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/image/download/accountId/
    When get a random vhicle with <accountId> and complete status not removed
    When adding api path for get request <accountId>
    When adding uuid, provider, vin parameters for vehicle
    And perform the request
    Then the response code should be 200
    And I should see json response with the array of equal than 10 items on the filtered imageUrls node
    #second step - verifying the priorities
    And Query all downloaded images of the vehicle in the db
    And The priorities of the images should be correct
    Examples:
      | accountId       |
      | bmwofsanantonio |
      | bmwofdallas     |


  @bmw_end2end
  @Rest_Assured
  @Endpoint_6
  Scenario Outline: Directly Calling to the service by queues to get images [expected 10] in the response
    Given the server endpoint is http://vtqainv-imagingservice01.int.dealer.com:9615/image/queue-download/accountId/
    When get a random vhicle with <accountId> and complete status not removed
    When adding api path for get request <accountId>
    When adding uuid, provider, vin parameters for vehicle
    And perform the request
    Then the response code should be 200
    And I should see json response with keys on the filtered hey node
      | uuid          |
      | imageProvider |
      | vin           |
      | accountId     |

    Examples:
      | accountId         |
      | bmwofsanantonio   |
      | bmwofdallas       |
      | universityaudiaoa |

#     TODO Add data for audi, too

