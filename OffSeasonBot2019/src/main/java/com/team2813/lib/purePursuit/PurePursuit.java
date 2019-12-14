package com.team2813.lib.purePursuit;

import java.util.*;

public class PurePursuit {

    public double[][] origPath;
    public double[][] nodeOnlyPath;
    public double[][] smoothPath;
    public double[][] leftPath;
    public double[][] rightPath;

    //Orig Velocity
    public double[][] origCenterVelocity;
    public double[][] origRightVelocity;
    public double[][] origLeftVelocity;

    //smooth velocity // how are the data points stored?
    public double[][] smoothCenterVelocity;
    public double[][] smoothRightVelocity;
    public double[][] smoothLeftVelocity;

    //accumulated heading // What is heading?
    public double[][] heading;

    double totalTime;
    double totalDistance;
    double numFinalPoints;

    double targetX;
    double targetY;


    public double pAlpha, pBeta, pTolerance; //pathing variables for path injecting algorithm

    public double vAlpha, vBeta, vTolerance; //tolerance variables for smoothing algorithm

    public double getRightVelocity() {
        return smoothRightVelocity[1][1];
    }

    public double getLeftVelocity() {
        return smoothLeftVelocity[1][1];
    }

    public boolean done() {
        if(origPath[1][0] == targetX && origPath[1][1] == targetY){
            return true;
        }
        return false;
    }

    public PurePursuit(double targetX, double targetY) {

        //this.origPath = doubleArrayCopy(path);

        // ^ copy array from PATH into INPUTPATH, essentially creating an implementable constructors

        this.pAlpha = 0.7;
        this.pBeta = 0.3;
        this.pTolerance = 0.0000001;

        this.vAlpha = 0.1;
        this.vBeta = 0.3;
        this.vTolerance = 0.0000001;

        //this.encoderRotationRight = encoderRotationRight;
        //this.encoderRotationLeft = encoderRotationLeft;
        this.targetX = targetX;
        this.targetY = targetY;

        // odometry to create x/y for original path

        double[][] path = new double[2][2];
        path[0][0] = 0; path[0][1] = 0;
        path[1][0] = targetX; path[1][1] = targetY;

        this.origPath = doubleArrayCopy(path);
    }

    public static void print(double[] path){ // printing framework for basic double arrays. Makes things easy to comprehend!
        for(double u : path){
            System.out.println(u);
        }
    }

    public static double[][] doubleArrayCopy(double[][] arr) // Method for copying matrices, something that comes up quite often. Credit to KHEngineering for method.
    {

        //size first dimension of array
        double[][] temp = new double[arr.length][arr[0].length];

        for(int i=0; i<arr.length; i++)
        {
            //Resize second dimension of array
            temp[i] = new double[arr[i].length];

            //Copy Contents
            for(int j=0; j<arr[i].length; j++)
                temp[i][j] = arr[i][j];
        }

        return temp;

    }

    public double[][] inject(double[][] orig, int numToInject)
    {
        double[][] morePoints = new double[orig.length + ((numToInject)*(orig.length-1))][2];

        int index = 0;

        //loop through original array
        for(int i=0; i<orig.length-1; i++)
        {
            //copy first
            morePoints[index][0] = orig[i][0];
            morePoints[index][1] = orig[i][1];
            index++;

            for(int j=1; j<numToInject+1; j++)
            {
                //calculate intermediate x points between j and j+1 original points
                morePoints[index][0] = j*((orig[i+1][0]-orig[i][0])/(numToInject+1))+orig[i][0];

                //calculate intermediate y points  between j and j+1 original points
                morePoints[index][1] = j*((orig[i+1][1]-orig[i][1])/(numToInject+1))+orig[i][1];

                index++;
            }
        }

        //copy last
        morePoints[index][0] = orig[orig.length-1][0];
        morePoints[index][1] = orig[orig.length-1][1];

        return morePoints;
    }

    public double[][] smoother(double[][] path, double weight_data, double weight_smooth, double tolerance)
    {

        //copy array
        double[][] newPath = doubleArrayCopy(path);

        double change = tolerance;
        while(change >= tolerance)
        {
            change = 0.0;
            for(int i=1; i<path.length-1; i++)
                for(int j=0; j<path[i].length; j++)
                {
                    double aux = newPath[i][j];
                    newPath[i][j] += weight_data * (path[i][j] - newPath[i][j]) + weight_smooth * (newPath[i-1][j] + newPath[i+1][j] - (2.0 * newPath[i][j]));
                    change += Math.abs(aux - newPath[i][j]);
                }
        }

        return newPath;

    }

    public static double[][] nodeOnlyWayPoints(double[][] path)
    {

        // this function takes a list of coordinates and returns a double matrix with coordinates of the original list that have a change in velocity

        List<double[]> li = new LinkedList<double[]>();

        li.add(path[0]);

        for(int i=1; i<path.length-1; i++)
        {
            //calculate direction - Math.atan2 function returns the theta component of polar coordinates.
            double vector1 = Math.atan2((path[i][1]-path[i-1][1]),path[i][0]-path[i-1][0]);
            double vector2 = Math.atan2((path[i+1][1]-path[i][1]),path[i+1][0]-path[i][0]);

            if(Math.abs(vector2-vector1)>=0.01)
                li.add(path[i]);
        }

        li.add(path[path.length-1]);

        // put the updated coords into an array
        double[][] temp = new double[li.size()][2];

        for (int i = 0; i < li.size(); i++)
        {
            temp[i][0] = li.get(i)[0];
            temp[i][1] = li.get(i)[1];
        }

        return temp;
    }

