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
	public double dot_product(Vector other) {
		return this.x*other.x + this.y*other.y;
	}
	public double module() {
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}
	@Override
	public String toString() {
		return String.format("%.6f %.6f\n",x,y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getCuadratic(Vector other){
		return Math.pow(this.x-other.getX(),2) + Math.pow(this.y-other.getY(),2);
	}
}


