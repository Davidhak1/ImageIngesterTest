Feature: E2E Testing image ingester logic for bmw & aoa including rabbitMQ and SQL Table

  Background:
    Given Initialize the browser with chrome
    And Clean vehicle and vehicle_image tables from data created during prior tests
    And Navigate to "http://vtqase-platrabbitmq01.dyn.dealer.ddc:15672/#/channels"
    When User enters username and password and logs in
    And set the removed flag to true for vehicles having uuid length greater than 35
    And generate a new uuid
    And Go to queues
    And Choose image-action-queue
    And set the generic headers and properties for the publish


  @bmw_end2end
  @end2end_1
  Scenario Outline: E2E Validating the status and the next_scheduled_time for vehicles that have the same VIN already existing in the db with status initial for bmw

    And Get a random <oem> vehicle with <status> and Not removed
    And fill in the payload with the <oem> vin and new uuid
    And publish the message and wait for 2 seconds
    Then The new item should exist in the vehicle table
    And Query myVehicle until the status is None, Partial or Complete
    And The next_scheduled_date of the new item should be between 6-8 days or equal to last_modified_on timestamp
    And close the browser
    And set the removed flag to true for myVehicle

    Examples:
      | oem | status  |
      | bmw | initial |
      | bmw | initial |
#      |bmw    |initial|
#      |bmw    |initial|
#      |bmw    |initial|
#      |bmw    |initial|
#      |bmw    |initial|
#      |bmw    |initial|


#----------------------------------------------------------------------------------------------------------------------------
  @bmw_end2end
  @end2end_2
  Scenario Outline: E2E Validating the status and the next_scheduled_time for vehicles that have the same VIN already existing in the db with status initial for audi

    And Get the vehicle with <vin> vin Not Removed
    And Extend the vehicle's original uuid
    And fill in the payload with the <oem> vin and new uuid with force refresh
    And publish the message and wait for 2 seconds
    Then The new item should exist in the vehicle table
    And Query myVehicle until the date_time is between 6-8 days or equal to last_modified_on timestamp
    And The next_scheduled_date of the new item should be between 6-8 days or equal to last_modified_on timestamp
    And close the browser
    And set the removed flag to true for myVehicle

    Examples:
      | oem | vin               |
      | aoa | WAU8DAF89KN003220 |
      | aoa | WA1FVAF17KD014453 |

    #WBA4W9C51KAF94395 with complete solution in the Server but partial in the db
    #WBXYJ3C33JEJ82355 With partial solution in the Server but initial in the db
#----------------------------------------------------------------------------------------------------------------------------


  @bmw_end2end
  @end2end_3
  Scenario Outline: E2E Validating the status and the timestamp for vehicles that have the same VIN already existing in the db with status none and partial

    And Get the vehicle with <vin> vin Not Removed
    And fill in the payload with the <oem> vin and new uuid
    And publish the message and wait for 2 seconds
    Then The new item should exist in the vehicle table and the status should be <status>
    And The next_scheduled_date of the new item should be between 6-8 days from now
    And change the next-shchedule_date to the current date
    And Query myVehicle until the date_time is between 6-8 days from now
    And The next_scheduled_date of the new item should be between 6-8 days from now
    And close the browser
    And set the removed flag to true for myVehicle


    Examples:
      | oem | status  | vin               |
      | bmw | none    | WBXHT3C30J5K24880 |
      | bmw | partial | WBXHT3C36J5L27981 |
      | aoa | none    | WA1BCCFS4JR027669 |
      | aoa | partial | WAU44AFD2JN000766 |


  # WBXHT3C30J5K24880 - This vehicle has None status in the image_ingester table, and no urls in Sulzer
  # WBXHT3C36J5L27981 - This vehicle has Partial status in the image_ingester table, and no urls in Sulzer
  # WA1BCCFS4JR027669 - This vehicle has None status in the image_ingester table, and no urls in AOA server
  # WAU44AFD2JN000766 - This vehicle has Partial status in the image_ingester table, and no urls in AOA server

  #----------------------------------------------------------------------------------------------------------------------------

  @bmw_end2end
  @end2end_4
  Scenario Outline: E2E Test validating the queue and the timestamp for vehicles that already exist in the db with status complete Not Force

    And Get a random <oem> vehicle with <status> and Not removed
    And fill in the payload with the <oem> vin and new uuid
    And publish the message and wait for 2 seconds
    Then The new item should exist in the vehicle table and the status should be <status>
    And The next_scheduled_date of the new item should be the same as last_updated date and less than 5 mins
    And close the browser
    And set the removed flag to true for myVehicle



    Examples:
      | oem | status   |
      | bmw | complete |
      | aoa | complete |
