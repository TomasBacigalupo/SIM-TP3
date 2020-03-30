package ar.edu.itba.sis;

import java.util.LinkedList;

public class Animation {
    public static int WallCrash;
    public static int Crash;
    public static double time;
    public static LinkedList<Double> colisionTimes = new LinkedList<>();
    public static LinkedList<Double> modules = new LinkedList<>();
    public static LinkedList<LinkedList<Double>> boardModules =new LinkedList<>();
    public static LinkedList<Double[]> bpPositionEvents = new LinkedList<>();
}
