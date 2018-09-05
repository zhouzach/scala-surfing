package org.zach

package object utils {

  implicit class StringHelper(str: String) {
    def removeLast() = {
      if (str == null || "".equals(str)) str
      else str.substring(0, str.length() - 1)
    }
  }

}
