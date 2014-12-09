CS349 A2 ScreenSaver
By Justin Hu/20465661/j62hu

The program starts with a default screen saver displayed.

When the mouse leaves the window, screen saver mode is activated and animation starts.
When the mouse enters the window, editor mode is activated where user is able to manipulate nodes on the screen.

In editor mode, user can simply drag any node to reposition it. This will aslo move its children. While moving, each node will maintain its orientation towards its parent.

User can also select a specific node by clicking on it. If a node is selected, it will be highlighted, and a list of buttons will appear at the top of the screen which allow user to change attributes of the selected node. The buttons, from left to right, are:

"Size Increase"|"Size Decrease"|"Rotate Left"|"Rotate Right"|"Spin Slower"|"Spin Faster"|"Reverse Direction"|"Add Child"|"Star"|"Heart"|"Rectangle"|"Circle"|"Red"|"Orange"|"Yellow"|"Green"|"Blue"

Their names will appear when the mouse is over the corresponding button.

The functionality of these buttons are:
	"Size Increase": Increase the size of the selected node
	"Size Decrease": Decrease the size of the selected node
	"Rotate Left": Rotate this selected node counterclockwise (also rotates its children)
	"Rotate Right": Rotate this selected node clockwise (also rotates its children)
	"Spin Slower": Decrease the spinning speed of the selected node around its parent 
	"Spin Faster" : Increase the spinning speed of the selected node around its parent 
	"Reverse Direction": Make the  selected node around its parent in opposite direction
	"Add Child": Add a node as a child of the selected node
	"Star": Change the shape of the selected node to star(as defined in Main.java)
	"Heart": Change the shape of the selected node to heart(as defined in Main.java)
	"Rectangle": Change the shape of the selected node to rectangle(as defined in Main.java)
	"Circle": Change the shape of the selected node to circle(a new shape)
	"Red" :Change the color of the selected node to red
	"Orange" :Change the color of the selected node to red
	"Yellow":Change the color of the selected node to yellow
	"Green":Change the color of the selected node to green
	"Blue": Change the color of the selected node to blue
        