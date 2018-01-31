package org.usfirst.frc.team904.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
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
	final String doNothing = "Do nothing";
	final String leftAuto = "Left side Auton";
	final String rightAuto = "Right side Auton";
	final String centerAuto = "Center Auton";
	final String baseLine = "Cross Base Line";
	final String rightShooter = "Right side Shoot";
	final String leftShooter = "Left side Shoot";
	
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	public Joystick driverJoystick = new Joystick(0);
	public XboxController operatorController = new XboxController(1);
	public CANTalon leftMotor1;
	public CANTalon leftMotor2;
	public CANTalon leftMotor3;
	public CANTalon rightMotor1;
	public CANTalon rightMotor2;
	public CANTalon rightMotor3;
	public CANTalon shooterMotor;
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
	public double xJoystick;
	public double yJoystick;
	public double zJoystick;
	
	public DoubleSolenoid gearSolenoid;
	public boolean aButton;
	public boolean buttonALastTime;
	public boolean slotClose;
	public double shooterSpeed;
	public double motorSpeed;
	public double firingSpeed;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", doNothing);
		chooser.addObject("Left side Auton", leftAuto);
		chooser.addObject("Right side Auton", rightAuto);
		SmartDashboard.putData("Auto choices", chooser);

		shifterSolenoid = new DoubleSolenoid(0, 1);
		gearSolenoid = new DoubleSolenoid(2, 3);
		driverJoystick = new Joystick(0);
		// Drive variables
		leftMotor1 = new CANTalon(0);
		leftMotor2 = new CANTalon(1);
		leftMotor3 = new CANTalon(2);
		rightMotor1 = new CANTalon(3);
		rightMotor2 = new CANTalon(4);
		rightMotor3 = new CANTalon(5);
		shooterMotor = new CANTalon(6);
		triggerPressedLastTime = false;
		lowSpeed = true;
		shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
		// Operation Variables
		gearSolenoid.set(DoubleSolenoid.Value.kReverse);
		buttonALastTime = false;
		slotClose = true;
		shooterSpeed = 0;
		motorSpeed = .5;
		firingSpeed = 0;
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
		case doNothing:
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
		xJoystick = driverJoystick.getX();
		yJoystick = driverJoystick.getY();
		zJoystick = driverJoystick.getZ();
		double xValue;
		double yValue;
		if (Math.abs(zJoystick) > Math.abs(xJoystick)){
			xJoystick = zJoystick;
		}
		if (xJoystick > 0.15){
			xValue = (xJoystick-.15)*1.176;	
		}
		else if (xJoystick < -.15 ){
			xValue = (xJoystick+.15)*1.176;
		}
		else {
			xValue = 0;
		}
		if (yJoystick > 0.15){
			yValue = (yJoystick-.15)*1.176;
		}
		else if (yJoystick < -.15 ){
			yValue = (yJoystick+.15)*1.176;
		}
		else{
			yValue = 0;
		}
		drive(xValue, yValue);
		gear();
		shooter();
		
		SmartDashboard.putNumber("Joystick X Value", xJoystick);
		SmartDashboard.putNumber("Joystick Y Value", yJoystick);
		SmartDashboard.putNumber("Dead Zone X Value", xValue);
		SmartDashboard.putNumber("Dead Zone Y Value", yValue);
		SmartDashboard.putNumber("X vs Z", Math.max(xJoystick, zJoystick));
		SmartDashboard.putNumber("Joystick Z Value", zJoystick);
		SmartDashboard.putNumber("Shooter Speed", firingSpeed);
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}

	public void drive(double xValue, double yValue) {

		xValueLeft = xValue;
		xValueRight = -xValue;
		yValueLeft = -yValue;
		yValueRight = -yValue;
		motorLeft = (yValueLeft + xValueLeft);
		motorRight = (yValueRight + xValueRight);
		if ((Math.max(Math.abs(motorLeft), Math.abs(motorRight)) > 1)) {
			scaleFactor = Math.max(Math.abs(motorLeft), Math.abs(motorRight));
		} else {
			scaleFactor = 1;
		}
		if (yValueLeft < 0) {
			if (Math.abs(zJoystick) < Math.abs(xJoystick)){
				double temp = motorLeft;
				motorLeft = motorRight;
				motorRight = temp;
			}
		}
		motorLeft = motorLeft / scaleFactor;
		motorRight = -motorRight / scaleFactor;

		leftMotor1.set(motorLeft);
		leftMotor2.set(motorLeft);
		leftMotor3.set(motorLeft);
		rightMotor1.set(motorRight);
		rightMotor2.set(motorRight);
		rightMotor3.set(motorRight);
		/*
		 * Checks if button is pressed Pressed the first time will do it
		 * Releasing the button will let the second if to run again once trigger
		 * is pressed
		 */
		boolean triggerPressedThisTime;
		triggerPressedThisTime = driverJoystick.getButton(Joystick.ButtonType.kTrigger);
		// Checks if trigger is pressed
		if (triggerPressedThisTime) {
			/*
			 * triggerPressedLastTime is set to false for first time If
			 * triggerPressedLastTime is true, will ignore conditions until the
			 * trigger is released Sets the trigger value the same so to not run
			 * if trigger is held
			 */
			if (triggerPressedLastTime == false) {
				triggerPressedLastTime = triggerPressedThisTime;
				/*
				 * This part shifts the solenoid back and forth to speed up or
				 * slow down the robot Each time the trigger is pressed, the
				 * solenoid shifts
				 */
				if (lowSpeed) {
					shifterSolenoid.set(DoubleSolenoid.Value.kForward);
					lowSpeed = false;
				} else {
					shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
					lowSpeed = true;
				}
			}
		}
		/*
		 * Each time the trigger is released, the variable,
		 * triggerPressedLastTime turns false to allow the if statement to run
		 * again Releasing it will also turn off the solenoid
		 */
		else {
			triggerPressedLastTime = triggerPressedThisTime;
			shifterSolenoid.set(DoubleSolenoid.Value.kOff);
		}
		// if (driverJoystick.getRawButton(1))
		// shifterSolenoid.set(!shifterSolenoid.get());

	}
	public void gear(){
		boolean buttonAThisTime;
		
		buttonAThisTime = operatorController.getRawButton(3);
		if (buttonAThisTime){
			if (buttonALastTime == false){
				buttonALastTime = buttonAThisTime;
				if (slotClose){
					gearSolenoid.set(DoubleSolenoid.Value.kForward);
					slotClose = false;
				} else {
					gearSolenoid.set(DoubleSolenoid.Value.kReverse);
					slotClose = true;
				}	
			}
		} else {
			buttonALastTime = false;
			gearSolenoid.set(DoubleSolenoid.Value.kOff);
		}
	}
	public void shooter(){
		boolean rightBumper = operatorController.getRawButton(6);
		boolean aButton = operatorController.getRawButton(1);
		boolean yButton = operatorController.getRawButton(4);
		if (rightBumper){
			if (yButton){
				shooterSpeed += .005;
			} else if(aButton){
				shooterSpeed -= .005;
			}
		firingSpeed = motorSpeed + shooterSpeed;
		} else {
			firingSpeed = 0;
		}
		shooterMotor.set(firingSpeed);
	}
}
