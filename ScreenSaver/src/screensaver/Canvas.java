package screensaver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

/**
* The canvas where the screen saver is drawn.
*/
public class Canvas extends JComponent implements ActionListener {
    
    
    // A list of nodes.  Only one is used in the sample, but there's
    // no reason there couldn't be more.
    private ArrayList<SSNode> nodes = new ArrayList<SSNode>();
    private static final int FPS = 20;	// How often we update the animation.
    private final Timer timer;			// The timer to actually cause the animation updates.
    private SSNode clickedNode = null;		// Which node is selected; null if none
    private SSNode selectedNode = null;
    private Icon selectedTool = null;
    private boolean toolbar = false;
    private ArrayList<Icon> tools = new ArrayList<Icon>();;
    //ActionListener updateProBar;
    
    public Canvas() {
        timer = new Timer(1000/FPS, this);
        timer.setInitialDelay(150);
        
        /*
        * The mouse input listener combines MouseListener and MouseMotionListener.
        * Still need to add it both ways, though.
        */
        MouseInputListener mil = new MouseHandler();
        this.addMouseListener(mil);
        this.addMouseMotionListener(mil);
        this.setOpaque(true);	// we paint every pixel; Java can optimize
        
        tools.add(new Icon ("Size Increase",0));
        tools.add(new Icon ("Size Decrease",1));
        tools.add(new Icon ("Rotate Left",2));
        tools.add(new Icon ("Rotate Right",3));
        tools.add(new Icon ("Spin Slower",4));
        tools.add(new Icon ("Spin Faster",5));
        tools.add(new Icon ("Reverse Direction",6));
        tools.add(new Icon ("Add Child",8));
        tools.add(new Icon ("Star",10));
        tools.add(new Icon ("Heart",11));
        tools.add(new Icon ("Rectangle",12));
        tools.add(new Icon ("Circle",13));
        tools.add(new Icon ("Red",15));
        tools.add(new Icon ("Orange",16));
        tools.add(new Icon ("Yellow",17));
        tools.add(new Icon ("Green",18));
        tools.add(new Icon ("Blue",19));
        
        //Size Increase.
    }
    
    /**
     * Paint this component: fill in the background and then draw the nodes
     * recursively.
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (SSNode n : nodes) {
            n.paintNode((Graphics2D) g);
        }
        if(toolbar){
            for(Icon i:tools){
                i.paintIcon(g2);
            }
        }
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        for (SSNode n : nodes) {
            n.tick();
        }
        repaint();
    }
    
    /**
     * Add a new node to the canvas.
     */
    public void addNode(SSNode n) {
        this.nodes.add(n);
    }
    
    /**
     * Get the node containing the point p. Return null if no such node exists.
     */
    public SSNode getNode(Point2D p) {
        SSNode hit = null;
        int i = 0;
        while (hit == null && i < nodes.size()) {
            hit = nodes.get(i).hitNode(p);
            i++;
        }
        return hit;
    }
    public Icon getTool(Point2D p) {
        Icon hit = null;
        int i = 0;
        while (hit == null && i < tools.size()) {
            if(tools.get(i).contains(p)){
                hit = tools.get(i);
            }
            i++;
        }
        return hit;
    }
    
    public void toggelToolbar(){
        toolbar = !toolbar;
    }

    
    /**
     * Convert p to a Point2D, which has higher precision.
     */
    private Point2D.Double p2D(Point p) {
        return new Point2D.Double(p.getX(), p.getY());
    }


    /**
     * Listen for mouse events on the Canvas. Pass them on to the selected node
     * (if there is one) in most cases.
     */
    class MouseHandler implements MouseInputListener {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            clickedNode = getNode(p2D(e.getPoint()));
            selectedTool = getTool(p2D(e.getPoint()));
            if (clickedNode != null) {
                if(clickedNode == selectedNode){
                    selectedNode.deselect();
                    selectedNode = null;
                    toolbar = false;
                }else{
                    if(selectedNode != null){
                        selectedNode.deselect();
                    }
                    selectedNode = clickedNode;
                    toolbar = true;
                    clickedNode.select();
                }

                //toggelToolbar();
                repaint();
            }else if(selectedTool != null && toolbar){
                selectedTool.action(selectedNode); 
                selectedTool.mouseClicked(e);
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            clickedNode = getNode(p2D(e.getPoint()));
            selectedTool = getTool(p2D(e.getPoint()));
            if (clickedNode != null) {
                clickedNode.mousePressed(e);
                repaint();
            }else if(selectedTool != null && toolbar){
                selectedTool.mousePressed(e);
                                repaint();
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (clickedNode != null) {
                clickedNode.mouseReleased(e);

            }else if(selectedTool != null && toolbar){
                selectedTool.mouseReleased(e);
                
                
            }
            repaint();
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            timer.stop();
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            for (SSNode n : nodes) {
                n.mouseExited(e);
            }
            toolbar = false;
            repaint();
            timer.start();
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (clickedNode != null) {
                clickedNode.mouseDragged(e);
                repaint();
            }
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            if(toolbar){
                selectedTool = getTool(p2D(e.getPoint()));
                if (selectedTool != null){
                    selectedTool.mouseEntered(e);
                    repaint();
                }
            }else if(toolbar){
                for (Icon n : tools) {
                    
                    //n.mouseExited(e);
                }
            }
        }
        
    }
}