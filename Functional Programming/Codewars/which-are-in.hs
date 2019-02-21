-- Found at https://www.codewars.com/kata/which-are-in/train/haskell

-- Instructions:
-- Given two arrays of strings a1 and a2 return a sorted array r in lexicographical order of the strings of a1 which are substrings of strings of a2.
-- #Example 1: a1 = ["arp", "live", "strong"]
-- a2 = ["lively", "alive", "harp", "sharp", "armstrong"]
-- returns ["arp", "live", "strong"]
-- #Example 2: a1 = ["tarp", "mice", "bull"]
-- a2 = ["lively", "alive", "harp", "sharp", "armstrong"]
-- returns []
-- Notes:
-- Arrays are written in "general" notation. See "Your Test Cases" for examples in your language.
-- In Shell bash a1 and a2 are strings. The return is a string where words are separated by commas.
-- Beware: r must be without duplicates.
-- Don't mutate the inputs.

module Codewars.Kata.Which where

import Data.List as List

removeDuplicates :: Eq a => [a] -> [a]
removeDuplicates [] = []
removeDuplicates (x:xs)
  | x `elem` xs = removeDuplicates xs
  | otherwise   = x:removeDuplicates xs

isSubStringOfAny :: String -> [String] -> Bool
isSubStringOfAny subject strings = any (\s -> subject `List.isInfixOf` s) strings

inArray :: [String] -> [String] -> [String]
inArray a1 a2 = List.sort $ removeDuplicates $ filter (\s -> s `isSubStringOfAny` a2) a1