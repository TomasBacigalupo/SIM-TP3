package ar.edu.itba.sis.model;

public class Vector {
	double x;
	double y;
	public Vector(double x , double y) {
		this.x = x;
		this.y = y;
	}
	public Vector() {
		// TODO Auto-generated constructor stub
	}
	double dot_product(Vector other) {
		return this.x*other.x + this.y*other.y;
	}
	public double module() {
		return this.x*this.x + this.y*this.y;
	}
	
	@Override
	public String toString() {
		return String.format("(%.2f %.2f)",x,y);
	}
	
}
