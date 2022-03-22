import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.{Clock, Instant, ZoneId}

class ReceiptPrinterTest extends AnyWordSpec with Matchers {
  val coffeeConnectionCafe = new CafeDetails(
    "The Coffee Connection",
    "123 Lakeside Way",
    "16503600708",
    Map(
      "Cafe Latte" -> 4.75,
      "Flat White" -> 4.75,
      "Cappuccino" -> 3.85,
      "Single Espresso" -> 2.05,
      "Double Espresso" -> 3.75,
      "Americano" -> 3.75,
      "Cortado" -> 4.55,
      "Tea" -> 3.65,
      "Choc Mudcake" -> 6.40,
      "Choc Mousse" -> 8.20,
      "Affogato" -> 14.80,
      "Tiramisu" -> 11.40,
      "Blueberry Muffin" -> 4.05,
      "Chocolate Chip Muffin" -> 4.05,
      "Muffin Of The Day" -> 4.55
    )
  )
  val printer = new ReceiptPrinter(
    coffeeConnectionCafe,
    Map("Cafe Latte" -> 1,
      "Tea" -> 2,
      "Affogato" -> 1),
    Clock.fixed(Instant.parse("2022-03-21T16:22:00.00Z"), ZoneId.systemDefault())
  )

  "The cafeInfo" should {
    "include details" which {
      "contains the name, address and phone number of the cafe" in {

        printer.cafeInfo should equal ("The Coffee Connection, 123 Lakeside Way, 16503600708")
      }
    }
  }

  "The dateAndTime" should {
    "return a string of the current date and time" which {
      "is formatted dd/MM/yyy HH:mm" in {

        printer.dateAndTime should equal ("21/03/2022 16:22")
      }
    }
  }

//  "The orderWithPrices" should {
//    "generate a list of tuples" which {
//      "each contains the item, the units ordered and the total cost for the units" in {
//
//        printer.orderWithPrices should have size (3)
//        printer.orderWithPrices.head._3 should equal (4.75)
//        printer.orderWithPrices.tail.head._3 should equal (7.3)
//      }
//    }
//  }

  "The totalPrice" should {
    "calculate the cost of the order" which {
      "sums up the cost of all items" in {

        printer.totalPrice should equal (26.85)
      }
    }
  }
  "The vatAdded" should {
    "calculate the added value tax" which {
      "is 20% of the total price of the order" in {

        printer.vatAdded should equal (5.37)
      }
    }
  }

  println(printer.receipt)

    "A ReceiptPrinter" should {
      "format a receipt" which {
        "contains the cafe's details, a list of all items, total and VAT" in {

          printer.receipt should equal (f"""The Coffee Connection, 123 Lakeside Way, 16503600708
                                           |21/03/2022 16:22
                                           |1 x Cafe Latte           4.75
                                           |2 x Tea                  7.30
                                           |1 x Affogato             14.80
                                           |Total 26.85
                                           |VAT 5.37""".stripMargin)
        }
      }
    }
}
