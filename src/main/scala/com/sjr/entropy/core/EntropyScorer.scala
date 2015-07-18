package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 1/23/15.
 */

class EntropyScorer {

  def score(board: EntropyBoard): Int =
    board.allPatterns.foldLeft(0)((score, pattern) => score + scoreCombination(pattern))

  def scoreCombination(pattern: String): Int =
    if (pattern.isEmpty) 0
    else scoreSinglePattern(pattern) + scoreCombination(pattern.tail) + scoreLeading(pattern.front)

  def scoreLeading(pattern: String): Int =
    if (pattern.length < 2) 0
    else scoreSinglePattern(pattern) + scoreLeading(pattern.front)

  def scoreSinglePattern(pattern: String): Int =
    if (pattern.contains(" ")) 0 //when scoring incomplete boards we don't want patterns with gaps to score
    else if (pattern.length > 1 && pattern.isPalindrome) pattern.length else 0
}