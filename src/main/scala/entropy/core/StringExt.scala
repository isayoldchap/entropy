package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 1/24/15.
 */
class StringExt(string: String) {

  def front: String = {
    if (string.isEmpty) ""
    else string.substring(0, string.length - 1)
  }

  def isPalindrome: Boolean = string.reverse == string // optimize later

}
