Feature: Login into Application

  @bmw_smoke
  @first
  Scenario Outline: Validating the queue and the timestamp for vehicles that have the same VIN already existing in the db with status initial
    Automated: no
    Given Initialize the browser with chrome
    And Get a random <oem> vehicle with <status> and Not removed
    And Set the next_schedyled timestamp to NULL for those who are not NULL
    And Navigate to "http://vtqase-platrabbitmq01.dyn.dealer.ddc:15672/#/channels"
    When User enters username and password and logs in
    And Go to queues
    And Purge high and low priority queues
    And Choose image-action-queue
    And set the generic headers and properties for the publish
    And generate a new uuid
    And fill in the payload with the <oem> vin and new uuid
    And publish the message and wait for three seconds
    Then The new item should exist in the vehicle table and the status should be <status>
    And The next_scheduled_date of the new item should be less than 1 hour
    And Go to queues
    And Choose image-action-high-priority queue
    And Wait until the number of messages in the queue changes
    And Query myVehicle again
    And The next_scheduled_date of the item should be greater but no more than 1 hours
    And close the browser
    And set the removed flag to true for myVehicle

    Examples:
      |oem    |status |
      |bmw    |initial|
      |aoa    |initial|

#Manually we can also check if the message exists in the queue or not

#----------------------------------------------------------------------------------------------------------------------------


  @second
  @bmw_smoke
  Scenario Outline: Validating the queue and the timestamp for vehicles that have the same VIN already existing in the db with status none and partial
  Automated: no
    Given Initialize the browser with chrome
    And Get a random <oem> vehicle with <status> and Not removed
    And Set the next_schedyled timestamp to NULL for those who are not NULL
    And Navigate to "http://vtqase-platrabbitmq01.dyn.dealer.ddc:15672/#/channels"
    When User enters username and password and logs in
    And Go to queues
    And Purge high and low priority queues
    And Choose image-action-queue
    And set the generic headers and properties for the publish
    And generate a new uuid
    And fill in the payload with the <oem> vin and new uuid
    And publish the message and wait for three seconds
    Then The new item should exist in the vehicle table and the status should be <status>
    And The next_scheduled_date of the new item should be between 6-8 days from now
    And change the next-shchedule_date to the current date
    And Go to queues
    And Choose image-action-low-priority queue
    And Wait until the number of messages in the queue changes
    And Query myVehicle again
    And The next_scheduled_date of the new item should be between 6-8 days from now
    And close the browser
    And set the removed flag to true for myVehicle



    Examples:
    |oem    |status   |
    |bmw    |none     |
    |bmw    |partial  |
#    |aoa    |none     |
#    |aoa    |partial  |


#Manually we can also check if the message exists in the queue or not

  #----------------------------------------------------------------------------------------------------------------------------

  @third
  @bmw_smoke
  Scenario Outline: Test validating the queue and the timestamp for vehicles that already exist in the db with status complete
    Given Initialize the browser with chrome
    And Get a random <oem> vehicle with <status> and Not removed
    And Set the next_schedyled timestamp to NULL for those who are not NULL
    And Navigate to "http://vtqase-platrabbitmq01.dyn.dealer.ddc:15672/#/channels"
    When User enters username and password and logs in
    And Go to queues
    And Purge high and low priority queues
    And Choose image-action-queue
    And set the generic headers and properties for the publish
    And generate a new uuid
    And fill in the payload with the <oem> vin and new uuid
    And publish the message and wait for three seconds
    Then The new item should exist in the vehicle table and the status should be <status>
    And The next_scheduled_date of the new item should be more than 1000 years
    And close the browser
    And set the removed flag to true for myVehicle



    Examples:
      |oem |status     |
      |bmw |complete   |
#      |aoa |complete   |

#----------------------------------------------------------------------------------------------------------------------------

  @fourth
  @bmw_smoke
  Scenario Outline:  Test validating the queue and the timestamp for vehicles that already exist in the db with all statuses and Force Refreshed
    Given Initialize the browser with chrome
    And Get a random <oem> vehicle with <status> and Not removed
    And Set the next_schedyled timestamp to NULL for those who are not NULL
    And Navigate to "http://vtqase-platrabbitmq01.dyn.dealer.ddc:15672/#/channels"
    When User enters username and password and logs in
    And Go to queues
    And Purge high and low priority queues
    And Choose image-action-queue
    And set the generic headers and properties for the publish
    And generate a new uuid
    And fill in the payload with the <oem> vin and new uuid with force refresh
    And publish the message and wait for three seconds
    Then The new item should exist in the vehicle table and the status should be <statusExpected>
    And The next_scheduled_date of the new item should be less than 1 hour
    And Go to queues
    And Choose image-action-high-priority queue
    And Wait until the number of messages in the queue changes
    And Query myVehicle again
    And The next_scheduled_date of the item should be greater but no more than 1 hours
    And close the browser
    And set the removed flag to true for myVehicle



    Examples:
      |oem    |status   |statusExpected  |
#      |bmw    |initial  |initial         |
#      |bmw    |none     |initial         |
#      |bmw    |partial  |initial         |
#      |bmw    |complete |initial         |
#      |aoa    |initial  |initial         |
#      |aoa    |none     |initial         |
#      |aoa    |partial  |initial         |
#      |aoa    |complete |initial         |
#----------------------------------------------------------------------------------------------------------------------------

  @fifth
  @bmw_smoke
  Scenario Outline: Validating if all the images are mapped to new items that have VINs already existing in the db with completed or partial solutions
    Given Initialize the browser with chrome
    And Get a random <oem> vehicle with <status> and Not removed
    And Set the next_schedyled timestamp to NULL for those who are not NULL
    And Navigate to "http://vtqase-platrabbitmq01.dyn.dealer.ddc:15672/#/channels"
    When User enters username and password and logs in
    And Go to queues
    And Purge high and low priority queues
    And Choose image-action-queue
    And set the generic headers and properties for the publish
    And generate a new uuid
    And fill in the payload with the <oem> vin and new uuid
    And publish the message and wait for three seconds
    Then The new item should exist in the vehicle table and the status should be <status>
    And Vehicles with old and new uuids and should have have equal number of images mapped
    And close the browser
    And set the removed flag to true for myVehicle



    Examples:
      |oem    |status   |
      |bmw    |partial  |
      |bmw    |complete |
#      |aoa    |partial  |
#      |aoa    |complete |
#----------------------------------------------------------------------------------------------------------------------------
