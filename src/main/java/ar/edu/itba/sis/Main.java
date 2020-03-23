package ar.edu.itba.sis;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class Main
{
    public static void main( String[] args ) throws IOException {

        double L = 0;
        int N = 0;

        InputStream input = new FileInputStream("src/config.properties");
        Properties prop = new Properties();
        prop.load(input);
        N = new Integer(prop.getProperty("sim.N"));
        L = new Integer(prop.getProperty("dim.L"));


        System.out.println( "(L="+L+", "+"N="+N+")");




    }
}
