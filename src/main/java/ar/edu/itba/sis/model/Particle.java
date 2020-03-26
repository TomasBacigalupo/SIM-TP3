package ar.edu.itba.sis.model;

public class Particle {
	private int id;
	private Vector position;
	private Vector velocity;
	private double radius;
	private double mass;
	
    public Particle(int id, Vector position) {
        this.id = id;
        this.position = position;
        this.mass = 0;
        this.velocity = new Vector(0,0);
        this.radius = 0;
    }
	
    public Particle(int id, Vector position, Vector velocity, double radius, double mass) {
		this.id = id;
		this.position = position;
		this.velocity = velocity;
		this.radius = radius;
		this.mass = mass;
	}

	public boolean overlapsB(Particle other){
        double dx = this.position.x - other.position.x;
        double dy = this.position.y - other.position.y;
        double centerDistance = Math.sqrt( dx*dx + dy*dy);
        double particleDistance = centerDistance -this.radius -other.radius;
        return particleDistance < 0;

    }
    
	public boolean overlaps(Particle other) {
		double dx = this.position.x - other.position.x;
		double dy = this.position.y - other.position.y;
		double dr = this.radius + other.radius;
		return dx*dx + dy*dy < dr*dr ;
	}
	
	public double tc (Particle other) {

		double dx = other.position.x - this.position.x;
		double dy = other.position.y - this.position.y;
		Vector dr = new Vector(dx,dy);
		double dvx = other.velocity.x - this.velocity.x;
		double dvy = other.velocity.y - this.velocity.y;
		Vector dv = new Vector(dvx,dvy);
		double dr2 = dr.dot_product(dr);
		double dv2 = dv.dot_product(dv);
		double dvdr = dv.dot_product(dr);
		double sigma = this.radius + other.radius;
		double d = dvdr*dvdr-dv2*(dr2-sigma*sigma);
		if(dvdr>0) {
			return Double.MAX_VALUE;
		}
		else if(d<0) {
			return Double.MAX_VALUE;
		}
		else {
			return - ( dvdr + Math.sqrt(d) ) / dv2 ;
		}
	}
	
	public double tc_vertical_wall (double right , double left) {
		double x = 0;
		if(this.velocity.x > 0) {
			x = (left - radius - position.x) / this.velocity.x;
		}
		else if(this.velocity.x < 0) {
			x = (right - radius - position.x) / this.velocity.x;
		}
		return x;
	}
	
	public double tc_horizontal_wall(double up , double down) {
		double y = 0;
		if(this.velocity.y > 0) {
			y = (up - radius - position.x) / this.velocity.y;
		}
		else if(this.velocity.y < 0) {
			y = (down - radius - position.x) / this.velocity.y;
		}
		return y;
	}
	
	public void collision(Particle other) {
		double sigma = this.radius + other.radius;
		double dx = other.position.x - this.position.x;
		double dy = other.position.y - this.position.y;
		Vector dr = new Vector(dx,dy);
		double dvx = other.velocity.x - this.velocity.x;
		double dvy = other.velocity.y - this.velocity.y;
		Vector dv = new Vector(dvx,dvy);
		double dvdr = dv.dot_product(dr);
		double J = (2*this.mass*other.mass*dvdr)/sigma*(this.mass + other.mass);
		double Jx = J*dx/sigma;
		double Jy = J*dy/sigma;
		
		this.velocity.x = this.velocity.x + Jx/this.mass;
		this.velocity.y = this.velocity.y + Jy/this.mass;
		
		other.velocity.x = other.velocity.x - Jx/other.mass;
		other.velocity.y = other.velocity.y - Jy/other.mass;
		
	}
	
	public void vertical_collision() {
		this.velocity.x = -1 * this.velocity.x;
	}
	
	public void horizontal_collision() {
		this.velocity.y = -1 * this.velocity.y;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    
    public String toOvito(){
	    StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append(" ");
        sb.append(this.position.x);
        sb.append(" ");
        sb.append(this.position.y);
        sb.append(" ");
        sb.append(this.radius);
        return sb.toString();
    }
    
    @Override
    public String toString() {
    	return String.format("id=%d position=%s velocity=%s r=%.2f m=%.2f",id,position.toString(),velocity.toString(),radius,mass);
    }
    
}
