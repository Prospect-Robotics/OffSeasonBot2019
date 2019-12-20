package com.team2813.lib.purePursuit;


import edu.wpi.first.wpilibj.Timer;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static java.awt.geom.Point2D.*;

public class PurePursuit {
    public double constrain(double value, double min, double max){
        return Math.min(Math.max(value, min), max);
    }
    // finds coordinate location of the robot
    public Point2D getLocation(double rightEncoderVal, double leftEncoderVal) {
        double currentRightVal = 0.0;
        double currentLeftVal = 0.0;
        double robot_angle = 0.0; //radians
        Point2D location = new Point2D.Double();
        double deltaRightVal = currentRightVal - rightEncoderVal;
        double deltaLeftVal = currentLeftVal - leftEncoderVal;
        double dist = (deltaRightVal + deltaLeftVal) / 2;
        double xVal = dist * Math.cos(robot_angle);
        double yVal = dist * Math.sin(robot_angle);
        location.setLocation(xVal, yVal);
        return location;
    }

    //injects points
    public ArrayList<Point2D> injectPoints(ArrayList<Point2D> path, double spacing) {
        int numSegments = path.size() - 1;
        for (int i = 0; i < path.size(); i++) {
            if (numSegments != 0) {
                Point2D startPosition = path.get(i);
                Point2D endPosition = path.get(i + 1);
                double xDif = endPosition.getX() - startPosition.getX();
                double yDif = endPosition.getY() - startPosition.getY();
                Point2D vector = new Point2D.Double();
                vector.setLocation(xDif, yDif);
                double vectorMagnitude = Math.sqrt((vector.getX() * vector.getX()) + (vector.getY() * vector.getY()));
                double numPoints = Math.ceil(Math.sqrt(vectorMagnitude / spacing));
                Point2D unitVector = new Point2D.Double();
                unitVector.setLocation(xDif / vectorMagnitude * spacing, yDif / vectorMagnitude * spacing);
                Point2D newPoint = new Point2D.Double();
                for (int j = 0; j < numPoints; j++) {
                    newPoint.setLocation(startPosition.getX() + unitVector.getX() * j, startPosition.getY() +
                            unitVector.getY() * j);
                    path.add(i + j + 1, newPoint);
                }
                i += numPoints;
                numSegments -= 1;
            }
        }
        return path;
    }

