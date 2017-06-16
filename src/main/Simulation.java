package main;
import graphics.Canvas;
import graphics.DrawableShape;

import java.awt.Color;
import java.util.List;

public class Simulation
{
    // instance variables - replace the example below with your own
    private DrawableShape robot;
    private List<MyShape> shapes;
    private DrawableShape shape;
    private Canvas canvas;

    /**
     * Constructor for objects of class Simulation
     */
    public Simulation(Robot robot, List<MyShape> shapes)
    {
        canvas = new Canvas();
        canvas.setVisible(true);
        canvas.setForegroundColor(Color.black);
        this.robot = new DrawableShape(robot, canvas, Color.black);
        this.shapes = shapes;
    }
    
    public Simulation(Robot robot, Circle shape)
    {
        canvas = new Canvas();
        canvas.setVisible(true);
        canvas.setForegroundColor(Color.black);
        this.robot = new DrawableShape(robot, canvas, Color.black);
        setCircle(shape);
    }
    
    public void run()
    {
        for(MyShape c:shapes)
        {
            shape = new DrawableShape(c, canvas, Color.blue);
            robot.draw();
            while(shape.shape.getYpos()>0)
            {
                shape.move();
                robot.move();
            }
            shape.erase();
        }
    }
    
    public void setCircle(Circle circle)
    {
        shape = new DrawableShape(circle, canvas, Color.blue);
    }
    
    public void start()
    {
        robot.draw();
        shape.draw();
    }
    
    public void reset()
    {   
        robot.erase();
        shape.erase();
    }
    
    public void update(int rate)
    {
        start();
        canvas.wait(rate);
        reset();
    }
    
    public void wait(int milliseconds)
    {
        canvas.wait(milliseconds);
    }
}
