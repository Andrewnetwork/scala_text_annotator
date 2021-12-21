package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js.timers.setInterval

object TutorialApp {
    var pointer_down_loc: (Double, Double) = (0,0)
    var menu : Option[dom.Element] = None
    var mouse_loc: (Double, Double) = (0,0)
    var isSelecting = false
    var currentSelection = "" 

    def onSelectionChange(eventListener: String => Unit){
        setInterval(200) {
            val updatedSelection = dom.window.getSelection().toString()
            if (updatedSelection != currentSelection){
                eventListener(updatedSelection)
                currentSelection = dom.window.getSelection().toString()
            }
        }
    }

    def createMenu(): Unit = {
        val menuDiv = document.createElement("div")
        val loc: (Double, Double) = if (mouse_loc._2 > pointer_down_loc._2) mouse_loc else pointer_down_loc
        menuDiv.innerHTML = "MENU"
        menuDiv.setAttribute("style", "background-color: red; height:200px;" +
          "width:200px; position: absolute; left: "+(dom.window.innerWidth/2 - 100)+"px;top:"+(loc._2+40)+"px;")

        menu = Some(menuDiv)
        document.body.appendChild(menu.get)
    }
    def deleteMenu(): Unit = {
        menu match {
            case Some(x) => { 
                document.body.removeChild(menu.get)
                menu = None
            }
            case _ => ()
        }
    }
    def updateMenuLoc(): Unit = {
        menu match{
            case Some(menu_inner) => menu_inner.setAttribute("style", "background-color: red; height:200px;" +
                    "width:200px; position: absolute; left: "+(dom.window.innerWidth/2 - 100)+"px;top:"+
                        (mouse_loc._2+40+dom.window.pageYOffset)+"px;");
            case None => ()
        }
    }
   
    def main(args: Array[String]): Unit = {
        document.addEventListener("mousemove", {(e: dom.MouseEvent) => 
            mouse_loc = (e.clientX, e.clientY)
            if (isSelecting){
                updateMenuLoc
            }
        })
        document.addEventListener("pointerdown", {(e: dom.MouseEvent) =>
            deleteMenu()
        })
        document.addEventListener("pointerup", {(e: dom.MouseEvent) =>
            isSelecting = false
        })
        onSelectionChange({(selection: String) => 
            if(selection.length()>0 && menu == None){
                createMenu()
                isSelecting = true
            } 
            if(selection.length()==0 && menu != None){
                deleteMenu()
            }
        })
    }
}

