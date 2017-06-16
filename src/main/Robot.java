package main;
import java.lang.Math;
import java.util.Arrays;
//import java.util.Random;

/**
 * Class implementing the agent and its controller.
 */
public class Robot extends MyShape
{
    private int index;
    private final static int s = 5;
    private final static int[] rayIndex = {-2,-1,0,1,2};
    // private final static int[] rayIndex = {-3,-2,-1,0,1,2,3};
    private final static double visualAngle = Math.PI/6;
    static double[] angles = new double[s];
    static double[] raySlopes = new double[s];
    static {
        for(int i = 0; i<s; i++)
            {
                angles[i] = rayIndex[i] * visualAngle / (s-1);
                raySlopes[i] = Math.tan((Math.PI / 2) + angles[i]);
            }
    }
    
    private double[] genome;
    
    private final static int rayLength = 220;
    final static int maxInput= 10;
        
    public Controller controller;
    
    /**
     * Constructor for objects of class Robot
     */
    public Robot(int index)
    {
        radius = 15;
        x0 = Environment.width/2;
        y0 = 0;
        xspeed = 0;
        yspeed = 0;
        reset();
        
        this.index = index;
        genome = new double[Controller.parameters];
        for(int i=0; i < Controller.parameters; i++)
        {
            genome[i] = random.nextDouble() * 2 - 1;
        }
//         genome[7] = genome[7] *2 + 3;
//         genome[8] = genome[8] * 2.5 - 7.5;
        genome[10] += 2;
        genome[11] += 2;
        controller = new Controller();
    }    
    
    /**
     * @return index associated with this robot
     */
    public int getIndex()
    {
        return index;
    }
    
    /**
     * @return the robot's genome.
     */
    public double[] getGenome()
    {
        return genome;
    }
    
    /**
     * Set the genome of the robot.
     * @param newGenome an array of values for each gene.
     */
    public void setGenome(double[] newGenome)
    {
        genome = newGenome;
    }
    
    /**
     * Calculate input values for each sensor if a shape is within the visual field of the robot, i.e., ...
     * if the shape and the ray from a given sensor intersect.
     * @param shape the shape that is currently on the environment.
     * @return an array of values representing the input to each sensor.
     */
    public double[] calcInput(MyShape shape)
    {
        double[] input = new double[Controller.nodes];
        double a = shape.getXpos();
        double b = shape.getYpos();
        double r = shape.getRadius();
        double A,B,C;
        
        for(int ray = 0; ray<Controller.sensors; ray++)
        {
            // system of equations to find interception (x-a)^2 + (y-b)^2 = r^2 with y = mx+n, or with x=xpos for vertical line
            // a and b are x and y coordinates of the shape, m is the slope of the ray, and n the intercept

            if(rayIndex[ray] != 0)
            {
                double m = raySlopes[ray];
                double n = - m * xpos;
                double s = n - b;
                A = 1 + Math.pow(m,2);
                B = 2 * (m*s - a);
                C = Math.pow(a,2) + Math.pow(s,2) - Math.pow(r, 2);
                double[] sol = solveQuadratic(A,B,C);
                if(sol!=null)
                {
                    double x;
                    if(rayIndex[ray] > 0)
                    {
                        x = Math.max(sol[0],sol[1]);
                    }
                    else
                    {
                        x = Math.min(sol[0],sol[1]);
                    }
                    double y = m * x + n;
                    double d = Math.sqrt(Math.pow((x - xpos),2) + Math.pow(y - ypos, 2)); 
                    input[ray] = boundInput(d);
                }
                else
                {
                    input[ray] = 0;
                }
            }
            else
            {
                A = 1;
                B = -2*b;
                C = Math.pow(xpos,2) - 2*xpos*a + Math.pow(a,2) + Math.pow(b,2) - Math.pow(r, 2);
                double[] sol = solveQuadratic(A,B,C);
                if(sol!=null)
                {
                    double y = Math.min(sol[0],sol[1]);
                    double d = Math.abs(y - ypos);
                    input[ray] = boundInput(d);
                }
                else
                {
                    input[ray] = 0;
                }
            }
        }
        return input;
    }
    
    /**
     * Solve a quadratic equation
     * @param A first coefficient
     * @param B second coefficient
     * @param C third coefficient
     * @return the solution to the equation, or null if it has no solution
     */
    public double[] solveQuadratic(double A, double B, double C)
    {
        double radicand = Math.pow(B, 2) - 4*A*C;
        if(radicand >= 0)
        {
            double sol1 = (-B + Math.sqrt(radicand)) / 2*A;
            double sol2 = (-B - Math.sqrt(radicand)) / 2*A;
            double[] sol = {sol1,sol2};
            return sol;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Assess if a quadratic equation is solvable.
     * @param A first coefficient
     * @param B second coefficient
     * @param C third coefficient
     * @return true if the equation is solvable
     */
    public boolean isSolvable(double A, double B, double C)
    {
        double radicand = Math.pow(B, 2) - 4*A*C;
        if(radicand >= 0)
        {
            return true;
        }
        else
        {
            return false;
        }        
    }
    
    /**
     * Bound the value of the input within the established range.
     * @param d distance to the input
     * @return the bounded input value
     */
    private double boundInput(double d)
    {
        double bounded = 0;
        if(d<rayLength)
        {
            bounded = maxInput - d * maxInput / rayLength;
        }
        return bounded;
    }    
    
    
    /**
     * 
     * Internal class that sets up the neural network that controls the robot.
     *
     */
    class Controller
    {
        final static int nodes = 7;
        //         final static int nodes = 14;
        final static int parameters = 12;
        //         final static int parameters = 12;
        private final static int sensors = 5;
        //         private final static int sensors = 7;
        
        double[] bias = new double[nodes];
        double[] gain = new double[nodes];
        double[] tau = new double[nodes];
        double[][] weights = new double[nodes][nodes];
        
        private Controller()
        {
            update();    
        }   
        
        public void update()
        {
        	//             Arrays.fill(gain, 0, sensors, genome[7] * 2 + 3);
        	//             Arrays.fill(gain, 0, sensors, genome[7] * 5);
            Arrays.fill(gain, 0, sensors, genome[7]);
            Arrays.fill(gain, sensors, nodes, 1);
            
            //             Arrays.fill(bias, 0, sensors, genome[8] * 2.5 - 7.5);
            //             Arrays.fill(bias, 0, sensors, genome[8] * 5);
            Arrays.fill(bias, 0, sensors, genome[8]);
            //             Arrays.fill(bias, sensors, nodes, genome[9] * 5);
            Arrays.fill(bias, sensors, nodes, genome[9]);
            
            
            //             Arrays.fill(tau, 0, sensors, genome[10]*0.5 +1.5);
            //             Arrays.fill(tau, 0, sensors, genome[10]*5);
            Arrays.fill(tau, 0, sensors, genome[10]);
            //             Arrays.fill(tau, sensors, nodes, genome[11]*0.5+1.5);
            //             Arrays.fill(tau, sensors, nodes, genome[11]*5);
            Arrays.fill(tau, sensors, nodes, genome[11]);
            
            for(int w=0; w<sensors; w++)
            {
                weights[w][5] = genome[w];
                weights[w][6] = genome[sensors-1-w];
            }
            for(int w=sensors; w<nodes; w++)
            {
                weights[w][5] = genome[w];
                weights[w][6] = genome[nodes-1-w];
            }        
        }
    }
}
