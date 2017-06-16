package main;
import graphics.GraphPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class implements the actual experiment. The number of individuals in the population,
 * generations, and evaluation trials has to be changed manually in the code.
 *
 */
public class Experiment
{
    int population = 10; 
    int generations = 20;
    int nParameters = 12;
    int evalTrials = 8;
    double[] distance = new double[evalTrials];
    double[] fitness = new double[population];
    double[] maxfit;
    
    List<MyShape> circles;
    List<Robot> agents;
    
    public Experiment()
    {
        run();
        finalEvalSim(10);
    }
    
    public static void main(String[] args)
    {
    }
    
    public void run()
    {
        
        double recomb = 0.5;
        double mutation = 3;
        
        //ArrayList<Circle> circles = createShapes(evalTrials);
        circles = createPredefShapes();        
        agents = createAgents(population);
        

        
        Random random = new Random();
        System.out.println("Parameters ok!.");
        
        for(int i=0; i<population; i++)
        {
            Robot agent = agents.get(i);
            distance = evalAgent(agent, circles, evalTrials);
            fitness[i] =  (200 - arraySum(distance)/evalTrials) / 2;
        }
        System.out.println("Initial population assessed!!!");

        // evolve
        int deme = 10; // distance of neighbourhood for trivial geometry
        int steps = population * generations;
        
        maxfit = new double[steps+1];
        maxfit[0] = findMax(fitness);
        
        System.out.println("Initial maximum fitness = " + maxfit[0] + " %");
        
        for(int step = 0; step<steps; step++)
        {
            
            // choose two neighbouring individuals    
            int A = (int) Math.floor(random.nextDouble()*population);
            int B = (int) ((A + Math.ceil(random.nextDouble()*deme)) % (population-1));
            Robot winner, loser;
            if(fitness[A] > fitness[B])
            {
                winner=agents.get(A);
                loser=agents.get(B);
            }
            else
            {
                winner=agents.get(B);
                loser=agents.get(A);
            }
            
            //recombination
            for(int np = 0; np<Robot.Controller.parameters; np++)
            {
                if(random.nextDouble()<recomb)
                {
                    loser.setGenome(winner.getGenome());
                }
            }

            // mutation
            double[] newGenome = new double[Robot.Controller.parameters];
            for(int i=0; i<Robot.Controller.parameters; i++)
            {
                double x = loser.getGenome()[i] + random.nextGaussian() * mutation;
//                 if(x<-1)
//                 {
//                     x= -2-x;
//                 }
//                 if(x>1)
//                 {
//                     x= 2-x;
//                 }


                newGenome[i] = x;
            }
            if(newGenome[7]<0)
            {
                newGenome[7] = -newGenome[7];
            }
            if(newGenome[10] < 1)
            {
                newGenome[10] = 1 + (Math.abs(1-newGenome[10]));
            }
            if(newGenome[11]<1)
            {
                newGenome[11] = 1 + (Math.abs(1-newGenome[11]));
            }
            loser.setGenome(newGenome);
            loser.controller.update();
            
            // assess and store fitness of child 
            distance = evalAgent(loser, circles, evalTrials);
         
            int nloser = loser.getIndex();
            fitness[nloser] = (200 - arraySum(distance)/evalTrials)/2;
            if(fitness[nloser] > maxfit[step])
            {
                maxfit[step+1] = fitness[nloser];
                System.out.println("New maxfit = " + maxfit[step+1] + " %");
                if(fitness[nloser] > 99)
                {
                    System.out.println("Step number " + step);
                    break;
                }
            }
            else
            {
                maxfit[step+1] = maxfit[step];
            }

        }
        
        System.out.println("COMPLETE!!!");
        System.out.println("Mean fitness = " + mean(fitness) + " %");
    }
    
    public void finalEvalSim(int trials)
    {
        System.out.println("Final evaluation:");
        List<MyShape> finalCircles = createShapes(trials);
        
        int maxIndex=0;
        for(int i=0; i<population; i++)
        {
            if(fitness[i]==maxfit[maxfit.length-1])
            {
                maxIndex = i;
                break;
            }
        }
        
        Robot agent = agents.get(maxIndex);
        double [] finalDistance = evalAgentSim(agent, finalCircles, trials); 

        System.out.println("Fittest agent = " + maxIndex);
        System.out.println("Final trials = " + trials);
        System.out.println("Circle position and distance from robot end position:");
        for(int i = 0; i<trials; i++)
        {
            System.out.println(finalCircles.get(i).getXpos() + " " + finalDistance[i]);
        }
        double finalFit =  (200 - arraySum(finalDistance)/trials)/2;
        System.out.println("Final fitness = " + finalFit + " %");
    }
    
    
    
    public void finalEval(int trials)
    {
        System.out.println("Final evaluation:");
        List<MyShape> circles = createShapes(trials);
        double[] finalDist = new double[trials];

        for(int i=0; i<population; i++)
        {
            Robot agent = agents.get(i);
            finalDist = evalAgent(agent, circles, trials);
            fitness[i] =  (200 - arraySum(finalDist)/trials)/2;
        }
        System.out.println("Maximum fitness = " + findMax(fitness) + " %");
        System.out.println("Mean fitness = " + mean(fitness) + " %");
    }
    
