package team904.mule.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import team904.mule.Robot;
import team904.mule.RobotMap;

public class DriveStrafe extends Command {

	Timer timer;
	double speed;
	double timeToRun;
	
	public DriveStrafe(double speed, double timeToRun){
		requires(RobotMap.drivetrain);
		this.speed = speed;
		this.timer = new Timer();
		this.timeToRun = timeToRun;
	}
	
	protected void initialize(){
		this.timer.reset();
		this.timer.start();
		RobotMap.drivetrain.drive(0, 0, speed);
	}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(timer.hasPeriodPassed(this.timeToRun)){
    		return true;
    	} else {
        return false;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	RobotMap.drivetrain.drive(0,0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

}
