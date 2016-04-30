package com.sjr.entropy.core

import com.sjr.entropy.core.game.EntropyBoard


/**
 * Created by stevenrichardson on 1/23/15.
 */

class EntropyScorer {

  def score(board: EntropyBoard): Int = board.allPatterns.map(scoreCombination).sum

  def scoreCombination(pattern: String): Int = {
    if (pattern.trim.isEmpty) 0
    else if (pattern.length == 2) scoreSinglePattern(pattern)
    else scoreSinglePattern(pattern) + scoreCombination(pattern.tail) + scoreLeading(pattern.front)
  }

  def scoreLeading(pattern: String): Int = {
    if (pattern.length < 2) 0
    else if (pattern.length == 2) scoreSinglePattern(pattern)
    else scoreSinglePattern(pattern) + scoreLeading(pattern.front)
  }

  def scoreSinglePattern(pattern: String): Int =
    if (pattern.contains(" ")) 0 //when scoring incomplete boards we don't want patterns with gaps to score
    else if (pattern.length > 1 && pattern.isPalindrome) pattern.length else 0
}