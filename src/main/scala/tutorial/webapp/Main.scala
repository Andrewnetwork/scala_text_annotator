package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js.timers.setInterval

object Main {
    var state = new GlobalState()
    var selectionMenu = new TagMenu(state)
    selectionMenu.addTag("Name", "rgba(255,0,4,0.3)")
    selectionMenu.addTag("Place", "rgba(0,21,255,0.3)")
    selectionMenu.addTag("Clothing", "rgba(255,247,0,0.3)")

    // If the user has selected new text, return the updated selection. Otherwise, return nothing. 
    val pollingFn: GlobalState => Option[String] = (state: GlobalState) => {
        val updatedSelection = dom.window.getSelection().toString()
        if (updatedSelection != state.currentSelection){
            state.currentSelection = dom.window.getSelection().toString()
            Some(updatedSelection)
        } else None
    }
    // This function is called when pollingFn returns something. 
    val eventListener: String => Unit = (selection: String) => {
        if(selection.length()>0 && !selectionMenu.isVisible){
            selectionMenu.displayMenu()
            selectionMenu.pushBack()
        } 
        if(selection.length()==0 && selectionMenu.isVisible){
            selectionMenu.hideMenu()
        }
        selectionMenu.updateMenuLoc()
    }
    // Call pollingFn on an interval. 
    val pollingEvent = new PollingEvent(state, pollingFn, eventListener)

    document.addEventListener("mousemove", {(e: dom.MouseEvent) =>
        state.mouse_loc = (e.clientX, e.clientY)
    })
    document.addEventListener("pointerdown", {(e: dom.MouseEvent) =>
        state.pointer_down_loc = (e.pageX, e.pageY)
    })
    document.addEventListener("pointerup", {(e: dom.MouseEvent) =>
        selectionMenu.bringForward()
    })

    def main(args: Array[String]): Unit = {}
}

