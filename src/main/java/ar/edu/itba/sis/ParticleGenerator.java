package ar.edu.itba.sis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleGenerator {
	private static final double L = 0.5;
	List <Particle> generate(int N , double T){
		Random rand = new Random();
		List<Particle> l = new ArrayList<Particle>(N+1);
		for(int i = 0 ; i < N ; i++) {
			double x =  rand.nextDouble()*L; 
			double y =  rand.nextDouble()*L;
			double vx = 0;
			double vy = 0;
			//double vx = rand.nextDouble()*L;
			//double vy = rand.nextDouble()*L;
			//|v| < 0.1 m/s
			
			l.add(new Particle(i,new Vector(x,y),new Vector(vx,vy),0.005,0.1));
		}
		l.add(new Particle(N+1,new Vector(L/2,L/2),new Vector(0,0),0.05,100));
		return l;
	}
}
