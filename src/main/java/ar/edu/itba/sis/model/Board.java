package ar.edu.itba.sis.model;


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
	
	public double temperature() {
		int i = 0;
		double ret = 0;
		for(Particle particle : particles) {
			double module = particle.getVelocity().module();
			ret += 0.5*particle.getMass()*module*module;
			i++;
		}
		return ret/i;
	}
	
	public boolean end() {
		double r = big_particle.getRadius();
		return big_particle.getPosition().x - r <= left || big_particle.getPosition().x + r >= right || big_particle.getPosition().y + r >= up || big_particle.getPosition().y - r <= down;
	}
	
	public double tc() {
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
	public void update(double tc) {
		for (Particle particle : particles) {
			particle.getPosition().x = particle.getPosition().x + particle.getVelocity().x*tc;
			particle.getPosition().y = particle.getPosition().y + particle.getVelocity().y*tc;
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
			if(p.getPosition().x == left || p.getPosition().x == right) {
				p.vertical_collision();
			}
			if(p.getPosition().y == up || p.getPosition().y == down) {
				p.horizontal_collision();
			}
		}
		
	}
	
	public void collision() {
		for(Particle[] pair : matches) {
			pair[0].collision(pair[1]);
		}
		
	}
}
