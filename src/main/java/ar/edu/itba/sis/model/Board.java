package ar.edu.itba.sis.model;


import ar.edu.itba.sis.Animation;

import java.util.ArrayList;
import java.util.LinkedList;
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
	private int lastCrachA =-1;
    private int lastCrachB =-1;
    
    private double max_v_module = 0;
    
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
        }while(!(this.Tmin < t && t < this.Tmax));
        
    }

    private List <Particle> generateRandomParticles(){
        Random rand = new Random();
        List<Particle> particles = new ArrayList<>(N+1);
        this.big_particle = new Particle(0,new Vector(L/2,L/2),new Vector(0,0),R2,M2);
        particles.add(this.big_particle);

        double x,y,v,vx,vy,alfa;
        for(int i = 1 ; i < N ; i++) {
            //Check it fits
            do {
        		x =  Math.random()*L;
        		y =  Math.random()*L;
            }while(overlaps(new Particle(0,new Vector(x,y),null,R1,0) , particles ) || !inside(new Particle(0,new Vector(x,y),null,R1,0)));
            
            v = rand.nextDouble()*V;
            alfa = rand.nextDouble();
            vx = v*Math.cos(alfa);
            vy = v*Math.sin(alfa);

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

	public double temperature() { // average kinetic energy
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
		double min = Double.MAX_VALUE;
		double aux;

		//TODO puede estar fallando por no recorrer de esta manera
        for (int i = 0; i < this.particles.size(); i++) {
            for (int j = i+1 ; j < this.particles.size() ; j++) {
                //check with other particles
                aux = this.particles.get(i).tc(this.particles.get(j));
                if(aux<min){
                    min = aux;
                    lastCrachA = i;
                    lastCrachB = j;
                }
            }
            //check walls
            aux = this.particles.get(i).tc_vertical_wall(this.L, 0);
            if(aux<min){
                lastCrachA = -1;
                lastCrachB = -1;
                min = aux;
            }

            aux = this.particles.get(i).tc_horizontal_wall(L,0);
            if(aux<min){
                min = aux;
                lastCrachA = -1;
                lastCrachB = -1;
            }

        }
//		for (Particle p : particles) {
//
//		    //check with other particles
//			for(Particle q : particles) {
//				if(p.getId()!=q.getId()) {
//					aux = p.tc(q);
//					//System.out.println(String.format(">>>>>>>>>>>>>>>>>>>>>>>%d tc %d = %.2f",p.getId(),q.getId(),p.tc(q)));
//					if(aux < min) {
//						min = aux;
//					}
//				}
//			}
//
//			//check with walls
//			aux = p.tc_vertical_wall(left,right);
//			//System.out.println(String.format("%d tc vertical wall   = %.2f",p.getId(),aux = p.tc_vertical_wall(left,right)));
//			if(aux < min) {
//				min = aux;
//			}
//			aux = p.tc_horizontal_wall(up,down);
//			//System.out.println(String.format("%d tc horizontal wall = %.2f",p.getId(),aux = p.tc_horizontal_wall(up,down)));
//			if(aux < min) {
//				min = aux;
//			}
//		}
		return min;
	}
	
	public void update(double tc) {
        matches.clear();
		for (Particle particle : this.particles) {
			particle.getPosition().x = particle.getPosition().x + particle.getVelocity().x*tc;
			particle.getPosition().y = particle.getPosition().y + particle.getVelocity().y*tc;
		}

		int n = particles.size();
		for(int i = 0 ; i < n ; i++) {
			Particle p = particles.get(i);
			if(p.getPosition().x - p.getRadius() <= left || p.getPosition().x + p.getRadius() >= right) {
				p.vertical_collision();
                Animation.WallCrash++;
			}
			if(p.getPosition().y + p.getRadius() >= up   || p.getPosition().y - p.getRadius() <= down) {
				p.horizontal_collision();
                Animation.WallCrash++;
			}
		}
		
	}
	
	public void collision() {
        if(lastCrachA!= -1){
            particles.get(lastCrachA).collision(particles.get(lastCrachB));
            Animation.Crash ++;
        }
	}
	
	public Vector getBigParticle() {
		return particles.get(0).getPosition();
	}
	
	public String get_modules() {
		StringBuilder ret = new StringBuilder();
		for(Particle p : particles) {
			double v = p.getVelocity().module();
			if(v > max_v_module) {
				max_v_module = v;
			}
			ret.append(v);
			ret.append("\n");
		}
		return ret.toString();
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


    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public double getTmin() {
        return Tmin;
    }

    public void setTmin(double tmin) {
        Tmin = tmin;
    }

    public double getTmax() {
        return Tmax;
    }

    public void setTmax(double tmax) {
        Tmax = tmax;
    }

    public double getR1() {
        return R1;
    }

    public void setR1(double r1) {
        R1 = r1;
    }

    public double getM1() {
        return M1;
    }

    public void setM1(double m1) {
        M1 = m1;
    }

    public double getR2() {
        return R2;
    }

    public void setR2(double r2) {
        R2 = r2;
    }

    public double getM2() {
        return M2;
    }

    public void setM2(double m2) {
        M2 = m2;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }

    public int getLastCrachA() {
        return lastCrachA;
    }

    public void setLastCrachA(int lastCrachA) {
        this.lastCrachA = lastCrachA;
    }

    public int getLastCrachB() {
        return lastCrachB;
    }

    public void setLastCrachB(int lastCrachB) {
        this.lastCrachB = lastCrachB;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public List<Particle[]> getMatches() {
        return matches;
    }

    public void setMatches(List<Particle[]> matches) {
        this.matches = matches;
    }

    public Particle getBig_particle() {
        return big_particle;
    }

    public void setBig_particle(Particle big_particle) {
        this.big_particle = big_particle;
    }

    public double getRight() {
        return right;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getUp() {
        return up;
    }

    public void setUp(double up) {
        this.up = up;
    }

    public double getDown() {
        return down;
    }

    public void setDown(double down) {
        this.down = down;
    }
    
    public double getMax_v_module() {
		return max_v_module;
	}

	public void setMax_v_module(double max_v_module) {
		this.max_v_module = max_v_module;
	}

    public Double velocity() {
        double vy=0,vx=0;
        for(Particle particle : particles){
            vx+=particle.getVelocity().x;
            vy+=particle.getVelocity().y;
        }
        return Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2));
    }

    public LinkedList<Double> getListModules() {
        LinkedList<Double> res = new LinkedList<>();
        for (Particle p:particles) {
            res.add(Math.sqrt(Math.pow(p.getVelocity().x,2)+Math.pow(p.getVelocity().y,2)));

        }
        return res;
    }
}
