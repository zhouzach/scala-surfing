package org.zach.utils

import java.awt.Color
import java.io.{ByteArrayOutputStream, OutputStream}
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.ContentDispositionTypes
import akka.stream.scaladsl.Source
import akka.util.ByteString
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jfree.chart.{ChartFactory, ChartUtils}
import org.jfree.chart.axis.DateAxis
import org.jfree.chart.ui.RectangleEdge
import org.jfree.data.time.{Minute, TimeSeries, TimeSeriesCollection}
import org.slf4j.LoggerFactory

object DownloadForGraph {

  private val logger = LoggerFactory.getLogger(this.getClass)


  private def createTimeSeriesChart(lineDataset: TimeSeriesCollection, title: String, x: String = "time", y: String = "value") = {
    val chart = ChartFactory.createTimeSeriesChart(title, x, y, lineDataset, true, true, false)
    chart.setTextAntiAlias(false)

    val plot = chart.getXYPlot
    plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF))

    val dateAxis = plot.getDomainAxis.asInstanceOf[DateAxis]
    val format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    dateAxis.setDateFormatOverride(format)

    val rangeAxis = plot.getRangeAxis
    rangeAxis.setUpperMargin(0.1)
    rangeAxis.setLowerMargin(0.1)
    rangeAxis.setLowerBound(0)

    logger.debug(s"title:[${title}]")
    title match {
      case "FILESYSTEM.FSCapacity[/sys/fs/cgroup]" => rangeAxis.setUpperBound(0.5)
      case "FILESYSTEM.FSCapacity[/boot]" => rangeAxis.setUpperBound(0.3)
      case _ => rangeAxis.setAutoRange(true)
    }

    chart.getLegend.setPosition(RectangleEdge.TOP)
    chart
  }

  private def createDatasetByTimeSeries(data: Seq[(String, Map[String, BigDecimal])]): TimeSeriesCollection = {
    val lineDataset = new TimeSeriesCollection
    val keys = data.head._2.keys.toArray
    keys.foreach { x =>
      println(s"map key: $x")
      val series = new TimeSeries(x)
      println(s"TimeSeries: $series")
      data.foreach { y =>
        println(s"y_1: ${y._1}")
//        series.addOrUpdate(new Minute(new Date()), y._2.getOrElse(x, BigDecimal(0)))
                series.addOrUpdate(new Minute(new Date(y._1)), y._2.getOrElse(x, BigDecimal(0)))
      }
      println("--------")
      lineDataset.addSeries(series)
    }
    lineDataset
  }

  val octetContentType = ContentType(MediaTypes.`application/octet-stream`.withParams(Map("charset" -> "UTF-8")))

  def disposition(format: String, fileName: String, encode: String = "UTF-8") = {
    headers.`Content-Disposition`(
      ContentDispositionTypes.attachment,
      Map("filename" ->
        (URLEncoder.encode(fileName, "UTF-8").replace(s".$format", "") + s".$format"))
    )
  }

  def getOctetHttpResponse(array: Array[Byte], format: String,
                           fileName: String, encode: String = "UTF-8") = {
    HttpResponse(
      status = StatusCodes.OK,
      entity = HttpEntity(octetContentType, Source.single(ByteString(array)))
    ).addHeader(disposition(format, fileName))
  }

  def exportReportExcelByJFreeChart(title: String,
                                    nameplate: List[String],
                                    dataSet: Map[String, Seq[(String, Map[String, BigDecimal])]],
                                    out: OutputStream) = {

    val workbook = new XSSFWorkbook
    try {
      logger.info("===== start generate excel report! =====")
      val sheet = workbook.createSheet(title)

      println("nameplate zip with index:")
      nameplate.zipWithIndex.foreach {
        println(_)
      }

      nameplate.zipWithIndex.foreach {

        case (value, i) =>
          sheet.createRow(i).createCell(0).setCellValue(value)
      }
      val size = nameplate.size
      val drawing = sheet.createDrawingPatriarch


      dataSet.toList.sortBy(_._1).zipWithIndex.foreach {
        case (c, i) =>
          val (instance, instanceGroup) = c
          println(s"instance: $instance")
          val chart = createTimeSeriesChart(createDatasetByTimeSeries(instanceGroup), instance)
          val anchor = drawing.createAnchor(0, 0, 0, 0, 0, size + i * 25, 20, 30 + i * 25)
          val byteArrayOut = new ByteArrayOutputStream()
          ChartUtils.writeChartAsPNG(byteArrayOut, chart, 1300, 400)
          drawing.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray, XSSFWorkbook.PICTURE_TYPE_BMP))
          byteArrayOut.close()
      }
      workbook.write(out)
      logger.info("===== generate excel report success! =====")
    } catch {
      case e: Exception =>
        logger.error(e.getMessage)
        e.printStackTrace()
      //        throw new Exception(e)
    } finally {
      workbook.close()
    }
  }


  def main(args: Array[String]): Unit = {

    println("get excle")
    val nameplate = List("报表名称：性能报表", s"业务系统：媒体", s"子业务系统：媒体广告位",
      s"服务器列表：host1,host2", s"查询开始时间：2018-11-1",
      s"查询结束时间：2018-11-2", s"报表生成时间：2018-11-3")

    val outputStream = new ByteArrayOutputStream()
    var array = outputStream.toByteArray

    val dataSet: Map[String, Seq[(String, Map[String, BigDecimal])]] = Map("china" -> Seq(("1541046786000", Map("shanghai" -> BigDecimal(1.0)))))
    try {
      exportReportExcelByJFreeChart("Sheet 1", nameplate, dataSet, outputStream)
      array = outputStream.toByteArray
      array.foreach(println(_))
      println(s"${array.length}")
    } catch {
      case exp: Exception =>
        exp.printStackTrace()
        logger.error(exp.getMessage)
    } finally {
      outputStream.close()
    }

    getOctetHttpResponse(array, "xlsx", s"testfile")

  }


}
