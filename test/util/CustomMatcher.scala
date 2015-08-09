package util

import org.specs2.matcher.{Matcher, Expectable}

object CustomMatchers {
  def containAllSubstringsIn(expectedSubstrings: Seq[String]) = new Matcher[String] {
    def apply[S <: String](expectable: Expectable[S]) = {
      result(expectedSubstrings.forall(expectable.value.contains(_)),
        expectable.description + " contains all substrings in " + expectedSubstrings,
        expectable.description + " doesn't contain all substrings in " + expectedSubstrings + "\nMissing substrings: " + expectedSubstrings.filterNot(expectable.value.contains(_)), expectable)
    }
  }
}
