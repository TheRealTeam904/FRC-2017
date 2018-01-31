package org.usfirst.frc.team904.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String leftAuto = "Left side Auton";
	final String rightAuto = "Right side Auton";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	public Joystick driverJoystick = new Joystick(0);
	public CANTalon leftMotor1;
	public CANTalon leftMotor2;
	public CANTalon leftMotor3;
	public CANTalon rightMotor1;
	public CANTalon rightMotor2;
	public CANTalon rightMotor3;
	public DoubleSolenoid shifterSolenoid;
	public double xValueLeft;
	public double xValueRight;
	public double yValueRight;
	public double yValueLeft;
	public double motorLeft;
	public double motorRight;
	public double scaleFactor;
	public boolean triggerPressedLastTime;
	public boolean lowSpeed;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("Left side Auton", leftAuto);
		chooser.addObject("Right side Auton", rightAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		shifterSolenoid = new DoubleSolenoid(0,1);
		driverJoystick = new Joystick(0);
		leftMotor1 = new CANTalon(0);
		leftMotor2 = new CANTalon(1);
		leftMotor3 = new CANTalon(2);
		rightMotor1 = new CANTalon(3);
		rightMotor2 = new CANTalon(4);
		rightMotor3 = new CANTalon(5);
		triggerPressedLastTime = false;
		lowSpeed = true;
		shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		drive();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
	
	public void drive(){
		
		
		xValueLeft = driverJoystick.getX();
		xValueRight = -driverJoystick.getX();
		yValueLeft = -driverJoystick.getY();
		yValueRight = -driverJoystick.getY();
		motorLeft = yValueLeft + xValueLeft;
		motorRight = yValueRight + xValueRight;
		if((Math.max(Math.abs(motorLeft), Math.abs(motorRight)) > 1)){
			scaleFactor = Math.max(Math.abs(motorLeft), Math.abs(motorRight));
		}
		else{
			scaleFactor = 1;
		}
		motorLeft = motorLeft/scaleFactor;
		motorRight = -motorRight/scaleFactor;
		
		leftMotor1.set(motorLeft);
		leftMotor2.set(motorLeft);
		leftMotor3.set(motorLeft);
		rightMotor1.set(motorRight);
		rightMotor2.set(motorRight);
		rightMotor3.set(motorRight);
/* Checks if button is pressed		
 * Pressed the first time will do it
 * Releasing the button will let the second if to run again once trigger is pressed
 */
		boolean triggerPressedThisTime;
		triggerPressedThisTime = driverJoystick.getButton(Joystick.ButtonType.kTrigger);
// Checks if trigger is pressed
		if (triggerPressedThisTime){
/* triggerPressedLastTime is set to false for first time
 * If triggerPressedLastTime is true, will ignore conditions until the trigger is released
 * Sets the trigger value the same so to not run if trigger is held
 */
			if (triggerPressedLastTime == false){
				triggerPressedLastTime = triggerPressedThisTime;
/* This part shifts the solenoid back and forth to speed up or slow down the robot
 * Each time the trigger is pressed, the solenoid shifts
 */
				if (lowSpeed){
					shifterSolenoid.set(DoubleSolenoid.Value.kForward);
					lowSpeed = false;
				}
				else {
					shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
					lowSpeed = true;
				}
			}
		}
/* Each time the trigger is released, the variable, triggerPressedLastTime turns false
 * to allow the if statement to run again
 * Releasing it will also turn off the solenoid
 */
		else {
			triggerPressedLastTime = triggerPressedThisTime;
			shifterSolenoid.set(DoubleSolenoid.Value.kOff);
		}		
//		if (driverJoystick.getRawButton(1))
//			shifterSolenoid.set(!shifterSolenoid.get());
		
		}
}
	
