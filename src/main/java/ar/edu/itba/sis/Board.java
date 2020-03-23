package ar.edu.itba.sis;

import java.util.List;

public class Board {
	double L;
	List<Particle> particles;
	List<Particle[]> matches;
	Particle big_particle;
	
	double right;
	double left;
	double up;
	double down;
	
	boolean end() {
		double r = big_particle.radius;
		return big_particle.position.x - r <= left || big_particle.position.x + r >= right || big_particle.position.y + r >= up || big_particle.position.y - r <= down;
	}
	
	double tc() {
		double min = 0;
		double aux = 0;
		for (Particle p : particles) {
			for(Particle q : particles) {
				aux = p.tc(q);
				if(min > aux) {
					min = aux;
				}
			}
			aux = p.tc_vertical_wall(left,right);
			if(min > aux) {
				min = aux;
			}
			aux = p.tc_horizontal_wall(up,down);
			if(min > aux) {
				min = aux;
			}
		}
		return min;
	}
	
	@SuppressWarnings("null")
	void update(double tc) {
		for (Particle p : particles) {
			p.position.x = p.position.x + p.velocity.x*tc;
			p.position.y = p.position.y + p.velocity.y*tc;
		}
		for(Particle p : particles) {
			for(Particle q : particles) {
				if(p.equals(q)){
					Particle [] r = null;
					r[0] = p;
					r[1] = q;
					matches.add(r);
				}
			}
			if(p.position.x == left || p.position.x == right) {
				p.vertical_collision();
			}
			if(p.position.y == up || p.position.y == down) {
				p.horizontal_collision();
			}
		}
		
	}
	
	void collision() {
		for(Particle[] pair : matches) {
			pair[0].collision(pair[1]);
		}
		
	}
}
