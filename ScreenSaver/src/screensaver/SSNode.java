package screensaver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represent one Screen Save Node.
 */
public class SSNode implements MouseMotionListener, MouseListener {

    private AffineTransform trans = new AffineTransform();
    private Shape shape;
    private ArrayList<SSNode> children = new ArrayList<SSNode>();
    private SSNode parent = null;
    private String id; // for debugging printf statements
    private Point2D lastPoint = null;
    private Point2D origin = new Point2D.Double(0, 0);
    private Color color = Color.RED;
    private Boolean selected = false;
    private double speed = Math.PI/128;

    /**
     * Create a new SSNode, given a shape and a colour.
     */
    public SSNode(Shape s, Color color) {
        this.id = "id";
        this.shape = s;
        this.color = color;
    }

    /**
     * Set this node's shape to a new shape.
     */
    public void setShape(Shape s) {
        this.shape = s;
    }
    
    public void setColor(Color c) {
        this.color = c;
    }

    /**
     * Add a child node to this node.
     */
    public void addChild(SSNode child) {
        child.id = this.id + "." + (this.children.size());
        this.children.add(child);
        child.parent = this;
    }

    /**
     * Is this node the root node? The root node doesn't have a parent.
     */
    private boolean isRoot() {
        return this.parent == null;
    }

    /**
     * Get this node's parent node; null if there is no such parent.
     */
    public SSNode getParent() {
        return this.parent;
    }

    /**
     * One tick of the animation timer. What should this node do when a unit of
     * time has passed?
     */
    public void tick() {
        for (SSNode c : this.children) {
            c.tick();
        }
        // Because the root node doesn't rotate, it'll be a special case.
        if (!this.isRoot()) {
            this.trans.preConcatenate(AffineTransform.getRotateInstance(this.speed));
        }

    }
    
    public void select() {
        this.selected = true;

    }
    
    public void deselect() {
        this.selected = false;

    }
    
    public void faster() {
        this.speed = speed*1.1;
    }
    
    public void slower() {
        this.speed = speed/1.1;
    }
    
    public void reverse(){
        this.speed = -speed;
    }

    /**
     * Does this node contain the given point (which is in window coordinates)?
     */
    public boolean containsPoint(Point2D p) {
        AffineTransform inverseTransform = this.getFullInverseTransform();
        Point2D pPrime = inverseTransform.transform(p, null);

        return this.shape.contains(pPrime);
    }

    /**
     * Return the node containing the point. If nodes overlap, child nodes take
     * precedence over parent nodes.
     */
    public SSNode hitNode(Point2D p) {
        for (SSNode c : this.children) {
            SSNode hit = c.hitNode(p);
            if (hit != null) {
                return hit;
            }
        }
        if (this.containsPoint(p)) {
            return this;
        } else {
            return null;
        }
    }

    /**
     * Transform this node's transformation matrix by concatenating t to it.
     */
    public void transform(AffineTransform t) {
        this.trans.concatenate(t);
    }

    /**
     */
    public Point2D.Double getCenter() {
        return new Point2D.Double(origin.getX(), origin.getY());
    }

