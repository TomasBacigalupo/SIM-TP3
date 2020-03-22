package ar.edu.itba.sis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main
{
    public static void main( String[] args ) throws FileNotFoundException {
        double L = 0;
        int N = 0;
        File config = new File("./config.txt");
        Scanner scn = new Scanner(config);
        while (scn.hasNextLine()){
            String line = scn.nextLine();
            String[] params = line.split(" ");
            String variable = params[0];
            if (variable.compareTo("L=") == 0){
                L = new Double(params[1]);
            }else if (variable.compareTo("N=")== 0){
                N = new Integer(params[1]);
            }
        }

        System.out.println( "(L="+L+", "+"N="+N+")");
    }
}
