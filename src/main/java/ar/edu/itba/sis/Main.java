package ar.edu.itba.sis;

import ar.edu.itba.sis.model.Board;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main
{
    public static void main( String[] args ) throws IOException {

        double L;
        int N;
        double tc;

        //set variables from config.properties
        InputStream input = new FileInputStream("src/resources/config.properties");
        Properties prop = new Properties();
        prop.load(input);
        N = new Integer(prop.getProperty("sim.N"));
        L = new Double(prop.getProperty("sim.L"));

        /*
        L = 0.5m
        N partículas chicas de R1 = 0.005m m1 = 0.1g
        1 partícula  grande de R2 = 0.05m  m2 = 100g
        Las posiciones de todas las partículas deben ser al azar con distribución uniforme dentro del dominio.
        Las partículas pequeñas deben tener velocidades con una distribución uniforme en el rango: |v| < 0.1 m/s.
        La partícula grande debe tener velocidad inicial v2 = 0 y su posición inicial en x2=L/2, y2=L/2.
        */

        Board board = new Board(L,N);
        StringBuilder simulacion = new StringBuilder();

        while(!board.end()){
            board.update(board.tc());
            board.collision();
            simulacion.append(board.toOvito());
        }

        FileWriter fw = new FileWriter("ovito.txt");
        fw.write(board.toOvito());
        fw.close();


    }

}



