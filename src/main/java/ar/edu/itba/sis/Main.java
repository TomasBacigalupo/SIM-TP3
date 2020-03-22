package ar.edu.itba.sis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws FileNotFoundException {
        double L = 0;
        File config = new File("./config.txt");
        Scanner scn = new Scanner(config);
        while (scn.hasNextLine()){
            String line = scn.nextLine();
            String[] params = line.split(" ");
            if (params[0].compareTo("L=") == 0){
                L = new Double(params[1]);
            }
        }

        System.out.println( L );
    }
}
