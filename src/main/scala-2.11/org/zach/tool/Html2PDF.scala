package org.zach.tool

import io.github.cloudify.scala.spdf._
import java.io._
import java.net._

object Html2PDF extends App {

  // Create a new Pdf converter with a custom configuration
  // run `wkhtmltopdf --extended-help` for a full list of options
  val pdf = Pdf(new PdfConfig {
    orientation := Landscape
    pageSize := "Letter"
    marginTop := "1in"
    marginBottom := "1in"
    marginLeft := "1in"
    marginRight := "1in"
  })

  val page = <html>
    <body>
      <h1>Hello World</h1>
    </body>
  </html>

  // Save the PDF generated from the above HTML into a Byte Array
  val outputStream = new ByteArrayOutputStream
  pdf.run(page, outputStream)

  // Save the PDF of Google's homepage into a file
  pdf.run(new URL("http://www.baidu.com"), new File("bd.pdf"))
  println("finished")

}
