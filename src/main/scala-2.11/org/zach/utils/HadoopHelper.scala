package org.zach.utils

import java.io.IOException

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

object HadoopHelper {
  private val conf = new Configuration()
  conf.addResource(new Path("core-site.xml"));
  conf.addResource(new Path("hdfs-site.xml"));


  def copyFromLocalFile(conf: Configuration, src: Path, dst: Path): Unit = {
    val fs = FileSystem.get(conf);

    try {
      fs.copyFromLocalFile(src, dst);
    } catch {
      case e: IOException =>
        e.printStackTrace()
    } finally {
      fs.close()
    }
  }


  def copyToLocalFile(conf: Configuration, src: Path, dst: Path): Unit = {
    val fs = FileSystem.get(conf);

    try {
      fs.copyToLocalFile(src, dst);
    } catch {
      case e: IOException =>
        e.printStackTrace()
    } finally {
      fs.close()
    }
  }

}
