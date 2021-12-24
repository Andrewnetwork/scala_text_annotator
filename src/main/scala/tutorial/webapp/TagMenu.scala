package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js

class TagMenu(gState: GlobalState) {
    val state = gState
    val menuDiv = document.createElement("div")
    var isVisible = false
    val menuContextMargin = 40
    var menuHeight = 0.0
    var selectingUp = true
    var tags = List[(String,String, dom.Element)]()

    val loc: (Double, Double) = if (state.mouse_loc._2 > state.pointer_down_loc._2) state.mouse_loc else state.pointer_down_loc
    menuDiv.setAttribute("id", "menu")
    menuDiv.setAttribute("style", "position: absolute; left: "+(dom.window.innerWidth/2 - 100)+
        "px;top:"+(loc._2+40)+"px;")
    updateSize()

    val tagApply: Function1[dom.Event, Unit] = (e0: dom.Event) => {
        val selection = dom.window.getSelection()
        val anchorNode = selection.anchorNode
        val anchorNodeParent = anchorNode.parentNode.asInstanceOf[dom.Element]
        val text = anchorNodeParent.innerHTML
        // Needed to get the proper offset value. 
        var child = anchorNode.previousSibling
        var lengthBeforeSelection = 0
        while(anchorNode.firstChild != child && !js.isUndefined(child)){
            if(child.toString() == "[object Text]"){
                lengthBeforeSelection += child.asInstanceOf[dom.Text].length
            }else{
                lengthBeforeSelection += child.asInstanceOf[dom.Element].outerHTML.length()
            } 
            child = child.previousSibling
        }
        // Splitting the HTML and inserting the tag.
        val fstSpit = if(selectingUp) selection.anchorOffset+lengthBeforeSelection else
            selection.anchorOffset+lengthBeforeSelection-state.currentSelection.length()
        val (fst, snd) = text.splitAt(fstSpit)
        val (trd, fth) = snd.splitAt(state.currentSelection.length())
        anchorNodeParent.innerHTML = fst+"<b>"+trd+"</b>"+fth
    }
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
            selectingUp = true
            state.mouse_loc._2+dom.window.pageYOffset+menuContextMargin
        }else{
            selectingUp = false
            state.mouse_loc._2+dom.window.pageYOffset-(80+menuContextMargin)
        }

        menuDiv.setAttribute("style", "position: absolute; left: "+(dom.window.innerWidth/2 - 100)+
            "px;top:"+yPos+"px;");
    }
    def pushBack(): Unit = {
        menuDiv.classList.add("menuBelow")
        menuDiv.classList.remove("menuAbove")
    }
    def bringForward(): Unit = {
        menuDiv.classList.add("menuAbove")
        menuDiv.classList.remove("menuBelow")
    }
    def addTag(name: String, color: String): Unit = {
        val tagDiv = document.createElement("div")
        
        val checkBox = document.createElement("input")
        checkBox.setAttribute("type", "checkbox")
        checkBox.addEventListener("click", tagApply)

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
    }
}
