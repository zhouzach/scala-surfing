package org.rabbit.utils

import java.awt.Color
import java.io.{ByteArrayOutputStream, File, OutputStream}
import java.text.{DateFormat, SimpleDateFormat}
import java.util.Date
import java.util.Map.Entry
import java.util.concurrent.{ConcurrentHashMap, Executors}
import java.util.{Date, ArrayList => JArrayList}

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.headers.ContentDispositionTypes
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}
import com.typesafe.config.{Config, ConfigFactory, ConfigValue}
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined
import org.apache.poi.ss.usermodel.{CellStyle, FillPatternType, Sheet, Workbook}
import org.apache.poi.ss.usermodel.charts.{AxisCrosses, AxisPosition, DataSources, LegendPosition}
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.{XSSFChart, XSSFRichTextString, XSSFWorkbook}
import org.jfree.chart.{ChartFactory, ChartUtils}
import org.jfree.chart.axis.DateAxis
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.ui.RectangleEdge
import org.jfree.data.category.{CategoryDataset, DefaultCategoryDataset}
import org.jfree.data.time.{Minute, TimeSeries, TimeSeriesCollection}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future, Promise}


final class ImperativeRequestContext(ctx: RequestContext, promise: Promise[RouteResult]) {
  private implicit val ec = ctx.executionContext

  val request = ctx.request

  def complete(obj: ToResponseMarshallable): Unit = ctx.complete(obj).onComplete(promise.complete)

  def fail(error: Throwable): Unit = ctx.fail(error).onComplete(promise.complete)
}

object ImperativeRequestContext {
  def imperativelyComplete(inner: ImperativeRequestContext => Unit): Route = {
    ctx: RequestContext =>
      val p = Promise[RouteResult]()
      inner(new ImperativeRequestContext(ctx, p))
      p.future
  }
}

object DownloadForExcel {

  def exportExcel(req: ImperativeRequestContext, data: Future[Seq[Map[String, Any]]]) = {
    val ex = new ExportExcel
    val conf = EquipmentExcelConf
    val os = new ByteArrayOutputStream

    val actorThreadPool = Executors.newFixedThreadPool(50)
    val actorExecutionContext = ExecutionContext.fromExecutorService(actorThreadPool)
    implicit val ec: ExecutionContext = actorExecutionContext

    data.flatMap {
      case res =>
        ex.exportExcel(conf.getTitle, conf.getHeaders, conf.getBody, res, os)
        val ret = os.toByteArray
        os.close()
        Future(req.complete(parsingDownloadResponse(ret, conf.getFileName)))
    }
  }


  def parsingDownloadResponse(source: Array[Byte], fileName: String): HttpResponse = {
    val entity = HttpEntity(ContentTypes.`application/octet-stream`, source).withoutSizeLimit()
    val disposition = headers.`Content-Disposition`.apply(ContentDispositionTypes.inline, Map("filename" -> fileName))
    HttpResponse(StatusCodes.OK, entity = entity).addHeader(disposition)
  }
}

class ExportExcel {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def createDataset(data: Seq[(String, Map[String, BigDecimal])]): CategoryDataset = {

    val dataset = new DefaultCategoryDataset()
    data.foreach { x =>
      val (time, group) = x
      group.foreach { case (k, v) =>
        dataset.addValue(v, k, time)
      }
    }
    dataset
  }

