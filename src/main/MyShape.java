package main;
import java.util.Random;

public class MyShape
{
    protected int radius;
    protected double ypos;
    protected double xpos;
    protected double yspeed;
    protected double xspeed;
    protected double x0;
    protected double y0;
    
    Random random = new Random();

    /**
     * Constructor for objects of class Shape
     */
    public MyShape()
    {
    }
   
   public void move()
    {
        xpos += xspeed;
        ypos += yspeed;
        
        if(xpos<0)
        {
            xpos = 0;
        }
        else if(xpos > Environment.width)
        {
            xpos = Environment.width;
        }
    }
    
    public void moveInCanvas()
    {
        xpos += xspeed;
        ypos -= yspeed;
        
        if(xpos<0)
        {
            xpos = 0;
        }
        else if(xpos > Environment.width)
        {
            xpos = Environment.width;
        }
    }
    
    public void reset()
    {
        xpos = x0;
        ypos = y0;
    }
    
    public double getXpos()
    {
        return xpos;
    }
    
    public void setXpos(double pos)
    {
        xpos = pos;
    }
    
    public double getYpos()
    {
        return ypos;
    }
    
    public void setYpos(double pos)
    {
        ypos = pos;
    }
    
    public void setXspeed(double speed)
    {
        xspeed = speed;
    }
    
    public double getXspeed()
    {
        return xspeed;
    }
    
    public double getYspeed()
    {
        return yspeed;
    }
    
    public void setYspeed(double speed)
    {
        yspeed = speed;
    }
    
    public double getRadius()
    {
        return radius;
    }

}