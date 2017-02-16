// Main singleton

/*
Send a GET with meaningless headers for demo purposes
*/
import java.io._
import com.rho.client.RhoClient

object D_Headers {

  def main(args: Array[String]) {

    // Opening a file for writing
    val file   = new File("data/google.html")
    val writer = new BufferedWriter(new FileWriter(file))  
    val prefix="[Demo] "
    println(prefix+"Start")

    // Setting some variables, path is long
    var host = "www.google.com"

    // Set URI, parameters
    System.out.println(prefix+"Initializing RhoClient object, setting URI")
    val Client = new RhoClient(host,"http")
    System.out.println(Client.URI)

    // Set Headers
    System.out.println(prefix+"Defining headers")  
    var heads = Map("Dummy 1"->"foo","Dummy 2"->"bar")
    Client.request_headers = heads
    System.out.println(Client.request_headers)
  
    // Perform GET request
    System.out.println(prefix+"Performing GET to google server")  
    val content = Client.doGET()
    System.out.println(prefix+"Response status")    
    System.out.println(Client.response)    
    System.out.println(prefix+"Response will be saved to data/google.html")
    writer.write(content)
    writer.close()
    Client.close()
    println(prefix+"Done")
  
  }

}