  def createChart(createDataset: CategoryDataset, title: String, x: String = "time", y: String = "value") = {
    val chart = ChartFactory.createLineChart(title, x, y, createDataset, PlotOrientation.VERTICAL, true, false, false)
    chart.setTextAntiAlias(false)

    val plot = chart.getCategoryPlot
    plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF))
    plot.getRangeAxis().setUpperMargin(0.1)
    plot.getRangeAxis.setLowerMargin(0.1)

    chart.getLegend.setPosition(RectangleEdge.TOP)
    chart
  }

  def createDatasetByTimeSeries(data: Seq[(String, Map[String, BigDecimal])]): TimeSeriesCollection = {
    val lineDataset = new TimeSeriesCollection
    val keys = data.head._2.keys.toArray
    keys.foreach { x =>
      val series = new TimeSeries(x)
      data.foreach { y =>
        series.addOrUpdate(new Minute(new Date(y._1)), y._2.getOrElse(x, BigDecimal(0)))
      }
      lineDataset.addSeries(series)
    }
    lineDataset
  }

  def createTimeSeriesChart(lineDataset: TimeSeriesCollection, title: String, x: String = "time", y: String = "value") = {
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

  def exportReportExcelByJFreeChart(title: String,
                                    nameplate: List[String],
                                    dataSet: Map[String, Seq[(String, Map[String, BigDecimal])]],
                                    out: OutputStream) = {

    val workbook = new XSSFWorkbook
    try {
      logger.info("===== start generate excel report! =====")
      val sheet = workbook.createSheet(title)
      nameplate.zipWithIndex.foreach {
        case (value, i) =>
          sheet.createRow(i).createCell(0).setCellValue(value)
      }
      val size = nameplate.size
      val drawing = sheet.createDrawingPatriarch
      dataSet.toList.sortBy(_._1).zipWithIndex.foreach {
        case (c, i) =>
          val (instance, instanceGroup) = c
          //          val chart = createChart(createDataset(instanceGroup), instance)
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
        throw new Exception(e)
    } finally {
      workbook.close()
    }
  }

  def exportReportExcel(title: String,
                        nameplate: List[String],
                        dataSet: Map[String, Seq[(String, Map[String, BigDecimal])]],
                        out: OutputStream) = {

    val workbook = new XSSFWorkbook
    try {
      logger.info("===== start generate excel report! =====")
      val sheet = workbook.createSheet(title)
      nameplate.zipWithIndex.foreach {
        case (value, i) =>
          sheet.createRow(i).createCell(0).setCellValue(value)
      }
      val size = nameplate.size
      val drawing = sheet.createDrawingPatriarch
      dataSet.zipWithIndex.foreach {
        case (c, i) =>
          val (instance, instanceGroup) = c
          val anchor = drawing.createAnchor(0, 0, 0, 0, 0, size + i * 25, 20, 30 + i * 25)
          val chart = drawing.createChart(anchor)
          chart.setTitleText(instance)
          generatePlot(chart, instanceGroup)
      }
      workbook.write(out)
      logger.info("===== generate excel report success! =====")
    } catch {
      case e: Exception =>
        logger.error(e.getMessage)
        throw new Exception(e)
    } finally {
      workbook.close()
    }
  }

  def generatePlot(chart: XSSFChart, data: Seq[(String, Map[String, BigDecimal])]) {

    // 创建图形注释的位置
    val legend = chart.getOrCreateLegend
    legend.setPosition(LegendPosition.TOP_RIGHT)

    // 创建绘图的类型 LineCahrtData 折线图
    val chartData = chart.getChartDataFactory.createLineChartData

    // 设置横坐标
    val bottomAxis = chart.getChartAxisFactory.createCategoryAxis(AxisPosition.BOTTOM)
    bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO)

    // 设置纵坐标
    val leftAxis = chart.getChartAxisFactory.createValueAxis(AxisPosition.LEFT)
    leftAxis.setCrosses(AxisCrosses.AUTO_ZERO)

    if (data.nonEmpty) {
      val t = data.head._2.keys.toArray
      val aa = data.map(_._1).toArray
      val d1 = DataSources.fromArray(aa)
      t.foreach {
        x =>
          val array: Array[BigDecimal] = data.map(_._2.getOrElse(x, BigDecimal(0))).toArray
          val d = DataSources.fromArray(array)
          val chartSeries = chartData.addSeries(d1, d)
          chartSeries.setTitle(x)
      }
    }
    chart.plot(chartData, bottomAxis, leftAxis)
  }

  def exportExcel(title: String, headers: JArrayList[String], body: JArrayList[String], dataset: Seq[Map[String, Any]], out: OutputStream): Unit = {

    //    val workbook: HSSFWorkbook = new HSSFWorkbook // HSSFWorkbook只适合数据量少的情况
    val workbook = new SXSSFWorkbook(1000) // 声明一个工作薄

    val sheet: Sheet = workbook.createSheet(title)  // 生成一个表格
    sheet.setDefaultColumnWidth(15.toShort) // 设置表格默认列宽度为15个字节

    val style: CellStyle = workbook.createCellStyle // 生成一个样式
    setDefaultStyle(workbook, style) // 设置默认的样式

    try {
      generateTitleRow(headers, sheet, style) // 生成标题行
      logger.info("===== generateTitleRow success! =====")
      generateDataRows(title, body, dataset, workbook, sheet) // 生成数据行
      logger.info("===== generateDataRows success! =====")
      workbook.write(out)
      logger.info("===== generate excel success! =====")
    } catch {
      case e: Exception =>
        logger.error(e.getMessage)
        throw new Exception(e)
    } finally {
      workbook.dispose()
      workbook.close()
    }

  }

  private def generateDataRows(title: String, body: JArrayList[String], dataset: Seq[Map[String, Any]], workbook: Workbook, sheet: Sheet) = {
    //遍历集合数据，产生数据行
    for (i <- 0 until dataset.length) {

      val row = sheet.createRow(i + 1)
      val data = dataset(i)
      val flag = title match {
        case "服务器" => 1
        case "告警中心" => 2
        case "用户" => 3
        case "审计日志" => 4
        case "阈值管理" => 5
        case _ => 0
      }
      for (j <- 0 until body.size()) {
        val cell = row.createCell(j)
        val fieldName = body.get(j)
        val value = data.get(fieldName).get
        var textValue = if (value != null) getTextValue(title, fieldName, value, flag) else ""
        val richString = new XSSFRichTextString(textValue)
        cell.setCellValue(richString)
      }
    }
  }

  private def generateTitleRow(headers: JArrayList[String], sheet: Sheet, style: CellStyle) = {
    //产生表格标题行
    val row = sheet.createRow(0)
    for (i <- 0 until headers.size()) {

      val cell = row.createCell(i)
      cell.setCellStyle(style)
      val text = new XSSFRichTextString(headers.get(i))
      cell.setCellValue(text)
    }
  }

  private def setDefaultStyle(workbook: Workbook, style: CellStyle) = {
    import org.apache.poi.ss.usermodel.{CellStyle, IndexedColors}
    // 设置样式
    //    style.setFillForegroundColor(HSSFColor.SKY_BLUE.index)
    style.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex)
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND)
    // 生成一个字体
    val font = workbook.createFont
    font.setColor(HSSFColorPredefined.WHITE.getIndex)
    font.setFontHeightInPoints(12.toShort)
    // 把字体应用到当前的样式
    style.setFont(font)
  }


  def getTextValue(title: String, fieldName: String, value: Any, flag: Int) = {
    var textValue: String = null
    if ("metricSetType".equals(fieldName)) {
      if("standard".equals(value)) {
        textValue = "标准"
      } else {
        textValue = "扩展"
      }
    } else if("templateType".equals(fieldName)) {
      if("standard".equals(value)) {
        textValue = "标准模板"
      } else if("normal".equals(value)) {
        textValue = "普通模板"
      } else {
        textValue = "临时模板"
      }
    } else if("roleCode".equals(fieldName)) {
      textValue = ""
    } else if("status".equals(fieldName) && flag == 1) { // 服务器状态
      textValue = ""
    } else if("status".equals(fieldName) && flag == 2) { // 告警状态
      val v = value.asInstanceOf[Int]
      if(v > 10) {
        textValue = "关闭"
      } else {
        textValue = "打开"
      }
    } else if("status".equals(fieldName) && flag == 3) { // 用户状态
      if(0.equals(value)) {
        textValue = "已启用"
      } else {
        textValue = "已禁用"
      }
    } else if("status".equals(fieldName)) {
      if(0.equals(value)) {
        textValue = "可用"
      } else {
        textValue = "不可用"
      }
    } else if("serverType".equals(fieldName)) {
      textValue = value match {
        case Some(s) => s.toString
        case None => ""
      }
    } else if("agentStatus".equals(fieldName)) { // 0：正常，1：超时，2：未注册，3：无状态
      if(0.equals(value)) {
        textValue = "正常"
      } else if(1.equals(value)) {
        textValue = "超时"
      } else if(2.equals(value)) {
        textValue = "未注册"
      } else if(3.equals(value)) {
        textValue = "无状态"
      }
    } else if("指标".equals(title) && "metricSetItemType".equals(fieldName)) {
      if("performance".equals(value)) {
        textValue = "性能类"
      } else if("availability".equals(value)) {
        textValue = "可用性类"
      } else if("capability".equals(value)) {
        textValue = "容量类"
      } else if("state".equals(value)) {
        textValue = "状态类"
      }
    } else if("dataType".equals(fieldName) || (flag == 5 && "metricSetItemType".equals(fieldName))) {
     ""
    } else if("effectiveMode".equals(fieldName)) {
      if("once".equals(value)) {
        textValue = "一次全部"
      } else if("oddeven".equals(value)) {
        textValue = "按奇偶分批次"
      } else if("batch".equals(value)) {
        textValue = "按数量分批次"
      }
    } else if("statusUpdated".equals(fieldName)) {
      textValue = DateFormat.formatTime(value.asInstanceOf[Option[Long]].get, DateFormat.yyyy_MM_dd_HH_mm_ss)
    } else if("lastHappened".equals(fieldName) || (flag == 4 && "updated".equals(fieldName))) {
      textValue = DateFormat.formatTime(value.asInstanceOf[String], DateFormat.yyyy_MM_dd_HH_mm_ss)
    } else if(List("created", "updated", "effectiveTime").contains(fieldName)) {
      textValue = DateFormat.formatTime(value.asInstanceOf[Long], DateFormat.yyyy_MM_dd_HH_mm_ss)
    } else {
      if (value != null) {
        if (value.isInstanceOf[Option[Any]]) {
          textValue = value.asInstanceOf[Option[Any]] match {
            case Some(s) => s.toString
            case None => ""
          }
        } else {
          textValue = value.toString
        }
      } else{
        textValue = ""
      }
    }
    textValue
  }
}



