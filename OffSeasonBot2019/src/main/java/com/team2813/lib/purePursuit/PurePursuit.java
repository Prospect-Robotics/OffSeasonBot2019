package com.team2813.lib.purePursuit;


import java.awt.geom.Point2D;
import java.util.ArrayList;

import static java.awt.geom.Point2D.*;

public class PurePursuit {
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
        for(int i = 0; i < path.size(); i++){
            if(numSegments != 0){
                Point2D startPosition = path.get(i);
                Point2D endPosition = path.get(i + 1);
                double xDif = endPosition.getX() - startPosition.getX();
                double yDif = endPosition.getY() - startPosition.getY();
                Point2D vector = new Point2D.Double();
                vector.setLocation(xDif, yDif);
                double vectorMagnitude = Math.sqrt((vector.getX() * vector.getX()) + (vector.getY() * vector.getY()));
                double numPoints = Math.ceil(Math.sqrt(vectorMagnitude/spacing));
                Point2D unitVector = new Point2D.Double();
                unitVector.setLocation(xDif/vectorMagnitude * spacing, yDif/vectorMagnitude * spacing);
                Point2D newPoint = new Point2D.Double();
                for(int j = 0; j < numPoints; j++){
                    newPoint.setLocation(startPosition.getX() + unitVector.getX() * j, startPosition.getY() + unitVector.getY() * j);
                    path.add(i + j + 1, newPoint);
                }
                i += numPoints;
                numSegments -= 1;
            }
        }
        return path;
    }
    public ArrayList<Point2D> smoother(ArrayList<Point2D> path, double a, double b, double tolerance){
        double change = tolerance;
        double newX = 0.0;
        double newY = 0.0;
        double x;
        double y;
        ArrayList<Point2D> newPath = new ArrayList<Point2D>();
        for(int i = 0; i < path.size(); i++){
            newPath.add(path.get(i));
        }
        while(change >= tolerance){
            change = 0.0;
            for(int i = 1; i < path.size() - 1; i++){
                for(int j = 0; j < 2; j++){
                    if(j == 0){
                        x = path.get(i).getX();
                        double aux = newPath.get(i).getX();
                        newX = newPath.get(i).getX();
                        newX += a * (x - aux) + b * (newPath.get(i - 1).getX() + newPath.get(i + 1).getX()) - (2.0 * aux);
                        change += Math.abs(aux - newX);
                    }
                    if(j == 1){
                        y = path.get(i).getY();
                        double aux = newPath.get(i).getY();
                        newY = newPath.get(i).getY();
                        newY += a * (y - aux) + b * (newPath.get(i - 1).getY() + newPath.get(i + 1).getY()) - (2.0 * aux);
                        change += Math.abs(aux - newY);
                    }
                }
                newPath.get(i).setLocation(newX, newY);
            }
        }
        return newPath;
    }
}