    /**
     * Convert p to a Point2D.
     */
    private Point2D.Double p2D(Point p) {
        return new Point2D.Double(p.getX(), p.getY());
    }

    
    public void scale(double s){
        AffineTransform scale = AffineTransform.getScaleInstance(s, s);
        AffineTransform iScale = null;
        try {
            iScale = scale.createInverse();
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(SSNode.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.trans.concatenate(scale);

        for (SSNode c : this.children) {
            c.trans.preConcatenate(iScale);
        }
    }
    /**
     * ***********************************************************
     * @param e* Handle mouse events directed to this node.
     *
     ************************************************************
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            this.selected = !selected;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.lastPoint = p2D(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.lastPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.selected = false;
        for (SSNode c : this.children) {
            c.mouseExited(e);
        }
    }

    /**
     * Handle mouse drag event, with the assumption that we have already been
     * "selected" as the sprite to interact with. This is a very simple method
     * that only works because we assume that the coordinate system has not been
     * modified by scales or rotations. You will need to modify this method
     * appropriately so it can handle arbitrary transformations.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D mouse = p2D(e.getPoint());

        double dx, dy;
        dx = mouse.getX() - this.lastPoint.getX();
        dy = mouse.getY() - this.lastPoint.getY();

        if (!this.isRoot()) {
            AffineTransform A = this.getParent().getFullTransform();
            AffineTransform Ai = this.getParent().getFullInverseTransform();
            AffineTransform drag = AffineTransform.getTranslateInstance(dx, dy);

            this.trans.preConcatenate(A);
            this.trans.preConcatenate(drag);
            this.trans.preConcatenate(Ai);

            Point2D pt = this.getParent().getCenter();
            double ax = this.origin.getX() - pt.getX();
            double ay = this.origin.getY() - pt.getY();
            double bx = ax + dx;
            double by = ay + dy;

            double Side_A = Math.sqrt(ax * ax + ay * ay);
            double Side_B = Math.sqrt(bx * bx + by * by);
            double Side_C = Math.sqrt(dx * dx + dy * dy);
            double Angle_c = Math.acos((Side_A * Side_A + Side_B * Side_B - Side_C * Side_C) / (2 * Side_A * Side_B));

            if (((ax - 0) * (by - 0) - (ay - 0) * (bx - 0)) > 0) {
                //ccw
                this.trans.concatenate(AffineTransform.getRotateInstance(Angle_c));
            } else {
                //cw
                this.trans.concatenate(AffineTransform.getRotateInstance(-Angle_c));
            }

        } else {
            this.trans.translate(dx, dy);
        }

        lastPoint = mouse;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Paint this node and its children.
     */
    public void paintNode(Graphics2D g2) {
        /*
         * You can change this code if you wish. Based on an in-class example
         * it's going to be really tempting. You are advised, however, not to
         * change it. Doing so will likely bring you hours of grief and much
         * frustration.
         */

        // Remember the transform being used when called
        AffineTransform t = g2.getTransform();

        g2.transform(this.getFullTransform());

        g2.setColor(this.color);
        g2.fill(this.shape);
        if(this.selected){
            g2.setColor(Color.YELLOW);
            g2.draw(this.shape);
        }
        // Restore the transform.
        g2.setTransform(t);

        AffineTransform Transform = this.getFullTransform();
        this.origin = Transform.transform(new Point2D.Double(0, 0), null);

        // Paint each child
        for (SSNode c : this.children) {
            c.paintNode(g2);
        }

        // Restore the transform.
        g2.setTransform(t);
    }

    /*
     * There are a number of ways in which the handling of the transforms could
     * be optimized. That said, don't bother. It's not the point of the
     * assignment.
     */
    /**
     * Returns our local transform. Copy it just to make sure it doesn't get
     * messed up.
     */
    public AffineTransform getLocalTransform() {
        return new AffineTransform(this.trans);
    }
    
    /**
     * Returns the full transform to this node from the root.
     */
    public AffineTransform getFullTransform() {
		// Start with an identity matrix. Concatenate on the left
        // every local transformation matrix from here to the root.
        AffineTransform at = new AffineTransform();
        SSNode curNode = this;
        while (curNode != null) {
            at.preConcatenate(curNode.getLocalTransform());
            curNode = curNode.getParent();
        }
        return at;
    }

    /**
     * Return the full inverse transform, starting with the root. That is, get
     * the full transform from here to the root and then invert it, catching
     * exceptions (there shouldn't be any).
     */
    private AffineTransform getFullInverseTransform() {
        try {
            AffineTransform t = this.getFullTransform();
            AffineTransform tp = t.createInverse();
            return tp;
        } catch (NoninvertibleTransformException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new AffineTransform();
        }
    }

}