    //smooths path
    public ArrayList<Point2D> smoother(ArrayList<Point2D> path, double a, double b, double tolerance) {
        double change = tolerance;
        double newX = 0.0;
        double newY = 0.0;
        double x;
        double y;
        ArrayList<Point2D> newPath = new ArrayList<Point2D>();
        for (int i = 0; i < path.size(); i++) {
            newPath.add(path.get(i));
        }
        while (change >= tolerance) {
            change = 0.0;
            for (int i = 1; i < path.size() - 1; i++) {
                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        x = path.get(i).getX();
                        double aux = newPath.get(i).getX();
                        newX = newPath.get(i).getX();
                        newX += a * (x - aux) + b * (newPath.get(i - 1).getX() + newPath.get(i + 1).getX()) -
                                (2.0 * aux);
                        change += Math.abs(aux - newX);
                    }
                    if (j == 1) {
                        y = path.get(i).getY();
                        double aux = newPath.get(i).getY();
                        newY = newPath.get(i).getY();
                        newY += a * (y - aux) + b * (newPath.get(i - 1).getY() + newPath.get(i + 1).getY()) -
                                (2.0 * aux);
                        change += Math.abs(aux - newY);
                    }
                }
                newPath.get(i).setLocation(newX, newY);
            }
        }
        return newPath;
    }

    //calculates the distance between points
    public double[] distancePoints(ArrayList<Point2D> path) {
        double xDif;
        double yDif;
        double[] distancePoints = new double[path.size()];
        xDif = path.get(0).getX() - path.get(1).getX();
        yDif = path.get(0).getY() - path.get(1).getY();
        distancePoints[0] = Math.sqrt((xDif * xDif) + (yDif * yDif));
        for (int i = 1; i < path.size(); i++) {
            xDif = path.get(i).getX() - path.get(i - 1).getX();
            yDif = path.get(i).getY() - path.get(i - 1).getY();
            distancePoints[i] = distancePoints[i - 1] + Math.sqrt((xDif * xDif) + (yDif * yDif));
        }
        return distancePoints;
    }

    //finds the curvature of the path at a given point
    public double findCurvature(Point2D P, Point2D Q, Point2D R) {
        double x1 = P.getX() + 0.001;
        double y1 = P.getY();
        double x2 = Q.getX();
        double y2 = Q.getY();
        double x3 = R.getX();
        double y3 = R.getY();
        double k1 = 0.5 * (((x1 * x1) + (y1 * y1)) - ((x2 * x2) + (y2 * y2))) / (x1 - x2);
        double k2 = (y1 - y2) / (x1 - x2);
        double b = 0.5 * ((x2 * x2) - (2 * x2 * k1) + (y2 * y2) - (x3 * x3) + (2 * x3 * k1) -
                (y3 * y3)) / (x3 * k2 - y3 + y2 - x2 * k2);
        double a = k1 - k2 * b;
        double r = Math.sqrt(((x1 - a) * (x1 - a)) + ((y1 - b) * (y1 - b)));
        double curvature = 1 / r;
        return curvature;
    }

    //returns an array with the curvature of the path at each point
    public double[] findPathCurvature(ArrayList<Point2D> path) {
        double[] pathCurvature = new double[path.size()];
        pathCurvature[0] = 0.0;
        for (int i = 1; i < path.size() - 1; i++) {
            pathCurvature[i] = findCurvature(path.get(i), path.get(i - 1), path.get(i + 1));
        }
        pathCurvature[path.size() - 1] = 0.0;
        return pathCurvature;
    }

    //returns an array with target velocities for each point
    public double[] pathVelocity(double[] pathCurvature, double maxVelocity, double minVelocity, double k) {
        double min;
        double[] pathVelocity = new double[pathCurvature.length];
        for (int i = 0; i < pathVelocity.length; i++) {
            min = k / pathCurvature[i];
            try {
                min = k / pathCurvature[i];
            } catch (ArithmeticException e) {
                min = minVelocity;
            }
            if (maxVelocity > min) {
                pathVelocity[i] = min;
            } else if (maxVelocity < min) {
                pathVelocity[i] = maxVelocity;
            } else {
                pathVelocity[i] = min;
            }
        }
        return pathVelocity;
    }
    //Part 3: calculates target robot path velocities for each point given the velocity setpoints above run through a rate limiter
    public double[] targetVelocity(double[] pathVelocity, double maxAcceleration, ArrayList<Point2D> path) {
        double xDif;
        double yDif;
        double distancePoints;
        double max;
        double[] distance = distancePoints(path);
        double[] targetVelocity = new double[pathVelocity.length];
        double[] maxVelocity = new double[pathVelocity.length];
        for (int i = 1; i > targetVelocity.length; i++) {
            //find maximum reachable velocities
            max = Math.sqrt(Math.pow(pathVelocity[i - 1], 2) + 2 * maxAcceleration * distance[i]);
            maxVelocity[i] = max;
        }
        targetVelocity[targetVelocity.length - 1] = 0;
        for (int i = targetVelocity.length - 2; i > 0; i --){
            xDif = path.get(i).getX() - path.get(i + 1).getX();
            yDif = path.get(i).getY() - path.get(i + 1).getY();
            distancePoints = Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));
            targetVelocity[i] = Math.min(pathVelocity[i], Math.sqrt(Math.pow(targetVelocity[i + 1], 2) +
                    2 * maxAcceleration * distancePoints));
        }
        return targetVelocity;
    }

    //smooths the transition in velocity by limiting the rates
    public double[] rateLimiter(double[] targetVelocity, double maxRateOfChange) {
        double changeInTime = 0.0;
        double maxChange = 0.0;
        double[] output = new double[targetVelocity.length];
        changeInTime = Timer.getFPGATimestamp();
        maxChange = changeInTime * maxRateOfChange;
        output[0] = constrain(targetVelocity[0], -maxChange, maxChange);
        targetVelocity[0] += output[0];
        for(int i = 1; i < targetVelocity.length; i++){
            changeInTime = Timer.getFPGATimestamp();
            maxChange = changeInTime * maxRateOfChange;
            output[i] = constrain(targetVelocity[i] - output[i - 1], -maxChange, maxChange);
            targetVelocity[i] += output[i];
        }
        return targetVelocity;
    }
    public Point2D closestPoint(ArrayList<Point2D> path, int indexLastPoint, double rightEncoderVal,
                                double leftEncoderVal){
        Point2D closestPoint = new Point2D.Double();
        ArrayList<Point2D> searchPath = new ArrayList<Point2D>();
        for(int i = indexLastPoint + 1; i < path.size(); i++){
            searchPath.add(path.get(i));
        }
        Point2D location = new Point2D.Double();
        location = getLocation(rightEncoderVal, leftEncoderVal);
        double xDif;
        double yDif;
        double[] distance = new double[searchPath.size()];
        for(int i = 0; i < searchPath.size(); i++){
            xDif = location.getX() - searchPath.get(i).getX();
            yDif = location.getY() - searchPath.get(i).getY();
            distance[i] = Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));
        }
        int index = 0;
        for(int i = 0; i < distance.length; i++){
            if(distance[index] > distance[i]){
                index = i;
            }
        }
        closestPoint = searchPath.get(index);
        return closestPoint;
    }
    //finds look ahead point
    public Point2D lookAheadPoint(Point2D e, Point2D l, Point2D c, double r){
        double t = Timer.getFPGATimestamp();
        Point2D d = new Point2D.Double();
        Point2D f = new Point2D.Double();
        Point2D intersection = new Point2D.Double();
        d.setLocation(l.getX() - e.getX(), l.getY() - e.getY());
        f.setLocation(e.getX() - c.getX(), e.getY() - c.getY());
        double a = Math.pow(d.getX(), 2) + Math.pow(d.getY(), 2);
        double b = 2 * ((f.getX() * d.getX()) + (f.getY() * d.getY()));
        double dc = Math.pow(f.getX(), 2) + Math.pow(f.getY(), 2) - Math.pow(r, 2);
        double discriminant = Math.pow(b, 2) - (4 * a * dc);
        if(discriminant < 0){
            return intersection;
        }
        else{
            discriminant = Math.sqrt(discriminant);
            double t1 = (-b - discriminant)/(2 * a);
            double t2 = (-b + discriminant)/(2 * a);
            if(t1 >= 0 && t1 <= 1){
                intersection.setLocation(e.getX() + t1 * d.getX(), e.getY() + t1 * d.getX());
                return intersection;
            }
            else if(t2 >= 0 && t2 <= 1){
                intersection.setLocation(e.getX() + t2 * d.getX(), e.getY() + t2 * d.getX());
                return intersection;
            }
            else{
                return intersection;
            }
        }
    }
    //finds curvature of arc to look ahead point
    public double lookAheadCurvature(Point2D lookAheadPoint, double l, double r, double rightEncoderVal,
                                     double leftEncoderVal){
        double curvature = (2 * lookAheadPoint.getX())/Math.pow(l, 2);
        double robotAngle;
        Point2D robotLocation = new Point2D.Double();
        robotLocation = getLocation(rightEncoderVal, leftEncoderVal);
        robotAngle = Math.atan((lookAheadPoint.getY() - robotLocation.getY())/(lookAheadPoint.getX() -
                robotLocation.getX()));
        double a = -Math.tan(robotAngle);
        double c = Math.tan(robotAngle) * robotLocation.getX() - robotLocation.getY();
        double d = Math.abs((a * lookAheadPoint.getX()) + lookAheadPoint.getY() + c)/
                Math.sqrt(Math.pow(a, 2) + 1);
        double side = Math.signum(Math.sin(robotAngle) * (lookAheadPoint.getX() - robotLocation.getX()) -
                Math.cos(robotAngle) * (lookAheadPoint.getY() - robotLocation.getY()));
        curvature *= side;
        return curvature;
    }
}