    public void plotGraph()
    {
        ArrayList<Double> toPlot = new ArrayList<Double>();
        for(Double d:maxfit)
        {
            toPlot.add(d);
        }
        GraphPanel.createGraph(toPlot);
    }
    
    
    public static List<MyShape> createShapes(int n)
    {
        List<MyShape> array = new ArrayList<MyShape>();
        for(int i=0; i<n;i++)
        {
           array.add(new Circle());
        }
        return array;
    }
    
    public static List<MyShape> createPredefShapes()
    {
        List<MyShape> array = new ArrayList<MyShape>();
        array.add(new Circle(0.7,-3));
        array.add(new Circle(-0.1, -1));
        array.add(new Circle(3,-4.5));
        array.add(new Circle(-2.5,-3.5));
        array.add(new Circle(5,-1));
        array.add(new Circle(-5,-1));
        array.add(new Circle(-6, -5));
        array.add(new Circle(6,-5));
        return array;
    }
    
    public static ArrayList<Robot> createAgents(int n)
    {
        ArrayList<Robot> r = new ArrayList<Robot>();
        for(int i=0; i<n; i++)
        {
            r.add(new Robot(i));
        }
        return r;
    }
    
    public static double findMax(double[] array)
    {
        double max = 0;
        for(int i=0; i<array.length; i++)
        {
            if(array[i] > max)
            {
                max = array[i];
            }
        }
        return max;
    }
    
    public static double arraySum(double[] array)
    {
        double sum = 0;
        for(int i=0; i<array.length; i++)
        {
            sum += array[i];
        }
        return sum;
    }
    
    public static double mean(double[] array)
    {
        double sum = 0;
        for(int i = 0; i<array.length; i++)
        {
            sum += array[i];
        }
        return sum/array.length;
    }
    
    public static double[] evalAgent(Robot agent, List<MyShape> shapes, int trials)
    {
        double[] distance = new double[trials];
        double h = 0.1;
        //[weight gain bias tau] = mapParameters(agent.parameters,nodes);
        for(int trial=0; trial<trials; trial++)
        {
            double[] y = new double[Robot.Controller.nodes];
            agent.setXpos(agent.x0);
            MyShape circle = shapes.get(trial);
            circle.reset();
            while(circle.getYpos() > 0)
            {
                circle.move();
                double[] input = agent.calcInput(circle);
                for(int update = 0; update < 1/h; update++)
                {
                    for (int nodei=0; nodei<Robot.Controller.nodes; nodei++)
                    {
                        double wsum = 0;
                        for (int nodej=0; nodej<Robot.Controller.nodes; nodej++)
                        {
                            wsum += agent.controller.weights[nodej][nodei] * Math.tanh(agent.controller.gain[nodej]*(y[nodej]+agent.controller.bias[nodej]));
                        }
                        y[nodei] = y[nodei] + h / agent.controller.tau[nodei] * (-y[nodei] + wsum + input[nodei]);
                    }
                }
                agent.setXspeed((y[5] + y[6]) * 0.2);
                agent.move();
            }
            distance[trial] = Math.abs(agent.getXpos() - circle.getXpos());
//             System.out.println("Circle final = " + circle.getXpos() + ". Agent final = " + agent.getXpos());
        }
        return distance;
    }
    
        public static double[] evalAgentSim(Robot agent, List<MyShape> shapes, int trials)
    {
        double[] distance = new double[trials];
        double h = 0.1;
        
         Simulation sim = new Simulation(agent, shapes);
        
        for(int trial=0; trial<trials; trial++)
        {
            double[] y = new double[Robot.Controller.nodes];
            agent.setXpos(agent.x0);
            Circle circle = (Circle) shapes.get(trial);
            circle.reset();
            
            sim.setCircle(circle);
            
            while(circle.getYpos() > 0)
            {
                circle.move();
                double[] input = agent.calcInput(circle);
                for(int update = 0; update < 1/h; update++)
                {
                    for (int nodei=0; nodei<Robot.Controller.nodes; nodei++)
                    {
                        double wsum = 0;
                        for (int nodej=0; nodej<Robot.Controller.nodes; nodej++)
                        {
                            wsum += agent.controller.weights[nodej][nodei] * Math.tanh(agent.controller.gain[nodej]*(y[nodej]+agent.controller.bias[nodej]));
                        }
                        y[nodei] = y[nodei] + h / agent.controller.tau[nodei] * (-y[nodei] + wsum + input[nodei]);
                    }
                }
                agent.setXspeed((y[5] + y[6]) * 0.2);
                agent.move();
                sim.update(5);
                //System.out.println(circle.getXpos() + " " + agent.getXpos());
            }
            distance[trial] = Math.abs(agent.getXpos() - circle.getXpos());
            sim.update(250);
        }
        return distance;
    }
    
    public static double sigmoid(double exp)
    {
            return 1 / (1 + Math.exp(-exp));
    }
    
    public void printArray(double[] array)
    {
        for(int i=0; i<array.length; i++)
        {
            System.out.println(array[i]);
        }
    }   
}
