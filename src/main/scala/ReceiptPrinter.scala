import java.time.{Clock, Instant, ZoneId}
import java.time.format.DateTimeFormatter

class CafeDetails (
                    val shopName: String,
                    val address: String,
                    val phone: String,
                    val prices: Map[String, Double]
                  )

class ReceiptPrinter(val cafe: CafeDetails, var order: Map[String, Int] = Map(), val clock: Clock = Clock.systemUTC()) {

  val cafeInfo: String =
    f"${cafe.shopName}, ${cafe.address}, ${cafe.phone}"

  def dateAndTime: String = {
    val instant = Instant.now(clock)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm").withZone(ZoneId.systemDefault())
    val formattedDate = formatter.format(instant)
    formattedDate
  }

  val getPrice: (String, Int) => String = (itemName: String, quantity: Int) =>
    "%.2f".format(cafe.prices(itemName) * quantity)

  val formatLine: (String, Int, String) => String = (itemName: String, quantity:Int, price: String) =>
    f"$quantity x $itemName%-20s $price"

  def mapOrder(orderToMap: Map[String, Int]): Iterable[String] = {
    orderToMap map { case (k, v) => formatLine(k, v, getPrice(k, v))}
  }

  val getPrices: (String, Int) => Double = (itemName: String, quantity: Int) =>
    cafe.prices(itemName) * quantity

  def totalPrice: Double =
    (order map { case (k, v) => cafe.prices(k) * v}).sum

  val vatAdded: (Double) = {
    val vat = totalPrice * 0.2
    Math.round(vat*100.0)/100.0
  }

  def receipt: String = {
    f"""${cafeInfo}
       |${dateAndTime}
       |${mapOrder(order).mkString("\n")}
       |Total ${totalPrice}
       |VAT ${vatAdded}""".stripMargin
  }
}