object DateFormat {
  val yyyyMMdd = "yyyy/MM/dd"
  val yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS"
  val yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"
  val yyyy_MM_ddTHH_mm_ss_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

  /** *
    * 格式化当前时间
    *
    * @return
    */
  def formatNowDate: String = {
    formatTime(System.currentTimeMillis, yyyyMMddHHmmssSSS)
  }

  /** *
    * 格式化时间
    *
    * @param time   时间
    * @param format 样式
    * @return
    */
  def formatTime(time: Long, format: String) = {
    formatDate(new Date(time), format)
  }

  /** *
    * 格式化日期
    *
    * @param date
    * @param format
    * @return
    */
  def formatDate(date: Date, format: String) = {
    new SimpleDateFormat(format).format(date)
  }

  /** *
    * 将UTC时区的字符串 => 日期类型 => 东八区指定日期格式的字符串
    *
    * @param time
    * @param format
    * @return
    */
  def formatTime(time: String, format: String) = {
    import java.util.TimeZone

    time match {
      case null => ""
      case t if t.trim.length == 0 => ""
      case _ =>
        val sdFormatter = new SimpleDateFormat(yyyy_MM_ddTHH_mm_ss_SSSZ)
        sdFormatter.setTimeZone(TimeZone.getTimeZone("UTC")) // 设置时区为UTC
      val date = sdFormatter.parse(time)
        formatDate(date, format)
    }
  }
}
object ExportExcel {

