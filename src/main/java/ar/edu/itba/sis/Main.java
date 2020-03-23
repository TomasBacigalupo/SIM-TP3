package ar.edu.itba.sis;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.List;
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
    /*

    L = 0.5m

    N partículas chicas de R1 = 0.005m m1 = 0.1g
    1 partícula  grande de R2 = 0.05m  m2 = 100g

    Las posiciones de todas las partículas deben ser al azar con distribución uniforme dentro del dominio. 
    Las partículas pequeñas deben tener velocidades con una distribución uniforme en el rango: |v| < 0.1 m/s.

    La partícula grande debe tener velocidad inicial v2 = 0 y su posición inicial en x2=L/2, y2=L/2.

    */
    		Board board = new Board();
    		ParticleGenerator sg = new ParticleGenerator();
    		//int N = 0;
    		double T = 0;
    		@SuppressWarnings("unused")
    		List <Particle> particles = sg.generate(N,T);
    		
    		while(!board.end()) {
    			double tc = board.tc();
    			board.update(tc);
    			board.collision();
    		}
    	}
        
    }