    double[][] velocity(double[][] smoothPath, double timeStep)
    {
        double[] dxdt = new double[smoothPath.length];
        double[] dydt = new double[smoothPath.length];
        double[][] velocity = new double[smoothPath.length][2];

        //set first instance to zero
        dxdt[0]=0;
        dydt[0]=0;
        velocity[0][0]=0;
        velocity[0][1]=0;
        heading[0][1]=0;

        for(int i=1; i<smoothPath.length; i++)
        {
            dxdt[i] = (smoothPath[i][0]-smoothPath[i-1][0])/timeStep;
            dydt[i] = (smoothPath[i][1]-smoothPath[i-1][1])/timeStep;

            //create time vector
            velocity[i][0]=velocity[i-1][0]+timeStep;
            heading[i][0]=heading[i-1][0]+timeStep;

            //calculate velocity
            velocity[i][1] = Math.sqrt(Math.pow(dxdt[i],2) + Math.pow(dydt[i],2));
        }


        return velocity;

    }

    public double[][] velocityFix(double[][] smoothVelocity, double[][] origVelocity, double tolerance)
    {

        /*pseudo
         * 1. Find Error Between Original Velocity and Smooth Velocity
         * 2. Keep increasing the velocity between the first and last node of the smooth Velocity by a small amount
         * 3. Recalculate the difference, stop if threshold is met or repeat step 2 until the final threshold is met.
         * 3. Return the updated smoothVelocity
         */

        //calculate error difference
        double[] difference = errorSum(origVelocity,smoothVelocity);


        //copy smooth velocity into new Vector
        double[][] fixVel = new double[smoothVelocity.length][2];

        for (int i=0; i<smoothVelocity.length; i++)
        {
            fixVel[i][0] = smoothVelocity[i][0];
            fixVel[i][1] = smoothVelocity[i][1];
        }

        //optimize velocity by minimizing the error distance at the end of travel
        //when this converges, the fixed velocity vector will be smooth, start
        //and end with 0 velocity, and travel the same final distance as the original
        //un-smoothed velocity profile
        double increase = 0.0;
        while (Math.abs(difference[difference.length-1]) > tolerance)
        {
            increase = difference[difference.length-1]/1/50;

            for(int i=1;i<fixVel.length-1; i++)
                fixVel[i][1] = fixVel[i][1] - increase;

            difference = errorSum(origVelocity,fixVel);
        }

        //fixVel =  smoother(fixVel, 0.001, 0.001, 0.0000001);
        return fixVel;

    }

    private double[] errorSum(double[][] origVelocity, double[][] smoothVelocity)
    {
        //copy vectors
        double[] tempOrigDist = new double[origVelocity.length];
        double[] tempSmoothDist = new double[smoothVelocity.length];
        double[] difference = new double[smoothVelocity.length];


        double timeStep = origVelocity[1][0]-origVelocity[0][0];

        //copy first elements
        tempOrigDist[0] = origVelocity[0][1];
        tempSmoothDist[0] = smoothVelocity[0][1];


        //calculate difference
        for (int i=1; i<origVelocity.length; i++)
        {
            tempOrigDist[i] = origVelocity[i][1]*timeStep + tempOrigDist[i-1];
            tempSmoothDist[i] = smoothVelocity[i][1]*timeStep + tempSmoothDist[i-1];

            difference[i] = tempSmoothDist[i]-tempOrigDist[i];

        }

        return difference;
    }

    public int[] injectionCounter2Steps(double numNodeOnlyPoints, double maxTimeToComplete, double timeStep)
    {
        int first = 0;
        int second = 0;
        int third = 0;

        double oldPointsTotal = 0;

        numFinalPoints  = 0;

        int[] ret = null;

        double totalPoints = maxTimeToComplete/timeStep;

        if (totalPoints < 100)
        {
            double pointsFirst = 0;
            double pointsTotal = 0;


            for (int i=4; i<=6; i++)
                for (int j=1; j<=8; j++)
                {
                    pointsFirst = i *(numNodeOnlyPoints-1) + numNodeOnlyPoints;
                    pointsTotal = (j*(pointsFirst-1)+pointsFirst);

                    if(pointsTotal<=totalPoints && pointsTotal>oldPointsTotal)
                    {
                        first=i;
                        second=j;
                        numFinalPoints=pointsTotal;
                        oldPointsTotal=pointsTotal;
                    }
                }

            ret = new int[] {first, second, third};
        }
        else
        {

            double pointsFirst = 0;
            double pointsSecond = 0;
            double pointsTotal = 0;

            for (int i=1; i<=5; i++)
                for (int j=1; j<=8; j++)
                    for (int k=1; k<8; k++)
                    {
                        pointsFirst = i *(numNodeOnlyPoints-1) + numNodeOnlyPoints;
                        pointsSecond = (j*(pointsFirst-1)+pointsFirst);
                        pointsTotal =  (k*(pointsSecond-1)+pointsSecond);

                        if(pointsTotal<=totalPoints)
                        {
                            first=i;
                            second=j;
                            third=k;
                            numFinalPoints=pointsTotal;
                        }
                    }

            ret = new int[] {first, second, third};
        }


        return ret;
    }

