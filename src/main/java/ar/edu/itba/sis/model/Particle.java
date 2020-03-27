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
		//System.out.println(String.format("%d %d",this.id,other.getId()));
		double dx = other.position.x - this.position.x;
		//System.out.println("dx="+dx);
		double dy = other.position.y - this.position.y;
		//System.out.println("dy="+dy);
		Vector dr = new Vector(dx,dy);
		double dvx = other.velocity.x - this.velocity.x;
		//System.out.println("dvx="+dvx);
		double dvy = other.velocity.y - this.velocity.y;
		//System.out.println("dvy="+dvy);
		Vector dv = new Vector(dvx,dvy);
		double dr2 = dr.dot_product(dr);
		//System.out.println("dr2="+dr2);
		double dv2 = dv.dot_product(dv);
		//System.out.println("dv2="+dv2);
		double dvdr = dv.dot_product(dr);
		//System.out.println("dvdr="+dvdr);
		double sigma = this.radius + other.radius;
		//System.out.println("sigma="+sigma);
		double d = dvdr*dvdr-dv2*(dr2-sigma*sigma);
		//System.out.println("d="+d);
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
		double x = Double.MAX_VALUE;
		if(this.velocity.x > 0) {
			x = (left - radius - position.x) / this.velocity.x;
		}
		else if(this.velocity.x < 0) {
			x = (right - radius - position.x) / this.velocity.x;
		}
		return x;
	}
	
	public double tc_horizontal_wall(double up , double down) {
		double y = Double.MAX_VALUE;
		if(this.velocity.y > 0) {
			y = (up - radius - position.y) / this.velocity.y;
		}
		else if(this.velocity.y < 0) {
			y = (down - radius - position.y) / this.velocity.y;
		}
		return y;
	}
	
	public void collision(Particle other) {
		System.out.println(String.format("COLLISION BETWEEN: %d and %d",id,other.id));
		double sigma = this.radius + other.radius;
		System.out.println("sigma="+sigma);
		double dx = other.position.x - this.position.x;
		System.out.println("dx="+dx);
		double dy = other.position.y - this.position.y;
		System.out.println("dy="+dy);
		Vector dr = new Vector(dx,dy);
		double dvx = other.velocity.x - this.velocity.x;
		System.out.println("dvx="+dvx);
		double dvy = other.velocity.y - this.velocity.y;
		System.out.println("dvy="+dvy);
		Vector dv = new Vector(dvx,dvy);
		double dvdr = dv.dot_product(dr);
		System.out.println("dvdr="+dvdr);
		double J = (2*this.mass*other.mass*dvdr)/(sigma*(this.mass + other.mass));
		System.out.println("J="+J);
		double Jx = J*dx/sigma;
		System.out.println("Jx="+Jx);
		double Jy = J*dy/sigma;
		System.out.println("Jy="+Jy);
		
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
    	return String.format("id=%d position=%s velocity=%s r=%.4f m=%.2f",id,position.toString(),velocity.toString(),this.radius,mass);
    }
    
}
