package org.usfirst.frc.team904.robot;



public class targetPosition {
	public double centerX;
	public double distanceZ;
	public static double z_projected = 1;
	public static double targetWidth;
	
	public targetPosition(double centerX, double distanceZ){
		this.centerX = centerX;
		this.distanceZ = distanceZ;
	}
	
	public static targetPosition findingPositionFromCamera(double x1_Projected, double x2_Projected){
		double x1;
		double x2;
		double z= z_projected*(targetWidth)/(x1_Projected - x2_Projected);
		x1 = z*(x1_Projected)/z_projected;
		x2 = z*(x2_Projected)/z_projected;
		double xAverage = (x1+x2)/2;
		return new targetPosition(xAverage, z);
	}
}
