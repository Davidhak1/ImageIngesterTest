Feature: E2E Testing image ingester logic for bmw & aoa including rabbitMQ and SQL Table

  Background:
    Given showcase initialize browser
    And Navigate showcase to "http://wcinv-showcase01.dealer.ddc:9745/showcase/showcase/inventoryLocator/"


  @bmw_end2end
  @bmw_showcase
  @showcase_1
  Scenario Outline: Showcase validation of the right vehicle info and the quantity of images

    When Get a random <oem> vehicle that has <quantity> number of images mapped to it in db
    And fill in the search textbox with the uuid of the vehicle
    And choose QA environment and press Locate
    Then the vin of the showcase should be equal to the vehicle's vin
    And vinSpecificStockImages should be equal to max number of images
    And showcase close browser

    Examples:
      | oem | quantity |
      | bmw | max      |
      | bmw | min      |
      | aoa | max      |
      | aoa | min      |



  @bmw_end2end
  @bmw_showcase
  @showcase_2
  Scenario Outline: Showcase validation the images are being showed under showcase media

    When Get a random <oem> vehicle that has <quantity> number of images mapped to it in db
    And fill in the search textbox with the uuid of the vehicle
    And choose QA environment and press Locate
    Then the vin of the showcase should be equal to the vehicle's vin
    And vinSpecificStockImages should be equal to max number of images
    When press on media tab
    Then We should see the exact amount of images under Vin-Specific stock images
    And showcase close browser

    Examples:
      | oem | quantity |
      | bmw | max      |
      | bmw | min      |
      | aoa | max      |
      | aoa | min      |

