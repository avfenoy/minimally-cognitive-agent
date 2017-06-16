package graphics;

import java.awt.*;

import main.Environment;
import main.MyShape;

/**
 * This class implements the graphical representation and behavior of the shapes used in the experiment.
 */

public class DrawableShape
{
    private Canvas canvas;
    public MyShape shape;
    private Color color;
    
    public DrawableShape(MyShape shape, Canvas drawingCanvas, Color color)
    {
        this.shape = shape;
        canvas = drawingCanvas;
        this.color = color;
    }
    
    /**
     * Draw a circular shape.
     */
    public void draw()
        {
            canvas.setForegroundColor(color);
            canvas.fillCircle(shape.getXpos(), Environment.height-shape.getYpos(),(double) shape.getRadius()*2);
        }

    /**
     * Erase this ball at its current position.
     **/
    public void erase()
    {
        canvas.eraseCircle(shape.getXpos(), Environment.height-shape.getYpos(), (double) shape.getRadius()*2);
    }    

    /**
     * Move this ball according to its position and speed and redraw.
     **/
    public void move()
    {
        erase();
        shape.move();
        draw();
    }    

    /**
     * Return the horizontal position of this ball.
     */
    public double getXPosition()
    {
        return shape.getXpos();
    }

    /**
     * Return the vertical position of this ball.
     */
    public double getYPosition()
    {
        return shape.getYpos();
    }
}
