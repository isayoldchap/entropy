package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 1/24/15.
 */
class StringExt(string: String) {

  def front: String = string.dropRight(1)

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
