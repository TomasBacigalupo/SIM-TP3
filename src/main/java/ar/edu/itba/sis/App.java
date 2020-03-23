package ar.edu.itba.sis;

import java.util.List;

public class App 
{
	@SuppressWarnings("null")
	public static void main(String[] args) {
		Board board = null;
		
/*

L = 0.5m

N partículas chicas de R1 = 0.005m m1 = 0.1g
1 partícula  grande de R2 = 0.05m  m2 = 100g

Las posiciones de todas las partículas deben ser al azar con distribución uniforme dentro del dominio. 
Las partículas pequeñas deben tener velocidades con una distribución uniforme en el rango: |v| < 0.1 m/s.

La partícula grande debe tener velocidad inicial v2 = 0 y su posición inicial en x2=L/2, y2=L/2.

*/
		
		ParticleGenerator sg = new ParticleGenerator();
		int N = 0;
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
