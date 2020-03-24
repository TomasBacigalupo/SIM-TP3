package ar.edu.itba.sis;

import ar.edu.itba.sis.model.Board;
import ar.edu.itba.sis.model.Particle;
import ar.edu.itba.sis.model.ParticleGenerator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Main
{
    public static void main( String[] args ) throws IOException {

        double L = 0;

        int N = 0;

        InputStream input = new FileInputStream("src/resources/config.properties");
        Properties prop = new Properties();
        prop.load(input);
        N = new Integer(prop.getProperty("sim.N"));
        L = new Double(prop.getProperty("sim.L"));

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
        List<Particle> particles = sg.generate(N,T);
        StringBuilder sb = new StringBuilder();
        sb.append(particles.size());
        sb.append("\n");
        sb.append("comment");
        sb.append("\n");
        for (Particle particle : particles){
            sb.append(particle.toOvito());
            sb.append("\n");
        }

        while(!board.end()) {
            double tc = board.tc();
            board.update(tc);
            board.collision();
        }
    }

}



