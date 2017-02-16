// This one performs a GET to checkip.dyndns.org,
// then outputs the HTML content.

import com.rho.client.RhoClient

object B_GET {

  def main(args: Array[String]) {

    // Init
    val prefix="[Demo] "
    println(prefix+"Start")

    // Set URI, parameters
    System.out.println(prefix+"Initializing RhoClient object, setting URI")
    val Client = new RhoClient("checkip.dyndns.org") // Init client
    System.out.println(Client.URI)

    // Perform GET request
    System.out.println(prefix+"Performing GET to checkip.dyndns.org")
    val content = Client.doGET() // GET, no path and no params
    Client.close() // Close client

    // Display results
    System.out.println(prefix+"Response status will be displayed")
    System.out.println(Client.response) // Response metadata
    System.out.println(prefix+"If succesful, this will return your IP")
    System.out.println(content) // Response content
    println(prefix+"Done")  

  }

}
