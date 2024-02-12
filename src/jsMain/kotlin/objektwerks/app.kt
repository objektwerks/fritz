package objektwerks

import dev.fritz2.core.render

fun main() {
  render("#target") {
      h1 { +"App" }
      div {
          p {
              +"Hello, Fritz2!"
          }
      }
  }
}