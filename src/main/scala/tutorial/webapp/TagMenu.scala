package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document

class TagMenu(gState: GlobalState) {
    val state = gState
    val menuDiv = document.createElement("div")
    var isVisible = false
    val menuContextMargin = 40
    var menuHeight = 0.0
    var tags = List[(String,String, dom.Element)]()

    val loc: (Double, Double) = if (state.mouse_loc._2 > state.pointer_down_loc._2) state.mouse_loc else state.pointer_down_loc
    menuDiv.setAttribute("id", "menu")
    menuDiv.setAttribute("style", "position: absolute; left: "+(dom.window.innerWidth/2 - 100)+
        "px;top:"+(loc._2+40)+"px;")
    updateSize()

    def hideMenu(): Unit = {
        document.body.removeChild(menuDiv)
        isVisible = false
    }
    def updateSize(): Unit = {
        //TODO
    }
    def displayMenu(): Unit = {
        document.body.appendChild(menuDiv)
        isVisible = true
    }
    def updateMenuLoc(): Unit = {
        // Determines if the user is selecting up or down and returns a y-coordinate accordingly.
        val yPos = if (state.mouse_loc._2+dom.window.pageYOffset > state.pointer_down_loc._2){
            (state.mouse_loc._2+dom.window.pageYOffset+menuContextMargin)
        }else (state.mouse_loc._2+dom.window.pageYOffset-(80+menuContextMargin))

        menuDiv.setAttribute("style", "position: absolute; left: "+(dom.window.innerWidth/2 - 100)+"px;top:"+
            yPos+"px;");
    }
    def addTag(name: String, color: String): Unit = {
        val tagDiv = document.createElement("div")
        
        val checkBox = document.createElement("input")
        checkBox.setAttribute("type", "checkbox")
        val checkBoxDiv = document.createElement("div")
        checkBoxDiv.setAttribute("class", "tagCheckBox")
        checkBoxDiv.setAttribute("style", "background-color:"+color)
        checkBoxDiv.appendChild(checkBox)

        val nameDiv = document.createElement("div")
        nameDiv.setAttribute("class", "tagNameDiv")
        nameDiv.innerHTML = name

        tagDiv.appendChild(checkBoxDiv)
        tagDiv.appendChild(nameDiv)
        menuDiv.appendChild(tagDiv)
        tags :+ (name,color,tagDiv)
        updateSize()
        // <div>
        //     <div style="background-color:blue; padding:5px;">
        //         <input type="checkbox" id="vehicle1" name="vehicle1" value="Bike">
        //     </div>
        //     <div style="line-height:30px; margin-left:5px;">
        //         Name
        //     </div>
        // </div>
    }
}
