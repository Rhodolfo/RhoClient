// Main singleton
/* Here we do URI manipulation */
import com.rho.client.RhoClient

object A_URI {
  def main(args: Array[String]) {
    val prefix="[Demo] "
    println(prefix+"Start")
    // Set URI, some sample URI operations
    val Client = new RhoClient("www.google.com","https")
    System.out.println(prefix+"URI initialized")
    System.out.println(Client.URI)
    println(prefix+"Done")
  }
}
