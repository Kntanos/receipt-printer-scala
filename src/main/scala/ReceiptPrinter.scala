import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter

class CafeDetails (
                    val shopName: String,
                    val address: String,
                    val phone: String,
                    val prices: Map[String, Double]
                  )

class ReceiptPrinter(val cafe: CafeDetails, var order: Map[String, Int] = Map()) {

  val cafeInfo: String =
    s"${cafe.shopName}, ${cafe.address}, ${cafe.phone}"

  val dateAndTime: String = {
    val instant = Instant.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm").withZone(ZoneId.systemDefault())
    val formattedDate = formatter.format(instant)
    formattedDate
  }

  val orderWithPrices: Iterable[(String, Int, Double)] =
    for (item <- order;
         entry <- cafe.prices;
         if (item._1 == entry._1))
    yield (item._1, item._2, (item._2 * entry._2))

  def itemToLine(line: (String, Int, Double)): String = {
    f"${line._2} x ${line._1}%-10s${line._3}%10s"
  }

//  def printItemsList(listOfItems: Iterable[(String, Int, Double)]): String =
//    listOfItems.map(itemToLine).mkString("")

  val totalPrice: Double =
    orderWithPrices.foldLeft(0.0)((total, order) => total + order._3)

  val vatAdded: Double = {
    val vat = totalPrice * 0.2
    Math.round(vat*100.0)/100.0
  }

  def receipt: String = {
    s"""${cafeInfo}
    |${dateAndTime}
    |${orderWithPrices.map(itemToLine).mkString("\n")}
    Total ${totalPrice}
    VAT ${vatAdded}""".stripMargin
  }
}