    public void leftRight(double[][] smoothPath, double robotTrackWidth)
    {

        double[][] leftPath = new double[smoothPath.length][2];
        double[][] rightPath = new double[smoothPath.length][2];

        double[][] gradient = new double[smoothPath.length][2];

        for(int i = 0; i<smoothPath.length-1; i++)
            gradient[i][1] = Math.atan2(smoothPath[i+1][1] - smoothPath[i][1],smoothPath[i+1][0] - smoothPath[i][0]);

        gradient[gradient.length-1][1] = gradient[gradient.length-2][1];


        for (int i=0; i<gradient.length; i++)
        {
            leftPath[i][0] = (robotTrackWidth/2 * Math.cos(gradient[i][1] + Math.PI/2)) + smoothPath[i][0];
            leftPath[i][1] = (robotTrackWidth/2 * Math.sin(gradient[i][1] + Math.PI/2)) + smoothPath[i][1];

            rightPath[i][0] = robotTrackWidth/2 * Math.cos(gradient[i][1] - Math.PI/2) + smoothPath[i][0];
            rightPath[i][1] = robotTrackWidth/2 * Math.sin(gradient[i][1] - Math.PI/2) + smoothPath[i][1];

            //convert to degrees 0 to 360 where 0 degrees is +X - axis, accumulated to aline with WPI sensor
            double deg = Math.toDegrees(gradient[i][1]);

            gradient[i][1] = deg;

            if(i>0)
            {
                if((deg-gradient[i-1][1])>180)
                    gradient[i][1] = -360+deg;

                if((deg-gradient[i-1][1])<-180)
                    gradient[i][1] = 360+deg;
            }
        }

        this.heading = gradient;
        this.leftPath = leftPath;
        this.rightPath = rightPath;
    }

    public void calculate(double totalTime, double timeStep, double robotTrackWidth)
    {


        //first find only direction changing nodes
        nodeOnlyPath = nodeOnlyWayPoints(origPath);

        //Figure out how many nodes to inject
        int[] inject = injectionCounter2Steps(nodeOnlyPath.length, totalTime, timeStep); // what exactly do {f, s, t} represent? what are they used for?

        //iteratively inject and smooth the path
        for(int i=0; i<inject.length; i++)
        {
            if(i==0) //runs once, can it be implemented before the for loop
            {
                smoothPath = inject(nodeOnlyPath,inject[0]);
                smoothPath = smoother(smoothPath, pAlpha, pBeta, pTolerance);
            }
            else
            {
                smoothPath = inject(smoothPath,inject[i]);
                smoothPath = smoother(smoothPath, 0.1, 0.3, 0.0000001);
            }
        }

        //calculate left and right path based on center path
        leftRight(smoothPath, robotTrackWidth);

        origCenterVelocity = velocity(smoothPath, timeStep);
        origLeftVelocity = velocity(leftPath, timeStep);
        origRightVelocity = velocity(rightPath, timeStep);

        //copy smooth velocities into fix Velocities
        smoothCenterVelocity =  doubleArrayCopy(origCenterVelocity);
        smoothLeftVelocity =  doubleArrayCopy(origLeftVelocity);
        smoothRightVelocity =  doubleArrayCopy(origRightVelocity);

        //set final vel to zero
        smoothCenterVelocity[smoothCenterVelocity.length-1][1] = 0.0;
        smoothLeftVelocity[smoothLeftVelocity.length-1][1] = 0.0;
        smoothRightVelocity[smoothRightVelocity.length-1][1] = 0.0;

        //Smooth velocity with zero final V
        smoothCenterVelocity = smoother(smoothCenterVelocity, vAlpha, vBeta, vTolerance);
        smoothLeftVelocity = smoother(smoothLeftVelocity, vAlpha, vBeta, vTolerance);
        smoothRightVelocity = smoother(smoothRightVelocity,vAlpha, vBeta, vTolerance);

        //fix velocity distance error
        smoothCenterVelocity = velocityFix(smoothCenterVelocity, origCenterVelocity, 0.0000001);
        smoothLeftVelocity = velocityFix(smoothLeftVelocity, origLeftVelocity, 0.0000001);
        smoothRightVelocity = velocityFix(smoothRightVelocity, origRightVelocity, 0.0000001);

        for(int i = 0; i < smoothRightVelocity.length; i++){
            for(int j = 0; j < smoothRightVelocity[0].length; j++){
                System.out.println(smoothRightVelocity[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        /*
        final PurePursuit path = new PurePursuit(12, 15);
        path.calculate(8, 0.1, 2.5); // TODO: find exact width
         */
    }
}