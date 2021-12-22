package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document

class SelectionMenu(gState: GlobalState) {
    val state = gState
    val menuDiv = document.createElement("div")
    var isVisible = false

    val loc: (Double, Double) = if (state.mouse_loc._2 > state.pointer_down_loc._2) state.mouse_loc else state.pointer_down_loc
    menuDiv.innerHTML = "MENU"
    menuDiv.setAttribute("style", "background-color: red; height:200px;" +
        "width:200px; position: absolute; left: "+(dom.window.innerWidth/2 - 100)+"px;top:"+(loc._2+40)+"px;")

    def hideMenu(): Unit = {
        document.body.removeChild(menuDiv)
        isVisible = false
    }
    def displayMenu(): Unit = {
        document.body.appendChild(menuDiv)
        isVisible = true
    }
    def updateMenuLoc(): Unit = {
        val yPos = if (state.mouse_loc._2 > state.pointer_down_loc._2){
            (state.mouse_loc._2+40)
        }else (state.mouse_loc._2-240)

        menuDiv.setAttribute("style", "background-color: red; height:200px;" +
            "width:200px; position: absolute; left: "+(dom.window.innerWidth/2 - 100)+"px;top:"+
                yPos+"px;");
    }
}
