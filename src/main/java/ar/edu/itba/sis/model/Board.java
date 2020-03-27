package ar.edu.itba.sis.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    
	private int N;
	private double L;
	private double Tmin;
	private double Tmax;
	private double R1;
	private double M1;
	private double R2;
	private double M2;
	private double V;

	private List<Particle> particles;
	private List<Particle[]> matches;
	private Particle big_particle;

	private double right;
	private double left;
	private double up;
	private double down;

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
        matches = new ArrayList<>();
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

    private List <Particle> generateRandomParticles(){
        Random rand = new Random();
        List<Particle> particles = new ArrayList<>(N+1);
        this.big_particle = new Particle(0,new Vector(L/2,L/2),new Vector(0,0),R2,M2);
        particles.add(this.big_particle);

        double x,y,vx,vy;
        for(int i = 1 ; i < N ; i++) {
            //Check it fits
            do {
        		x =  rand.nextDouble()*L;
        		y =  rand.nextDouble()*L;
            }while(overlaps(new Particle(0,new Vector(x,y),null,R1,0) , particles ) || !inside(new Particle(0,new Vector(x,y),null,R1,0)));

            vx = rand.nextDouble()*V;
            vy = rand.nextDouble()*V;

            particles.add(new Particle(i,new Vector(x,y),new Vector(vx,vy),R1,M1));
        }
        return particles;
    }

    private boolean overlaps(Particle other,List<Particle> particles) {
		for(Particle p : particles) {
			if(p.overlapsB(other)) {
				return true;
			}
		}
		return false;
	}

	public double temperature() {
		double k = 0;
		double module;
		for(Particle particle : this.particles) {
			module = particle.getVelocity().module();
			k += 0.5*particle.getMass()*module*module;
		}
		return k    /this.particles.size();
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
		//double min = this.particles.get(0).tc(this.particles.get(1));
		double min = Double.MAX_VALUE;
		double aux;
		for (Particle p : particles) {

		    //check with other particles
			for(Particle q : particles) {
				if(p.getId()!=q.getId()) {
					aux = p.tc(q);
					//System.out.println(String.format(">>>>>>>>>>>>>>>>>>>>>>>%d tc %d = %.2f",p.getId(),q.getId(),p.tc(q)));
					if(aux < min) {
						min = aux;
					}
				}
			}

			//check with walls
			aux = p.tc_vertical_wall(left,right);
			//System.out.println(String.format("%d tc vertical wall   = %.2f",p.getId(),aux = p.tc_vertical_wall(left,right)));
			if(aux < min) {
				min = aux;
			}
			aux = p.tc_horizontal_wall(up,down);
			//System.out.println(String.format("%d tc horizontal wall = %.2f",p.getId(),aux = p.tc_horizontal_wall(up,down)));
			if(aux < min) {
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
				if(p.overlaps(q) && p.getId()!=q.getId()){
					Particle [] r = new Particle[2];
					r[0] = p;
					r[1] = q;
					matches.add(r);
				}
			}
			if(p.getPosition().x - p.getRadius() <= left || p.getPosition().x + p.getRadius() >= right) {
				p.vertical_collision();
			}
			if(p.getPosition().y + p.getRadius() >= up   || p.getPosition().y - p.getRadius() <= down) {
				p.horizontal_collision();
			}
		}
		
	}
	
	public void collision() {
		for(Particle[] pair : matches) {
			System.out.println(String.format("COLLISION: %d %d",pair[0].getId(),pair[1].getId()));
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
			for(Particle q : particles) {
				if(p.overlaps(q) && p.getId() != q.getId()) {
					ret.append(" X");
				}
			}
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
