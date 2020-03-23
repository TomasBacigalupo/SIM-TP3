package ar.edu.itba.sis;

public class Particle {
	int id;
	Vector position;
	Vector velocity;
	double radius;
	double mass;
	
	public Particle(int id , Vector position , Vector velocity , double radius , double mass) {
		this.id = id;
		this.position = position;
		this.velocity = velocity;
		this.radius = radius;
		this.mass = mass;
	}
	
	boolean overlaps(Particle other) {
		double dx = this.position.x - other.position.x;
		double dy = this.position.y - other.position.y;
		double dr = this.radius - other.radius;
		return dx*dx + dy*dy > dr*dr ;
	}
	
	double tc (Particle other) {
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
	
	double tc_vertical_wall (double right , double left) {
		double x = 0;
		if(this.velocity.x > 0) {
			x = (left - radius - position.x) / this.velocity.x;
		}
		else if(this.velocity.x < 0) {
			x = (right - radius - position.x) / this.velocity.x;
		}
		return x;
	}
	
	double tc_horizontal_wall(double up , double down) {
		double y = 0;
		if(this.velocity.y > 0) {
			y = (up - radius - position.x) / this.velocity.y;
		}
		else if(this.velocity.y < 0) {
			y = (down - radius - position.x) / this.velocity.y;
		}
		return y;
	}
	
	void collision(Particle other) {
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
	
	void vertical_collision() {
		this.velocity.x = -1 * this.velocity.x;
	}
	
	void horizontal_collision() {
		this.velocity.y = -1 * this.velocity.y;
	}
}
