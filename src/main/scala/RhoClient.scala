package com.rho.client

// Importing some classes we're gonna need
import java.net.URI
import java.io._
import java.util.ArrayList

// Apache imports
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.utils.URIBuilder 
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.http.client.methods
import org.apache.http.client.params
import org.apache.http.message.BasicNameValuePair
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.protocol.HttpContext
import org.apache.http.protocol.HttpCoreContext
import org.apache.http.client.protocol.HttpClientContext

class RhoClient(Host: String = "localhost", Scheme: String = "http", Headers: Map[String,String] = Map()) {

  // (Attrscheme Attrhost AttrURI)
  // Strings specifying scheme and host
  val scheme: String = Scheme
  val host: String = Host
  def URI(): URI = new URIBuilder().setScheme(scheme).setHost(host).build()

  // (Attrclient) (Attrresponse) (Attrencoding)
  var client:   CloseableHttpClient = HttpClients.createDefault()
  var response: HttpResponse = null
  var encoding: String = "UTF-8"

  // (Attrrequest_headers) (Attrresponse_headers)
  var request_headers : Map[String,String] = Headers
  var response_headers: Map[String,String] = Map()

  // (Attrcontext) (AttrcontextInfo)
  var context    : HttpCoreContext = new HttpCoreContext()
  var contextInfo: HttpClientContext = HttpClientContext.adapt(context);



/*  ********************************
  *** URI MANIPULATION METHODS ***
  ******************************** */


/*   (MthdbuildURI) buildURI
  RhoClient may change URI on the fly, or be set initially
  I use the "" string as a default, this string tells buildURI
  to do nothing to the field in question.
  Ex. If Host = not given or "" Host will be unchanged.
  Parameters is a String to String Map,
  if Paramaeters is only the empty Map(),
  buildURI will do nothing to the Parameters of the URI. */

  def buildURI(Scheme    : String = "", 
       Host      : String = "", 
       Path      : String = "", 
       Parameters: Map[String,String]
        = Map() ): URI = {

    // Making sure that if an argument is not passed 
    // when buildURI() is called, the old values from 
    // RhoClient.URI are used for the new URI.
    // Parameters are handled differently, it's an array

    val SchemeUsed = Scheme match {
      case ""  => this.scheme
      case Scheme => Scheme
    }

    val HostUsed = Host match {
      case "" => this.host
      case Host  => Host
    }

    val PathUsed = Path match {
      case "" => Path
      case Path  => Path
    }

    // Build URI except for Parameters
    var URI = new URIBuilder()
      .setScheme(SchemeUsed)
      .setHost(HostUsed)
      .setPath(PathUsed)
      .build()

    // Add Parameters through iteration of the map keys
    if (Parameters != Map()) {
      for (k <- Parameters.keys) {
        URI = new URIBuilder(URI)
        .setParameter(k,Parameters(k))
        .build()
      }
    }

    return URI

  }



/*  ***********************************
  *** CLIENT MANIPULATION METHODS ***
  *********************************** */



// Auxiliary function for handling HTTP requests, request must be formed
  private def doRequest(request: HttpRequestBase): String = {
    val request_checked = request match {
      case (x: HttpGet)  => request
      case (y: HttpPost) => request
      case (_) => throw new Error("[RhoClient] Invalid request type for doRequest")
    }
    // Extract response entity and headers
    response = client.execute(request_checked,context)
    response_headers = (for (enc<-response.getAllHeaders) yield (enc.getName,enc.getValue)).toMap
    // Deal with encoding
    val useenc = {
      if (response_headers.contains("Content-Type")) {
        ("charset=(.+?)($|,|\\s|;)".r findFirstMatchIn response_headers("Content-Type")) match {
          case Some(x) => x.group(1) // Get the thing in (.+?)
          case None    => encoding
        }
      } else {
        encoding
      }
    }
    // Extract response body
    val entity = response.getEntity()
    val content = {
      if (entity != null) {
        val stream = entity.getContent()
        val con = io.Source.fromInputStream(stream,useenc).getLines.mkString
        stream.close
        con
      } else {
        ""
      }
    }
    // HTTPComponents asks for this method to be called to reuse the client
    EntityUtils.consume(entity)
    content
  }



/*  (MthddoGET)
  Performs a GET request to Obj.URI */
  def doGET(getparams: Map[String,String] = Map(),path: String = ""): String = {
    // Initializing client object
    val URI = buildURI(this.scheme,this.host,path,getparams)
    val get = new HttpGet(URI)
    // Headers, only if they exist
    if (request_headers != Map()) {
      for (k <- request_headers.keys) {
        get.addHeader(k,request_headers(k))
      }
    }
    doRequest(get)
  }



/*  (MthddoPOST)
  Performs a POST request to Obj.URI */
  def doPOST(params: Map[String,String], getparams: Map[String,String] = Map(), path: String = ""): String = {
    // Initializing client object
    val URI = buildURI(this.scheme,this.host,path,getparams)
    val post = new HttpPost(URI)
    // Headers, only if the exist
    if (request_headers != Map()) {
      for (k <- request_headers.keys) {
        post.addHeader(k,request_headers(k))
      }
    }
    // Parameters
    val nvp = new ArrayList[NameValuePair](1)
    for (k <- params.keys) {
      nvp.add(new BasicNameValuePair(k,params(k)))
    }
    post.setEntity(new UrlEncodedFormEntity(nvp))
    doRequest(post)
  }



/*  (Mthdclose)
  Closes client, does nothing else, useless. */
  def close() {
    client.close()
  }

} // End of class
