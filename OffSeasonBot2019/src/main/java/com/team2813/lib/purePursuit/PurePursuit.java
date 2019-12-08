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
    public ArrayList<Point2D> injectPoints(ArrayList<Point2D> path,Point2D startPosition, Point2D endPosition, double spacing){
        Point2D vector = new Point2D.Double();
        double xDif = endPosition.getX() - startPosition.getX();
        double yDif = endPosition.getY() - startPosition.getY();
        vector.setLocation(xDif, yDif);
    }
}
