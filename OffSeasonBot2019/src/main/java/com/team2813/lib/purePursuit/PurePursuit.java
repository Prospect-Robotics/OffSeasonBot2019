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
}