#      | bmw | complete |
#      | aoa | complete |
#      | bmw | complete |
#      | aoa | complete |
#      | bmw | complete |
#      | aoa | complete |

#----------------------------------------------------------------------------------------------------------------------------

  @bmw_end2end
  @end2end_5
  Scenario Outline: E2E Test validating the queue and the timestamp for vehicles that already exist in the db with all statuses and Force Refreshed

    And Get a random <oem> vehicle with <status> and Not removed
    And fill in the payload with the <oem> vin and new uuid with force refresh
    And publish the message and wait for 2 seconds
    Then The new item should exist in the vehicle table
    And Query myVehicle until the date_time is between 6-8 days or equal to last_modified_on timestamp
    And The next_scheduled_date of the new item should be between 6-8 days or equal to last_modified_on timestamp
    And close the browser
    And set the removed flag to true for myVehicle



    Examples:
      | oem | status   |
      | bmw | none     |
      | bmw | partial  |
      | bmw | complete |
      | aoa | none     |
      | aoa | partial  |
      | aoa | complete |
#----------------------------------------------------------------------------------------------------------------------------

  @bmw_end2end
  @end2end_6
  Scenario Outline: E2E Validating if all the images are mapped to new items that have VINs already existing in the db with completed or partial solutions

    And Get a random <oem> vehicle with <status> and Not removed
    And fill in the payload with the <oem> vin and new uuid
    And publish the message and wait for 2 seconds
    Then The new item should exist in the vehicle table and the status should be <status>
    And Vehicles with old and new uuids and should have have equal number of images mapped
    And close the browser
    And set the removed flag to true for myVehicle



    Examples:
      | oem | status   |
      | bmw | partial  |
      | bmw | complete |
#      | aoa | partial  |
#      | aoa | complete |
#      | bmw | partial  |
#      | bmw | complete |
#      | aoa | partial  |
#      | aoa | complete |
#
#      | bmw | partial  |
#      | bmw | complete |
#      | aoa | partial  |
#      | aoa | complete |
#----------------------------------------------------------------------------------------------------------------------------

  @bmw_end2end
  @end2end_7
  Scenario Outline: E2E (BMW only) validating that with force refresh it calls directly to Sulzer & Cozy and doesn't compare the number of images in the system

    And Get the vehicle with <vin> vin Not Removed
    And fill in the payload with the <oem> vin and new uuid with force refresh
    And publish the message and wait for 2 seconds
    And The new item should exist in the vehicle table and the status should be initial
    Then The status of the old vehicle should be <status>
    And The next_scheduled_date of the new item should be less than 1 hour
    And Query myVehicle until the status is None, Partial or Complete
    And The next_scheduled_date of the new item should be between 6-8 days or equal to last_modified_on timestamp
    And The new item should exist in the vehicle table and the status should be <expectedStatus>
    And close the browser
    And set the removed flag to true for myVehicle



    Examples:
      | oem | status   | vin               | expectedStatus |
      | bmw | partial  | WBXHT3C36J5L27981 | None           |
      | bmw | complete | 5UXTR9C57JLD68868 | None           |
#      | bmw | partial  | WBXHT3C36J5L27981 | None           |
#      | bmw | complete | 5UXTR9C57JLD68868 | None           |

  # WBXHT3C36J5L27981 - This vehicle has Partial status in the image_ingester table, but no image urls in sulzer,
  # 5UXTR9C57JLD68868 - This vehicle has Complete status in the image_ingester table, but no image urls in sulzer,

#----------------------------------------------------------------------------------------------------------------------------
  @bmw_end2end
  @end2end_8
  Scenario Outline: E2E (AOA only) validating that with force refresh it calls directly to AOA Servers and doesn't compare the number of images in the system

    And Get the vehicle with <vin> vin Not Removed
    And Extend the vehicle's original uuid
    And fill in the payload with the <oem> vin and new uuid with force refresh
    And publish the message and wait for 1 seconds
    And The new item should exist in the vehicle table and the status should be initial
    And Query myVehicle until the status is None, Partial or Complete
    Then The new item should exist in the vehicle table and the status should be none
    And close the browser
    And set the removed flag to true for myVehicle

    Examples:
      | oem | status   | vin               |
      | aoa | partial  | WA1LFAFP8FA011332 |
      | aoa | complete | WAUG3AFC3JN049188 |
#      | aoa | partial  | WA1LFAFP8FA011332 |
#      | aoa | complete | WAUG3AFC3JN049188 |

      # WA1LFAFP8FA011332 - This vehicle has Partial status in the image_ingester table, but no image in AOA Server,
      # WAUG3AFC3JN049188 - This vehicle has Complete status in the image_ingester table, but no image in AOA Server,

