package objektwerks

import dev.fritz2.core.render

fun main() {
  render("#target") {
      h1 { +"App" }
      div("some-fix-css-class") {
          p(id = "someId") {
              +"Hello, Fritz2!"
          }
      }
  }
}