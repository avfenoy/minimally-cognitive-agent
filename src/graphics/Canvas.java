package graphics;

import javax.swing.*;

import main.Environment;

import java.awt.*;
import java.awt.geom.*;

/**
 *
 */
public class Canvas
{
    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private final Color backgroundColor = Color.white;
    private Image canvasImage;
    
    final static int width = Environment.width + 13;
    final static int height = Environment.height + 13;
    
    public static void main(String[] args)
    {
        Canvas myCanvas = new Canvas();
        myCanvas.setForegroundColor(Color.black);
        myCanvas.setVisible(true);
//         DrawableShape shape = new DrawableShape(new MyShape(), myCanvas);
//         shape.draw();
//         while(this.shape.shape.getYpos()<height -13)
//         {
//             myCanvas.wait(10);
//             this.shape.move();
//         }
    }

    /**
     * Constructor for objects of class Canvas
     */
    public Canvas()
    {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        canvas.setPreferredSize(new Dimension(width, height));
        frame.pack();
        
        setVisible(true);
        setForegroundColor(Color.black);
    }
    
    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
     public void setVisible(boolean visible)
    {
        if(graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background color
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColor);
            graphic.fillRect(0, 0, size.width, size.height);
        }
        frame.setVisible(true);
    }
    
    /**
     * Erase a given shape's interior on the screen.
     * @param  shape  the shape object to be erased 
     */
    public void erase(Shape shape)
    {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        graphic.fill(shape);              // erase by filling background color
        graphic.setColor(original);
        canvas.repaint();
    }
    
    /**
     * Erase the internal dimensions of the given circle. This is a 
     * convenience method. A similar effect can be achieved with
     * the "erase" method.
     */
    public void eraseCircle(double xPos, double yPos, double diameter)
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(xPos, yPos, diameter, diameter);
        erase(circle);
    }
    
       /**
     * Draw the outline of a given shape onto the canvas.
     * @param  shape  the shape object to be drawn on the canvas
     */
    public void draw(Shape shape)
    {
        graphic.draw(shape);
        canvas.repaint();
    }
    
    /**
     * Fill the internal dimensions of a given shape with the current 
     * foreground color of the canvas.
     * @param  shape  the shape object to be filled 
     */
    public void fill(Shape shape)
    {
        graphic.fill(shape);
        canvas.repaint();
    }
    
        /**
     * Fill the internal dimensions of the given circle with the current 
     * foreground color of the canvas.
     */
    public void fillCircle(double xPos, double yPos, double diameter)
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(xPos, yPos, diameter, diameter);
        fill(circle);
    }
    
    //TODO
    public void fillDiamond(double xPos, double yPos, double height)
    {
        Rectangle2D.Double diamond = new Rectangle2D.Double(xPos, yPos, height, height);
        
        graphic.translate(xPos,yPos);
        graphic.rotate(Math.PI/2);
        fill(diamond);
        graphic.rotate(-Math.PI/2);
        graphic.translate(-xPos,-yPos);
    }
    
        /**
     * Sets the foreground color of the Canvas.
     * @param  newColor   the new color for the foreground of the Canvas 
     */
    public void setForegroundColor(Color newColor)
    {
        graphic.setColor(newColor);
    }
    
     /**
     * Waits for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     * @param  milliseconds  the number 
     */
    public void wait(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        } 
        catch (InterruptedException e)
        {
            // ignoring exception at the moment
        }
    }
    
    /**
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel
    {
        public void paint(Graphics g)
        {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
}
