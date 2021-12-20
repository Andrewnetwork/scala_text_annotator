package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document

object TutorialApp {
    var pointer_down_loc: (Double, Double) = (0,0)

    def appendPar(targetNode: dom.Node, text: String): Unit = {
        val parNode = document.createElement("p")
        parNode.textContent = text
        targetNode.appendChild(parNode)
    }
    def createMenu(pointer_up_loc: (Double, Double)): dom.Element = {
        val menuDiv = document.createElement("div")
        val loc: (Double, Double) = if (pointer_up_loc._2 > pointer_down_loc._2) pointer_up_loc else pointer_down_loc
        menuDiv.setAttribute("style", "background-color: red; height:200px;" +
          "width:200px; position: absolute; left: "+(dom.window.innerWidth/2 - 100)+"px;top:"+(loc._2+40)+"px;")

        menuDiv
    }
    def main(args: Array[String]): Unit = {
        //appendPar(document.body, "Hello World!!")
        document.addEventListener("pointerup", {(e: dom.MouseEvent) => 
            dom.console.log(document.getSelection().toString())
            document.body.appendChild(createMenu((e.clientX, e.clientY)))
        })
        document.addEventListener("pointerdown", {(e: dom.MouseEvent) => 
            pointer_down_loc = (e.clientX, e.clientY)
        })
    }
}