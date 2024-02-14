package objektwerks

import dev.fritz2.core.render

fun main() {
  render("#content") {
      h1 { +"Fritz" }
      br {}
      div {
          p {
              +"Hello, Fritz2!"
          }
      }
  }
}