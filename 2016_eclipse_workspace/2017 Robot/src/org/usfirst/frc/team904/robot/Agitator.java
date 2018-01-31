package org.usfirst.frc.team904.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Agitator {
	public double agitatorPosition = 0;
	public AgitatorDirection agitatorDirection = AgitatorDirection.NonMove;
	public Timer agitatorTimer = new Timer();
	public CANTalon agitatorMotor = new CANTalon(7);
	
	public Agitator(){
	}
	
	public void move(boolean leftBumper, boolean bButton){
		// Bypassing all the fancy positioning logic
		if(leftBumper)
		{
			agitatorMotor.set(-0.3);
		}
		else if(bButton)
		{
			agitatorMotor.set(0.3);
		}
		else
		{
			agitatorMotor.set(0);
		}
		
		// Limiting position based on timer
		/*if (leftBumper){
			if(getRealPosition() > -.3){
				setDirection(AgitatorDirection.Left);
			}
			else{
				setDirection(AgitatorDirection.NonMove);
			}
		} else if (bButton){
			if(getRealPosition() < .3) {
				setDirection(AgitatorDirection.Right);
			} else {
				setDirection(AgitatorDirection.NonMove);
			}
		} else {
			if (getRealPosition() > .01){
				setDirection(AgitatorDirection.Left);
			} else if (getRealPosition() < -.01){
				setDirection(AgitatorDirection.Right);
			} else {
				setDirection(AgitatorDirection.NonMove);
			}
		}*/
	}
	
	
	void setPosition(){
			agitatorPosition = getRealPosition();
			agitatorTimer.stop();
			agitatorTimer.reset();
	}
	void setDirection(AgitatorDirection direction){
		if (agitatorDirection != direction){
			setPosition();
			agitatorDirection = direction;
			setMotor(direction);
		}
	}
	
	double getRealPosition(){
		switch (agitatorDirection){
		case Left:
			return agitatorPosition - agitatorTimer.get();
		case Right:
			return agitatorPosition + agitatorTimer.get();
		case NonMove:
			return agitatorPosition;
		default:
			return agitatorPosition;
		}
	}
	
	void setMotor(AgitatorDirection direction)
	{
		switch(direction){
		case Left:
			agitatorMotor.set(-0.3);
			agitatorTimer.start();
			break;
		case Right:
			agitatorMotor.set(0.3);
			agitatorTimer.start();
			break;
		case NonMove:
			agitatorMotor.set(0);
			break;
		default:
			agitatorMotor.set(0);
			break;
		}
	}
	
	public void adjustCenter(double newCenter){
		agitatorPosition = agitatorPosition - newCenter;
	}
	enum AgitatorDirection{
		Left,
		Right,
		NonMove,
	}
} 
