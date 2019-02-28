package org.rabbit.utils

import java.io.IOException
import java.nio.file.{Files, Path}

object FileHelper {

  def delete(path: Path) = {

    try {

      Files.deleteIfExists(path)
    } catch {
      case ioException: IOException =>
        println("Error caught in deleting directory")
        ioException.printStackTrace()
        false
    }
  }

}
