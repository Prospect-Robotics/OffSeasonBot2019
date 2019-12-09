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
    public ArrayList<Point2D> injectPoints(ArrayList<Point2D> path, double spacing){
        for(int i = 0; i < path.size(); i++){
            Point2D startPosition = path.get(i);
            Point2D endPosition = path.get(i + 1);
            double xDif = endPosition.getX() - startPosition.getX();
            double yDif = endPosition.getY() - startPosition.getY();
            Point2D vector = new Point2D.Double();
            vector.setLocation(xDif, yDif);
        }
    }
}
