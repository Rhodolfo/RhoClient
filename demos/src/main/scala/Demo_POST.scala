// Main singleton


/*
Sends POST query to 
http://www.coppel.com/ProductListingView?categoryId=13054&storeId=12759&pageSize=72
just for demo purposes
*/

import java.io._
import java.net.URI
import scala.io.Source
import scala.util.matching.Regex
import com.rho.client.RhoClient

object C_POST {

  // Data
  val    datdir: String = "data/"
  val      file: String = "post.html"
  val    prefix: String = "[Coppel] "
  val      host: String = "www.coppel.com"

  // Main routine
  def main(args: Array[String]) {

    System.out.println(prefix+"Start")

    // Let's save POST params
    val path = "/ProductListingView"
    val postparams = Map("pagesize"->"72",
      "nIndex"->"0",
      "productBeginIndex"->"72",
      "beginIndex"->"72",
      "storeId"->"12759",
      "catalogId"->"10001",
      "requesttype"->"ajax") // I found these parameters through scrapping Coppel
    val getparams = Map("categoryId"->"13054","storeId"->"12759","pageSize"->"72")
  
    // Extracting pre-existing parameters, save in oldpar
    val Client = new RhoClient(host)

    val content = Client.doPOST(postparams,getparams,path) // Perform POST with params
    System.out.println(prefix+"Response status")
    println(Client.response)
    System.out.println(prefix+"Response will be saved to "+datdir+file)
    val writer = new BufferedWriter(new FileWriter(datdir+file))
    writer.write(content)
    writer.close()

  }

}
