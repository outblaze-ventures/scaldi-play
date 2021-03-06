package scaldi.play

import org.scalatest.{WordSpec, Matchers}
import play.api.cache.CacheApi
import play.api.{Application, Play, GlobalSettings}
import scaldi.{Injector, Injectable, Module}
import ScaldiApplicationBuilder._

object ScaldiSupportTest {
  object DummySevice {
    var instanceCount: Int = 0
    var stopCount: Int = 0
  }

  class DummyService(implicit inj: Injector) extends Injectable {
    val cache = inject [CacheApi]

    import DummySevice._

    instanceCount += 1

    var stopped: Boolean = false

    def hi: String = {
      if (!stopped) {
        "hello"
      } else {
        "stopped"
      }
    }

    def stop() {
      stopCount += 1
      stopped = true
    }
  }

  object Global extends GlobalSettings with ScaldiSupport with Matchers with Injectable {
    var startCount: Int = 0

    override def applicationModule = new Module {
      binding toNonLazy new DummyService destroyWith(_.stop())
    }

    override def onStart(app: Application): Unit = {
      super.onStart(app)

      startCount += 1
    }
  }
}

class ScaldiSupportTest extends WordSpec with Matchers {
  import ScaldiSupportTest._

  "ScaldiSupport" should {
    "reinit with Global object" in {
      Global.startCount should equal(0)
      DummySevice.instanceCount should equal(0)
      DummySevice.stopCount should equal(0)

      withClue("first run") {
        withScaldiApp(global = Some(Global)) {
          Global.startCount should equal(1)
          DummySevice.instanceCount should equal(1)
          DummySevice.stopCount should equal(0)
        }

        DummySevice.stopCount should equal(1)
      }

      withClue("second run") {
        withScaldiApp(global = Some(Global)) {
          Global.startCount should equal(2)
          DummySevice.instanceCount should equal(2)
          DummySevice.stopCount should equal(1)
        }

        DummySevice.stopCount should equal(2)
      }
    }
  }
}
