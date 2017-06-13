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
    val host = "www.google.com"

    // Set URI, parameters, add headers
    System.out.println(prefix+"Initializing RhoClient object, setting URI and headers")
    val heads = Map("Dummy 1"->"foo","Dummy 2"->"bar")
    val Client = new RhoClient(host,"http",heads)
    System.out.println(Client.URI)

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
