package ar.edu.itba.sis;

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
		return 0;
	}
	public double module() {
		return this.x*this.x + this.y*this.y;
	}
}
