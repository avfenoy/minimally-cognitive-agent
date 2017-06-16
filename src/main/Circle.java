package main;

public class Circle extends MyShape
{    
    /**
     * Constructor for objects of class Circle
     */
    public Circle()
    {
        xspeed = random.nextDouble() * 12 - 6;
        yspeed = - (random.nextDouble() * 4.5 + 0.5);
        x0 = Environment.width/2;
        y0 = Environment.height;
        radius = 13;
        reset();
    }
    
    public Circle(double xspeed, double yspeed)
    {
        this.xspeed = xspeed;
        this.yspeed = yspeed;
        x0 = Environment.width/2;
        y0 = Environment.height;
        radius = 13;
        reset();
    }
    
    public Circle(double xspeed, double yspeed, double x0)
    {
        this.xspeed = xspeed;
        this.yspeed = yspeed;
        this.x0 = x0;
        y0 = Environment.height;
        radius = 13;
        reset();
    }
}
