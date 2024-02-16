package objektwerks

import dev.fritz2.core.render

fun main() {
  render("#content") {
      div {
          p {
              +"Hello, Pool Balance!"
          }
      }
  }
}