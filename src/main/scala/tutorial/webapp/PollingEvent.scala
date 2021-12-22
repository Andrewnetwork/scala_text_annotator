package tutorial.webapp
import scala.scalajs.js.timers.setInterval

class PollingEvent[EventArg, State](state: State, poll: State => Option[EventArg], eventListener: EventArg => Unit, pollDelta: Int = 100){
    setInterval(pollDelta) {
        poll(state) match{
            case Some(x) => eventListener(x)
            case None => ()
        }
    }
}