  def apply() = {
    new ExportExcel()
  }

}


trait ClientConf[C <: ClientConf[C]] {

  val configs: ConcurrentHashMap[String, Any]

  val logger = LoggerFactory.getLogger(this.getClass)

  val SERVICE_CONF = "application.conf"

  protected def getConfigs(key: String, confFile: String = SERVICE_CONF): ConcurrentHashMap[String, Any] = {
    val conf = new ConcurrentHashMap[String, Any]()
    val agentConfig = loadFromResource(key, confFile)
    for (entry <- agentConfig.entrySet().toArray) {
      val obj = entry.asInstanceOf[Entry[String, ConfigValue]]
      conf.put(obj.getKey, obj.getValue.unwrapped())
    }
    conf
  }

  def get[V](key: String): Option[V] = {
    configs.get(key) match {
      case x: V => Some(x)
      case _ => None
    }
  }

  private def loadFromResource(key: String, conf: String): Config = {
    val file = new File(".", conf)
    if (file.exists()) {
      loadConfig(file).getConfig(key)
    } else {
      ConfigFactory.load(conf).getConfig(key)
    }
  }

  private def loadConfig(file: File): Config = {
    ConfigFactory.parseFile(file)
  }
}

class ExcelConf extends ClientConf[ExcelConf] {

  val configs = getConfigs("exportExcel", ExcelConf.CONF_FILE)

  def equipmentTitle() = get[String]("equipmentTitle")

  def equipmentHeaders() = get[JArrayList[String]]("equipmentHeaders")

  def equipmentBody() = get[JArrayList[String]]("equipmentBody")

}

object ExcelConf {
  private val CONF_FILE = "excel.conf"


  val EXPORT_EQUIPMENT = "equipment"


  def apply() = new ExcelConf()


}




trait Conf {
  val conf = ExcelConf()
  def getTitle: String
  def getHeaders: JArrayList[String]
  def getBody: JArrayList[String]
  def getFileName: String
  def getSize: Int = 10000
  val suffix = ".xlsx"
}

object EquipmentExcelConf extends Conf {
  override def getTitle = conf.equipmentTitle().get
  override def getHeaders = conf.equipmentHeaders().get
  override def getBody = conf.equipmentBody().get
  override def getFileName = generateFileName("equipment", suffix)

  def generateFileName(name: String, suffix: String) = {
    name + "-" + DateFormat.formatNowDate + suffix
  }
}

