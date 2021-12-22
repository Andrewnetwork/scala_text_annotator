package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js.timers.setInterval

object Main {
    var state = new GlobalState()
    var selectionMenu = new TagMenu(state)
    selectionMenu.addTag("Name", "#348ceb")

    val pollingFn = (state: GlobalState) => {
        val updatedSelection = dom.window.getSelection().toString()
        if (updatedSelection != state.currentSelection){
            state.currentSelection = dom.window.getSelection().toString()
            Some(updatedSelection)
        } else None
    }
    val eventListener = (selection: String) => {
        if(selection.length()>0 && !selectionMenu.isVisible){
            selectionMenu.displayMenu()
        } 
        if(selection.length()==0 && selectionMenu.isVisible){
            selectionMenu.hideMenu()
        }
        selectionMenu.updateMenuLoc()
    }
    val pollingEvent = new PollingEvent(state, pollingFn, eventListener)

    document.addEventListener("mousemove", {(e: dom.MouseEvent) =>
        state.mouse_loc = (e.clientX, e.clientY)
    })
    document.addEventListener("pointerdown", {(e: dom.MouseEvent) =>
        state.pointer_down_loc = (e.pageX, e.pageY)
    })

    def main(args: Array[String]): Unit = {}
}

