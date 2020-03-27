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

        simulacion.append(board.toOvito());
        Animation.WallCrash = 0;
        Animation.Crash = 0;
        Animation.time = 0;
        Animation.colisionTimes.clear();
        double tc;
        while(!board.end()){
            tc = board.tc();
            Animation.colisionTimes.add(tc);
            Animation.time+= tc;
            board.update(tc);
            if(board.getLastCrachA()!= -1){
                board.collision();

            }
            simulacion.append(board.toOvito());
        }

        System.out.println(Animation.Crash);
        System.out.println(Animation.WallCrash);
        System.out.println(Animation.time);


        
        FileWriter fw = new FileWriter("ovito.txt");
        fw.write(simulacion.toString());
        fw.close();

        FileWriter fw2 = new FileWriter("dataTc.txt");
        double tim= 0;
        for (Double d: Animation.colisionTimes) {

            fw2.write(tim+"\n");
            tim+=d;
        }
        fw.close();
                
    }

}



