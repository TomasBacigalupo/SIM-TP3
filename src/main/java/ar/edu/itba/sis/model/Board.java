package ar.edu.itba.sis.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    
	int N;
    double L;
    double Tmin;
    double Tmax;
    double R1;
    double M1;
    double R2;
    double M2;
    double V;
	
	List<Particle> particles;
	List<Particle[]> matches;
	Particle big_particle;
	
	double right;
	double left;
	double up;
	double down;

    public Board(int N,double L,double Tmin , double Tmax,double R1,double M1,double R2,double M2,double V) {
    	this.N = N;
    	this.L = L;
        this.Tmin = Tmin;
        this.Tmax = Tmax;
        this.R1 = R1;
        this.M1 = M1;
        this.R2 = R2;
        this.M2 = M2;
        this.V = V;
        matches = new ArrayList<Particle[]>();
        left = 0;
        right = L;
        up = L;
        down = 0;
        double t;
        do {
        	particles = generateRandomParticles();
        	t = this.temperature();
        }while(!(Tmin < t && t < Tmax));
        
    }

    public List <Particle> generateRandomParticles(){
        Random rand = new Random();
        List<Particle> particles = new ArrayList<>(N+1);
        particles.add(new Particle(0,new Vector(L/2,L/2),new Vector(0,0),R2,M2));
        big_particle = particles.get(0);
        for(int i = 1 ; i < N ; i++) {
        	double x = 0;
            double y = 0;
            boolean overlaps = false;
            do {
        		x =  rand.nextDouble()*L;
        		y =  rand.nextDouble()*L;
            }while(overlaps(new Particle(0,new Vector(x,y),null,R1,0) , particles ) || !inside(new Particle(0,new Vector(x,y),null,R1,0)));
            double vx = rand.nextDouble()*V;
            double vy = rand.nextDouble()*V;
            particles.add(new Particle(i,new Vector(x,y),new Vector(vx,vy),R1,M1));
        }
        return particles;
    }

    private boolean overlaps(Particle other,List<Particle> l) {
		for(Particle p : l) {
			//System.out.println(String.format("Comparing %d" ,p.getId()));
			if(p.overlaps(other)) {
				return true;
			}
			//System.out.println("END");
		}
		return false;
	}

	public double temperature() {
		int i = 0;
		double acum = 0;
		for(Particle particle : particles) {
			double module = particle.getVelocity().module();
			acum += 0.5*particle.getMass()*module*module;
			i++;
		}
		return acum/i;
	}
	
    public boolean hasOverlapping() {
    	int n = particles.size();
    	for(int i = 0 ; i < n ; i++) {
    		for(int j = 0 ; j < n ; j++) {
    			if(particles.get(i).overlaps(particles.get(j)) && i!=j) {
    				//System.out.println(String.format("%d overlaps %s",i,j));
    				return true;
    			}
    		}
    	}
    	return false;
    	
    }
    
    public boolean allInside() {
    	int n = particles.size();
    	for(int i = 0 ; i < n ; i++) {
    		if(!inside(particles.get(i))) {
    			return false;
    		}
    	}
    	return true;
	}
    
	public boolean end() {
		double r = big_particle.getRadius();
		return big_particle.getPosition().x - r <= left || big_particle.getPosition().x + r >= right || big_particle.getPosition().y + r >= up || big_particle.getPosition().y - r <= down;
	}
	
	public boolean inside(Particle p) {
		double x = p.getPosition().x;
		double y = p.getPosition().y;
		double r = p.getRadius();
		return left < x - r && x + r < right && down < y - r && y + r < up ;
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
	
	public void update(double tc) {
		for (Particle particle : particles) {
			particle.getPosition().x = particle.getPosition().x + particle.getVelocity().x*tc;
			particle.getPosition().y = particle.getPosition().y + particle.getVelocity().y*tc;
		}
		for(Particle p : particles) {
			for(Particle q : particles) {
				if(p.equals(q)){
					Particle [] r = new Particle[2];
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
	
	public Vector getBigParticle() {
		return particles.get(0).getPosition();
	}
	
	public String printParticles() {
		StringBuilder ret = new StringBuilder();
		for(Particle p : particles) {
			ret.append(p.toString());
			ret.append("\n");
		}
		return ret.toString();
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
