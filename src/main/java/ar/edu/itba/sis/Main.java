package ar.edu.itba.sis;

import ar.edu.itba.sis.model.Board;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main{
    public static void main( String[] args ) throws IOException {
        
    	int N;
        double L;
        double Tmin;
        double Tmax;
        double R1;
        double M1;
        double R2;
        double M2;
        double V;
        
        //set variables from config.properties
        
        InputStream input = new FileInputStream("src/resources/config.properties");
        Properties prop = new Properties();
        prop.load(input);
        
        N = new Integer(prop.getProperty("sim.N"));
        L = new Double(prop.getProperty("sim.L"));
        Tmin = new Double(prop.getProperty("sim.Tmin"));
        Tmax = new Double(prop.getProperty("sim.Tmax"));
        R1 = new Double(prop.getProperty("sim.R1"));
        M1 = new Double(prop.getProperty("sim.M1"));
        R2 = new Double(prop.getProperty("sim.R2"));
        M2 = new Double(prop.getProperty("sim.M2"));
        V = new Double(prop.getProperty("sim.V0MAX"));

        Board board = new Board(N,L,Tmin,Tmax,R1,M1,R2,M2,V);
        
        StringBuilder simulacion = new StringBuilder();
        
        double tc = board.tc();
        System.out.println(tc);
        simulacion.append(board.toOvito());
        int i = 0;
        while(!board.end() && i < 200){
        	tc = board.tc();
            board.update(tc);
            board.collision();
            simulacion.append(board.toOvito());
            i++;
        }

        
        FileWriter fw = new FileWriter("ovito.txt");
        fw.write(simulacion.toString());
        fw.close();
                
    }

}



