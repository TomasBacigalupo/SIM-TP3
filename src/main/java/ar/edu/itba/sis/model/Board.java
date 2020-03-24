package ar.edu.itba.sis.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    int N;
	double L;
	List<Particle> particles;
	List<Particle[]> matches;
	Particle big_particle;
	
	double right;
	double left;
	double up;
	double down;

    public Board(double L, int N) {
        this.L = L;
        this.N = N;
        //TODO ver lo de la temperatura
        particles = generateRandomParticles(this.N,0);
    }

    public List <Particle> generateRandomParticles(int N , double T){
        Random rand = new Random();
        List<Particle> particles = new ArrayList<>(N+1);
        for(int i = 0 ; i < N ; i++) {
            double x =  rand.nextDouble()*L;
            double y =  rand.nextDouble()*L;
            //Falta chequear que se NO superpongan
            double vx = 0;
            double vy = 0;
            //double vx = rand.nextDouble()*L;
            //double vy = rand.nextDouble()*L;
            //|v| < 0.1 m/s
            //Falta chequear que la Temperatura sea igual a T
            particles.add(new Particle(i,new Vector(x,y),new Vector(vx,vy),0.005,0.1));
        }
        particles.add(new Particle(N+1,new Vector(L/2,L/2),new Vector(0,0),0.05,100));
        return particles;
    }

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

	public String toOvito(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.particles.size()+4);
        sb.append("\n");
        sb.append("comment");
        sb.append("\n");
        sb.append(this.sides());
        for (Particle particle : this.particles){
            sb.append(particle.toOvito());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String sides(){
        StringBuilder sides = new StringBuilder();
        sides.append(new Particle(-1,new Position(L,L)).toOvito());
        sides.append("\n");
        sides.append(new Particle(-2,new Position(L,0)).toOvito());
        sides.append("\n");
        sides.append(new Particle(-3,new Position(0,L)).toOvito());
        sides.append("\n");
        sides.append(new Particle(-4,new Position(0,0)).toOvito());
        sides.append("\n");

        return sides.toString();
    }
}
