/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package screensaver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;
import static screensaver.Main.star;
import static screensaver.Main.heart;
import static screensaver.Main.rectangle;
import static screensaver.Main.circle;
/**
 *
 * @author Justin Hu
 */
public class Icon implements MouseListener {
    private String id;
    private Area icon;
    private Color color = Color.LIGHT_GRAY;
    private int index;
    private boolean message;
    
    public Icon(String id,int index){
        this.id = id;
        this.index =index;
        this.icon = new Area(new Rectangle2D.Double(24*index, 0, 22, 22));
    }
    
    public void subtract(Area area){
        this.icon.subtract(area);
    }
    
    public void paintIcon(Graphics2D g2) {
        g2.setColor(this.color);
        g2.fill(this.icon);
        g2.setColor(Color.DARK_GRAY);
        AffineTransform t = g2.getTransform();
        g2.translate(+this.index*24,0);
        AffineTransform s = g2.getTransform();
        switch (id) {
            case "Size Increase":
                g2.fillRect(1, 8, 20, 6);
                g2.fillRect(8,1, 6, 20);
                break;
            case "Size Decrease":
                g2.fillRect(1, 8, 20, 6);
                break;
            case "Rotate Right":
                g2.translate(21,0);
                g2.scale(-1, 1);
                g2.drawArc(3, 3, 16, 16, -45, 180);
                g2.fillPolygon(new int[] { 4,7,2 }, new int[] { 4,7,9 }, 3);
                break;
            case "Rotate Left":
                //g2.fillRect(1, 8, 20, 6);
                g2.drawArc(3, 3, 16, 16, -45, 180);
                g2.fillPolygon(new int[] { 4,7,2 }, new int[] { 4,7,9 }, 3);
                break;
            case "Spin Faster":
                g2.fillPolygon(new int[] { 4,4,12 }, new int[] { 6,16,11 }, 3);
                g2.fillPolygon(new int[] { 12,12,20 }, new int[] { 6,16,11 }, 3);
                break;
            case "Spin Slower":
                g2.translate(21,0);
                g2.scale(-1, 1);
                g2.fillPolygon(new int[] { 4,4,12 }, new int[] { 6,16,11 }, 3);
                g2.fillPolygon(new int[] { 12,12,20 }, new int[] { 6,16,11 }, 3);
                break;
            case "Reverse Direction":
                g2.drawArc(3, 3, 16, 16, 45, 90);
                g2.fillPolygon(new int[] { 4,7,2 }, new int[] { 4,7,9 }, 3);
                g2.translate(21,21);
                g2.scale(-1, -1);
                g2.drawArc(3, 3, 16, 16, 45, 90);
                g2.fillPolygon(new int[] { 4,7,2 }, new int[] { 4,7,9 }, 3);
                break;
            case "Add Child":
                g2.drawOval(1, 1, 13, 13);
                g2.drawOval(13, 13, 7, 7);
                break;
            case "Star":
                g2.translate(11, 8);
                g2.scale(0.4, 0.4);
                g2.fill(star);
                break;
            case "Heart":
                g2.translate(11, 8);
                g2.scale(0.4, 0.4);
                g2.fill(heart);
                break;
            case "Rectangle":
                g2.translate(11, 11);
                g2.scale(0.5, 0.5);
                g2.fill(rectangle);
                break;
            case "Circle":
                g2.translate(11, 11);
                g2.scale(0.3, 0.3);
                g2.fill(circle);
                break;
            case "Red":
                g2.translate(11, 11);
                g2.scale(0.5, 0.5);
                g2.setColor(Color.RED);
                g2.fill(rectangle);
                break;
            case "Orange":
                g2.translate(11, 11);
                g2.scale(0.5, 0.5);
                g2.setColor(Color.ORANGE);
                g2.fill(rectangle);
                break;
            case "Yellow":
                g2.translate(11, 11);
                g2.scale(0.5, 0.5);
                g2.setColor(Color.YELLOW);
                g2.fill(rectangle);
                break;    
            case "Green":
                g2.translate(11, 11);
                g2.scale(0.5, 0.5);
                g2.setColor(Color.GREEN);
                g2.fill(rectangle);
                break;    
            case "Blue":
                g2.translate(11, 11);
                g2.scale(0.5, 0.5);
                g2.setColor(Color.BLUE);
                g2.fill(rectangle);
                break;      
        }
        g2.setTransform(s);
        g2.setColor(Color.WHITE);
        if(message){
            g2.drawString(id, 2, 35);
            message = false;
        }
        g2.setTransform(t);
    }
    
    public boolean contains(Point2D p){
        return this.icon.contains(p);
    }
    
    public String getId(){
        return this.id;
    }
    
    public void action(SSNode node){
        switch (id) {
            case "Size Increase":
                node.scale(1.1);
                break;
            case "Size Decrease":
                node.scale(1/1.1);
                break;
            case "Rotate Right":
                node.transform(AffineTransform.getRotateInstance(Math.PI/32));
                break;
            case "Rotate Left":
                node.transform(AffineTransform.getRotateInstance(-Math.PI/32));
                break;
            case "Spin Faster":
                node.faster();
                break;
            case "Spin Slower":
                node.slower();
                break;
            case "Reverse Direction":
                node.reverse();
                break;
            case "Add Child":
                SSNode child = new SSNode(star, Color.RED);
                child.transform(AffineTransform.getTranslateInstance(50, 50));
		child.transform(AffineTransform.getScaleInstance(0.8, 0.8));
                node.addChild(child);
                break; 
            case "Star":
                node.setShape(star);
                break;
            case "Heart":
                node.setShape(heart);
                break;
            case "Rectangle":
                node.setShape(rectangle);
                break;
            case "Circle":
                node.setShape(circle);
                break;
             case "Red":
                node.setColor(Color.RED);
                break;
            case "Orange":
                node.setColor(Color.ORANGE);
                break;
            case "Yellow":
                node.setColor(Color.YELLOW);
                break;    
            case "Green":
                node.setColor(Color.GREEN);
                break;    
            case "Blue":
                node.setColor(Color.BLUE);
                break;   
        }
    }
    
    public void message(){
        this.message = true;
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        this.color = color.DARK_GRAY;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        this.color = color.LIGHT_GRAY;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        this.message(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
         System.out.println(this.id + "leaving"); //To change body of generated methods, choose Tools | Templates.
    }
}
