package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 1/24/15.
 */
class StringExt(string: String) {

  def front: String = {
    if (string.isEmpty) ""
    else string.substring(0, string.length - 1)
  }

  def isPalindrome: Boolean = {
    val chars = string.toCharArray
    var frontIndex = 0
    var lastIndex = chars.indices.last
    while (frontIndex < lastIndex && chars(frontIndex) == chars(lastIndex)){
      frontIndex += 1
      lastIndex -= 1
    }
    lastIndex <= frontIndex
  }
}
