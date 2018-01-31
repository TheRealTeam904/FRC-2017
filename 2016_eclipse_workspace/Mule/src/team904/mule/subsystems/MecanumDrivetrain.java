package team904.mule.subsystems;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class MecanumDrivetrain extends Subsystem {
	
	private TalonSRX frontLeftMotor;
	private TalonSRX frontRightMotor;
	private TalonSRX backLeftMotor;
	private TalonSRX backRightMotor;
	
	public MecanumDrivetrain(TalonSRX frontLeftMotor, TalonSRX frontRightMotor, TalonSRX backLeftMotor, TalonSRX backRightMotor)
	{
		this.frontLeftMotor = frontLeftMotor;
		this.frontRightMotor = frontRightMotor;
		this.backLeftMotor = backLeftMotor;
		this.backRightMotor = backRightMotor;
	}
	
	public void drive(double forwardSpeed, double turnSpeed, double strafeSpeed)
	{
	frontLeftMotor.set(forwardSpeed + turnSpeed + strafeSpeed);
	frontRightMotor.set(-forwardSpeed + turnSpeed + strafeSpeed);
	backLeftMotor.set(forwardSpeed + turnSpeed - strafeSpeed);
	backRightMotor.set(-forwardSpeed + turnSpeed - strafeSpeed);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
