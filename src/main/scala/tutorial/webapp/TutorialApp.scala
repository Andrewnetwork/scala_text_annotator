package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document

object TutorialApp {
    var pointer_down_loc: (Double, Double) = (0,0)
    var menu : Option[dom.Element] = None
    var mouse_loc: (Double, Double) = (0,0)

    def createMenu(pointer_up_loc: (Double, Double)): dom.Element = {
        val menuDiv = document.createElement("div")
        val loc: (Double, Double) = if (pointer_up_loc._2 > pointer_down_loc._2) pointer_up_loc else pointer_down_loc
        menuDiv.setAttribute("style", "background-color: red; height:200px;" +
          "width:200px; position: absolute; left: "+(dom.window.innerWidth/2 - 100)+"px;top:"+(loc._2+40)+"px;")

        menuDiv
    }
    def updateMenuLoc(): Unit = {
        menu match{
            case Some(menu_inner) => menu_inner.setAttribute("style", "background-color: red; height:200px;" +
                    "width:200px; position: absolute; left: "+(dom.window.innerWidth/2 - 100)+"px;top:"+(mouse_loc._2+40)+"px;")
            case None => ()
        }
    }

    def main(args: Array[String]): Unit = {
        document.addEventListener("mousemove", {(e: dom.MouseEvent) => 
            mouse_loc = (e.clientX, e.clientY)
            updateMenuLoc
        })
        document.addEventListener("selectstart", {(x: dom.Event) =>
            menu = Some(createMenu(mouse_loc))
            document.body.appendChild(menu.get)
        })
        // document.addEventListener("selectionchange", {() =>
        //     menu = Some(createMenu((e.clientX, e.clientY)))
        //     document.body.appendChild(menu.get)
        // })
        // document.addEventListener("pointerup", {(e: dom.MouseEvent) => 
        //     ()
        // })
       

        // document.addEventListener("pointerdown", {(e: dom.MouseEvent) => 
        //     pointer_down_loc = (e.clientX, e.clientY)
        //     if (document.getSelection().toString().length() != 0){
        //         menu = Some(createMenu((e.clientX, e.clientY)))
        //         document.body.appendChild(menu.get)
        //     }
        //     // menu match{
        //     //     case Some(menu_inner) => {document.body.removeChild(menu_inner); menu = None}
        //     //     case None => ()
        //     // }
        // })
    }
}