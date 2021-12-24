package tutorial.webapp
import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js
import dom.window.console

class TagMenu(gState: GlobalState) {
    val state = gState
    val menuDiv = document.createElement("div")
    var isVisible = false
    val menuContextMargin = 40
    var menuHeight = 0.0
    var selectingUp = true
    var tags = List[(String,String, dom.Element, dom.html.Input)]()

    val loc: (Double, Double) = if (state.mouse_loc._2 > state.pointer_down_loc._2) state.mouse_loc else state.pointer_down_loc
    menuDiv.setAttribute("id", "menu")
    menuDiv.setAttribute("style", "position: absolute; left: "+(dom.window.innerWidth/2 - 100)+
        "px;top:"+(loc._2+40)+"px;")
    updateSize()

    private def extractNodeValue(node: dom.Node): String = {
        if(node.toString() == "[object Text]") node.asInstanceOf[dom.Text].data else
            node.asInstanceOf[dom.Element].outerHTML
    }
    def makeTagCallback(color: String): Function1[dom.Event, Unit] = {
        val tagBegin = "<span style='background-color:"+color+"'>"
        val tagEnd = "</span>"
        (e0: dom.Event) => {
            val selection = dom.window.getSelection()
            val anchorNode = selection.anchorNode
            val focusNode = selection.focusNode
            val focusOffset = selection.focusOffset
            val anchorOffset = selection.anchorOffset
            val anchorNodeParent = anchorNode.parentNode.asInstanceOf[dom.Element]
            val text = anchorNodeParent.innerHTML
            
            var currentNode = anchorNodeParent.firstChild
            var newHTML = ""
            var beginningTagPlaced = false
            var endTagPlaced = false
            var changeList = List[(dom.Element, String)]()
            var afterFirst = false
            while(currentNode != null){
                if(afterFirst){
                    newHTML += tagBegin
                }
                if(currentNode == anchorNode){
                    val (before, after) = extractNodeValue(currentNode).splitAt(anchorOffset)
                    if(!beginningTagPlaced){
                        if(anchorNode==focusNode){
                            if(anchorOffset <= focusOffset){
                                val (selectionText, afterSel) = after.splitAt(selection.toString().length())
                                newHTML += before+tagBegin+selectionText+tagEnd+afterSel
                                beginningTagPlaced = true
                                endTagPlaced = true
                            }else{
                                //Selecting from left to right. 
                                val (beforeSel, selectionText) = before.splitAt(before.length()-selection.toString().length())
                             
                                newHTML += beforeSel+tagBegin+selectionText+tagEnd+after
                                beginningTagPlaced = true
                                endTagPlaced = true
                            }
                        }else{
                            // The anchor node is not the focus node. Thus, we only place the beginning tag.
                            newHTML += before+tagBegin+after
                            beginningTagPlaced = true
                        }
                    }else{
                        if(anchorNode==focusNode){
                            val (selectionText, afterSel) = before.splitAt(selection.toString().length())
                            newHTML += before+tagBegin+selectionText+tagEnd+afterSel
                            endTagPlaced = true
                            beginningTagPlaced = true
                        }else{
                            newHTML += before+tagEnd+after
                            endTagPlaced = true
                        }
                    }
                }else if(currentNode == focusNode){
                    
                    if(beginningTagPlaced){
                        val (inMark, after) = extractNodeValue(currentNode).splitAt(focusOffset)
                        newHTML += inMark+tagEnd+after 
                        endTagPlaced = true
                    }else{
                        // Focus node reached before anchor node. This means we are selecting left to right. 
                        val (before, inMark) = extractNodeValue(currentNode).splitAt(focusOffset)
                        newHTML += before+tagBegin+inMark 
                        beginningTagPlaced = true
                    }
                }else newHTML += extractNodeValue(currentNode)

                // Next node logic.
                if(currentNode.nextSibling == null){
                    changeList = (currentNode.parentNode.asInstanceOf[dom.Element], 
                        (newHTML + (if (!endTagPlaced) tagEnd else ""))) :: changeList
                    if(!endTagPlaced || !beginningTagPlaced){
                        val nextNode = currentNode.parentNode.asInstanceOf[dom.Element].nextElementSibling.firstChild
                        currentNode = nextNode
                        afterFirst = true
                        newHTML = ""
                    }else currentNode = null
                }else{
                    currentNode = currentNode.nextSibling
                }
            }
            // Apply updates. 
            dom.window.getSelection().removeAllRanges()
            for((node: dom.Element, string: String) <- changeList){
                node.innerHTML = string
                console.log(string)
            }
            
    }}
    // var innerHTMLBefore = ""

            // var currentNode = anchorNode
            // while(currentNode != focusNode){
                
            //     innerHTMLBefore += extractNodeHTML(currentNode)
            //     console.log(innerHTMLBefore)
            //     currentNode = if(currentNode.nextSibling == null){
            //         // We have reached a leaf on the dom tree. 
            //         // We must go up and then down into the next branch.
            //         //currentNode.parentNode.asInstanceOf[dom.Element].innerHTML = currentNode.parentNode.asInstanceOf[dom.Element].innerHTML + "Hello"
                    
            //         currentNode.parentNode.asInstanceOf[dom.Element].nextElementSibling.firstChild
            //     }else currentNode.nextSibling
            // }
            // console.log(extractNodeHTML(focusNode).splitAt(selection.focusOffset))
            // Needed to get the proper offset value. 
            // var child = anchorNode.previousSibling
            // var lengthBeforeSelection = 0
            // dom.console.log(selection.asInstanceOf[dom.Element].outerHTML.length())
            // while(anchorNode.firstChild != child && !js.isUndefined(child)){
            //     if(child.toString() == "[object Text]"){
            //         lengthBeforeSelection += child.asInstanceOf[dom.Text].length
            //     }else{
            //         lengthBeforeSelection += child.asInstanceOf[dom.Element].outerHTML.length()
            //     } 
            //     child = child.previousSibling
            // }
            // // Splitting the HTML and inserting the tag.
            // val fstSpit = if(selection.anchorOffset <= selection.focusOffset) selection.anchorOffset+lengthBeforeSelection else 
            //         selection.anchorOffset+lengthBeforeSelection-state.currentSelection.length()
            // val (fst, snd) = text.splitAt(fstSpit)
            // val (trd, fth) = snd.splitAt(state.currentSelection.length())
            // anchorNodeParent.innerHTML = fst+"<span style='background-color:"+color+"'>"+trd+"</span>"+fth
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
    
        tags.foreach( (tag) =>{
            tag._4.checked = false
        })
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
        
        val checkBox = document.createElement("input").asInstanceOf[dom.html.Input]
        checkBox.setAttribute("type", "checkbox")
        checkBox.addEventListener("click", makeTagCallback(color))

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
        tags = (name,color,tagDiv,checkBox) :: tags
        updateSize()
    }
}
