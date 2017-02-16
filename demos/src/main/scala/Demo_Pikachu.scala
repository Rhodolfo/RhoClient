// Main singleton

/*
Performs GET to
http://www.coppel.com/es/tienda10/amiibo-pikachu-nintendo-pm-2319453
then extracts data with RegEx
*/

import java.io._
import scala.util.matching.Regex
import com.rho.client.RhoClient

object E_Parse {
  def main(args: Array[String]) {

    val prefix = "[Pikachu] "
    println(prefix+"Start")

    // Setting some variables, path is long
    var host = "www.coppel.com"
    var path = "/amiibo-pikachu-nintendo-pm-2319453"

    // Set URI, parameters
    System.out.println(prefix+"Initializing RhoClient object, setting URI")
    val Client = new RhoClient(host)
    System.out.println(Client.URI)

    // Perform GET request
    System.out.println(prefix+"Performing GET to server")  
    val page = Client.doGET(Map(),path)
    Client.close() // Done with the client

    // Regex lookups for regular prices
    var regex_name  = new Regex("id=\"ProductInfoName_.+?/>")
    var regex_sku   = new Regex("id=\"product_SKU.+?</span>")
    var regex_price = new Regex("id=\"ProductInfoPrice.+?/>")
    var regex_cred  = new Regex("\\d{1,6}\\s{1,}en\\s{1,}\\d{1,4}.+?uincenas.+?twoWeeksprice.+?Quincenal<")
  
    var name  = (regex_name  findFirstIn page).mkString
    name  = (".+?value=\"".r replaceAllIn(name,""))
    name  = ("\".+?>".r replaceAllIn(name,""))

    var sku   = (regex_sku   findFirstIn page).mkString
    sku   = (".+?:".r replaceAllIn(sku,""))
    sku   = ("<.+?>".r replaceAllIn(sku,""))

    var price = (regex_price findFirstIn page).mkString
    price  = (".+?value=\"\\$".r replaceAllIn(price,""))
    price  = ("\".+?>".r replaceAllIn(price,""))

    var cred  = (regex_cred  findFirstIn page).mkString

    // Coppel credit requires special care

    var regex_num = new Regex("\\d{1,}&.+?quincenas")
    var regex_pag = new Regex(">\\$\\d{1,}.+?uincenal")
    var regex_tot = new Regex("\\d.+?en\\s{1,}")
    
    var quin_num = (regex_num findFirstIn cred).mkString
    quin_num = ("\\D".r replaceAllIn(quin_num,""))

    var quin_pag = (regex_pag findFirstIn cred).mkString
    quin_pag = ("\\D".r replaceAllIn(quin_pag,""))

    var quin_tot = (regex_tot findFirstIn cred).mkString
    quin_tot = ("id=.+?\\$".r replaceAllIn(quin_tot,""))
    quin_tot = ("\\D".r replaceAllIn(quin_tot,""))

    // Print it
    println("                        Producto: "+name)
    println("                             SKU: "+sku)
    println("                          Precio: "+price)
    println("Credito Coppel Num. de Quincenas: "+quin_num)
    println("Credito Coppel Pago por Quincena: "+quin_pag)
    println("          Pago total con Credito: "+quin_tot)    

    // Done
    println(prefix+"Done")
  
  }

}
