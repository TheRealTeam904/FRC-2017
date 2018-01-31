package org.usfirst.frc.team904.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import com.ctre.CANTalon;


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
	final String leftHopper = "Left side Hopper";
	
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
	public NetworkTable contourReport;
	public DigitalInput ballHolderCAM;
	
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
	public DoubleSolenoid shooterSolenoid;
	public boolean aButton;
	public boolean buttonXLastTime;
	public boolean slotClose;
	public double shooterSpeed;
	public double motorSpeed;
	public double firingSpeed;
	public boolean shooterDown;
	public boolean shooterUp;
	
	private CANTalon ballHolder;
	public boolean rightTriggerLast = false;
	public double[] cameraDefaultX = { 80, 80 };
	public Timer shooterTimer;
	public boolean yPress;
	public boolean aPress;
	public double voltage;
	public double Onehundred;
	public Encoder leftEncoder;
	public Encoder rightEncoder;
	public boolean supposeToGoForward;
	public boolean supposeToTurn;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", doNothing);
		chooser.addObject("Left side Auton", leftAuto);
		chooser.addObject("Right side Auton", rightAuto);
		chooser.addObject("Center Auton", centerAuto);
		chooser.addObject("Baseline", baseLine);
		chooser.addObject("Right side shooter", rightShooter);
		chooser.addObject("Left side shooter", leftShooter);
		chooser.addObject("Left side Hopper", leftHopper);
		SmartDashboard.putData("Auto choices", chooser);

		CameraServer.getInstance().startAutomaticCapture();
		contourReport = NetworkTable.getTable("GRIP/myContoursReport");

		shifterSolenoid = new DoubleSolenoid(0, 1);
		gearSolenoid = new DoubleSolenoid(2, 3);
		shooterSolenoid = new DoubleSolenoid(4, 5);
		driverJoystick = new Joystick(0);
		// Drive variables
		leftMotor1 = new CANTalon(0);
		leftMotor2 = new CANTalon(1);
		leftMotor3 = new CANTalon(2);
		rightMotor1 = new CANTalon(3);
		rightMotor2 = new CANTalon(4);
		rightMotor3 = new CANTalon(5);
		shooterMotor = new CANTalon(6);
		ballHolder = new CANTalon(7);
		triggerPressedLastTime = false;
		lowSpeed = true;
		shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
		// Operation Variables
		gearSolenoid.set(DoubleSolenoid.Value.kReverse);
		buttonXLastTime = false;
		slotClose = true;
		shooterSpeed = 0;
		motorSpeed = .5;
		firingSpeed = 0;
		shooterSolenoid.set(DoubleSolenoid.Value.kReverse);
		shooterDown = true;
		shooterUp = false;
		shooterTimer = new Timer();
		yPress = false;
		aPress = false;
	 	shooterMotor.changeControlMode(CANTalon.TalonControlMode.Voltage);
	 	voltage = 0;
	 	Onehundred = 0;
	 	//leftEncoder = new Encoder(0,1, false, CounterBase.EncodingType.k4X);
	 	//rightEncoder = new Encoder(2,3, true, CounterBase.EncodingType.k4X);
	 	leftMotor1.setPosition(0);
	 	rightMotor1.setPosition(0);
	 	ballHolderCAM = new DigitalInput(0);
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
		leftMotor1.enableBrakeMode(true);
		leftMotor2.enableBrakeMode(true);
		leftMotor3.enableBrakeMode(true);
		rightMotor1.enableBrakeMode(true);
		rightMotor2.enableBrakeMode(true);
		rightMotor3.enableBrakeMode(true);
		leftMotor1.setPosition(0);
		supposeToGoForward = true;
		supposeToTurn = true;
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
			leftMotor1.set(0);
			leftMotor2.set(0);
			leftMotor3.set(0);
			rightMotor1.set(0);
			rightMotor2.set(0);
			rightMotor3.set(0);
			ballHolder.set(0);
			shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
			gearSolenoid.set(DoubleSolenoid.Value.kReverse);
			shooterSolenoid.set(DoubleSolenoid.Value.kReverse);
			shooterMotor.set(0);

			break;
			
			
			
		case centerAuto:
			double[] xTargets = contourReport.getNumberArray("centerX", cameraDefaultX);
			if (xTargets.length < 2) {
				xTargets = cameraDefaultX;
			}
			double averageXCenter = (xTargets[0] + xTargets[1]) / 2;
			visionAuton();
			SmartDashboard.putNumber("Camera X Center", averageXCenter);
			SmartDashboard.putNumber("Left Motor", leftMotor1.get());
			SmartDashboard.putNumber("Left Motor", leftMotor2.get());
			SmartDashboard.putNumber("Left Motor", leftMotor3.get());
			SmartDashboard.putNumber("Right Motor", rightMotor1.get());
			SmartDashboard.putNumber("Right Motor", rightMotor2.get());
			SmartDashboard.putNumber("Right Motor", rightMotor3.get());
			
			break;
		case leftAuto:
			// Number will be changed later
			double desiredSpeed = .2;
			double desiredDist = 5;
			double desiredTurnDist = 1;
			
			
			
			if (supposeToGoForward){
				if (forward(desiredSpeed, desiredDist)){
				} else {
					supposeToGoForward = false;
					leftMotor1.setPosition(0);
				}
			} else if (supposeToTurn){
				if (turningLeft(desiredSpeed, desiredTurnDist)){
				} else {
					supposeToTurn = false;
					leftMotor1.setPosition(0);
				}
			} else {
				visionAuton();
			}
			break;
			
			
			
			
			
		case rightAuto:
			desiredSpeed = .2;
			desiredDist = 5;
			desiredTurnDist = 1;
			
			if (supposeToGoForward){
				if (forward(desiredSpeed, desiredDist)){
				} else {
					supposeToGoForward = false;
					leftMotor1.setPosition(0);
				}
			} else if (supposeToTurn){
				if (turningLeft(desiredSpeed, desiredTurnDist)){
				} else {
					supposeToTurn = false;
					leftMotor1.setPosition(0);
				}
			} else {
				visionAuton();
			}
			
			break;
			
			
			
			
			
			
		case leftShooter:
			// Number will be changed later
			double desiredBoilerDist = 11000;
			desiredSpeed = .2;
			desiredDist = 5;
			desiredTurnDist = 5750;
			
			if (supposeToGoForward){
				while (leftMotor1.getPosition() < desiredBoilerDist){
					leftMotor1.set(desiredSpeed);
					leftMotor2.set(desiredSpeed);
					leftMotor3.set(desiredSpeed);
					rightMotor1.set(-desiredSpeed);
					rightMotor2.set(-desiredSpeed);
					rightMotor3.set(-desiredSpeed);
				}
				leftMotor1.setPosition(0);
				supposeToGoForward = false;
			}
			if (supposeToTurn){	
				while (leftMotor1.getPosition() < desiredTurnDist){
					leftMotor1.set(-desiredSpeed);
					leftMotor2.set(-desiredSpeed);
					leftMotor3.set(-desiredSpeed);
					rightMotor1.set(-desiredSpeed);
					rightMotor2.set(-desiredSpeed);
					rightMotor3.set(-desiredSpeed);
				}
				leftMotor1.setPosition(0);
				supposeToTurn = false;
			}
			shooterMotor.set(.55);
			ballHolder.set(.3);
			
			/* if (supposeToGoForward){
				if (forward(desiredSpeed, desiredDist)){
				} else {
					supposeToGoForward = false;
					leftMotor1.setPosition(0);
				}
			} else if (supposeToTurn){
				if (turningLeft(desiredSpeed, desiredTurnDist)){
				} else {
					supposeToTurn = false;
					leftMotor1.setPosition(0);
				}
			} else {
				visionAuton();
			}
			*/
			break;
			
			
			
			
			
		case rightShooter:
			desiredSpeed = .2;
			desiredDist = 5;
			desiredTurnDist = 1;
			desiredBoilerDist = 1.5;
			if (supposeToGoForward){
				if (forward(desiredSpeed, desiredDist)){
				} else {
					supposeToGoForward = false;
					leftMotor1.setPosition(0);
				}
			} else if (supposeToTurn){
				if (turningLeft(desiredSpeed, desiredTurnDist)){
				} else {
					supposeToTurn = false;
					leftMotor1.setPosition(0);
				}
			} else {
				visionAuton();
			}
			if (rightMotor1.getPosition() < desiredBoilerDist){
				leftMotor1.set(desiredSpeed);
				leftMotor2.set(desiredSpeed);
				leftMotor3.set(desiredSpeed);
				rightMotor1.set(desiredSpeed);
				rightMotor2.set(desiredSpeed);
				rightMotor3.set(desiredSpeed);
			}
			// Add part that either moves robot forward and shoot, or just shoot
			
			break;
			
			
			
			
			
		case baseLine:
			desiredDist = -21200;
			desiredSpeed = .2;
			if (leftMotor1.getPosition() > desiredDist){
				leftMotor1.set(desiredSpeed);
				leftMotor2.set(desiredSpeed);
				leftMotor3.set(desiredSpeed);
				rightMotor1.set(-desiredSpeed);
				rightMotor2.set(-desiredSpeed);
				rightMotor3.set(-desiredSpeed);
			} else {
				leftMotor1.set(0);
				leftMotor2.set(0);
				leftMotor3.set(0);
				rightMotor1.set(0);
				rightMotor2.set(0);
				rightMotor3.set(0);
			}
			break;
		case leftHopper:
			desiredDist = 0;
			desiredSpeed = 0;
			if (leftMotor1.getPosition() > desiredDist){
				leftMotor1.set(desiredSpeed/2);
				leftMotor2.set(desiredSpeed/2);
				leftMotor3.set(desiredSpeed/2);
				rightMotor1.set(-desiredSpeed);
				rightMotor2.set(-desiredSpeed);
				rightMotor3.set(-desiredSpeed);
			}
		}
		SmartDashboard.putNumber("Left encoder", leftMotor1.getPosition());
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		leftMotor1.enableBrakeMode(false);
		leftMotor2.enableBrakeMode(false);
		leftMotor3.enableBrakeMode(false);
		rightMotor1.enableBrakeMode(false);
		rightMotor2.enableBrakeMode(false);
		rightMotor3.enableBrakeMode(false);
		xJoystick = driverJoystick.getX();
		yJoystick = driverJoystick.getY();
		zJoystick = driverJoystick.getZ();
		double xValue;
		double yValue;
		if (Math.abs(zJoystick) > Math.abs(xJoystick)) {
			xJoystick = zJoystick;
		}
		if (xJoystick > 0.15) {
			xValue = (xJoystick - .15) * 1.176;
		} else if (xJoystick < -.15) {
			xValue = (xJoystick + .15) * 1.176;
		} else {
			xValue = 0;
		}
		if (yJoystick > 0.15) {
			yValue = (yJoystick - .15) * 1.176;
		} else if (yJoystick < -.15) {
			yValue = (yJoystick + .15) * 1.176;
		} else {
			yValue = 0;
		}
		
		drive(xValue, yValue);
		gear();
		shooter();
		adjustShooter();
		
		if (ballHolderCAM.get()){
			if (operatorController.getRawButton(5)) {
				ballHolder.set(.5);
			} else {
				ballHolder.set(0);
			}
		} else {
			ballHolder.set(.3);
		}
		
		voltage = shooterMotor.getOutputVoltage();
		// This will display the shooter value as it changes
		Onehundred = (motorSpeed + shooterSpeed)*100;
		SmartDashboard.putNumber("Shooter Speed", (int) Onehundred);
		SmartDashboard.putNumber("", (int) Onehundred);
		SmartDashboard.putBoolean("Gear slot", !slotClose);
		SmartDashboard.putBoolean("Shooter Up", shooterUp);
		SmartDashboard.putBoolean("Shooter Down", shooterDown);
		SmartDashboard.putNumber("Timer", shooterTimer.get());
		SmartDashboard.putNumber("Shooter Voltage", voltage);
		SmartDashboard.putNumber("Left encoder", leftMotor1.getPosition());
		SmartDashboard.putNumber("Right Encoder", rightMotor1.getPosition());
		SmartDashboard.putBoolean("Ball CAM", ballHolderCAM.get());
		SmartDashboard.putNumber("Ball Holder Motor", ballHolder.get());
		SmartDashboard.putString("Speed", lowSpeed? "Slow": "Fast");
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}

	public void visionAuton() {
		double[] xTargets = contourReport.getNumberArray("centerX", cameraDefaultX);
		if (xTargets.length < 2) {
			xTargets = cameraDefaultX;
		}
		double averageXCenter = (xTargets[0] + xTargets[1]) / 2;
		double xTargetDiffer = Math.abs(xTargets[0] - xTargets[1]);
		if (averageXCenter >= 75 && averageXCenter <= 85) {
			if (xTargetDiffer > 100) {
				leftMotor1.set(0);
				leftMotor2.set(0);
				leftMotor3.set(0);
				rightMotor1.set(0);
				rightMotor2.set(0);
				rightMotor3.set(0);

				Timer.delay(3);

				leftMotor1.set(-.5);
				leftMotor2.set(-.5);
				leftMotor3.set(-.5);
				rightMotor1.set(.5);
				rightMotor2.set(.5);
				rightMotor3.set(.5);

			} else {
				leftMotor1.set(.5);
				leftMotor2.set(.5);
				leftMotor3.set(.5);
				rightMotor1.set(-.5);
				rightMotor2.set(-.5);
				rightMotor3.set(-.5);
				
				leftEncoder.reset();
			}
		} else {
			if (averageXCenter < 75) {
				leftMotor1.set(.5);
				leftMotor2.set(.5);
				leftMotor3.set(.5);
				rightMotor1.set(.5);
				rightMotor2.set(.5);
				rightMotor3.set(.5);
				
				leftEncoder.reset();
				
			} else if (averageXCenter > 85) {
				leftMotor1.set(-.5);
				leftMotor2.set(-.5);
				leftMotor3.set(-.5);
				rightMotor1.set(-.5);
				rightMotor2.set(-.5);
				rightMotor3.set(-.5);
				
				leftEncoder.reset();
				
			}
		}
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
			if (Math.abs(zJoystick) < Math.abs(xJoystick)) {
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

	public void gear() {
		boolean buttonXThisTime;
		// Test this on dashboard
		buttonXThisTime = operatorController.getRawButton(3);
		if (buttonXThisTime) {
			if (buttonXLastTime == false) {
				buttonXLastTime = buttonXThisTime;
				if (slotClose) {
					gearSolenoid.set(DoubleSolenoid.Value.kForward);
					slotClose = false;
				} else {
					gearSolenoid.set(DoubleSolenoid.Value.kReverse);
					slotClose = true;
				}
			}
		} else {
			buttonXLastTime = false;
			gearSolenoid.set(DoubleSolenoid.Value.kOff);
		}
	}

	public void shooter() {
		boolean rightBumper = operatorController.getRawButton(6);
		boolean aButton = operatorController.getRawButton(1);
		boolean yButton = operatorController.getRawButton(4);

		if (yButton) {
			if (!yPress) {
				shooterTimer.start();
			}
			if (shooterTimer.hasPeriodPassed(.05)) {
				if (shooterSpeed >= .5){
					shooterSpeed += 0;
				} else {
					shooterSpeed += .01;
				}
				shooterTimer.reset();
			}
			yPress = true;
		}
		else if (aButton) {
			if (!aPress) {
				shooterTimer.start();
			}
			if (shooterTimer.hasPeriodPassed(.05)) {
				if (shooterSpeed <= -.5){
					shooterSpeed -= 0;
				} else { 
					shooterSpeed -= .01;
				}
				shooterTimer.reset();
			}
			aPress = true;
		} else {
			shooterTimer.stop();
			shooterTimer.reset();
			yPress = false;
			aPress = false;
		}
		if (rightBumper) {
			firingSpeed = motorSpeed + shooterSpeed;
		} else {
			firingSpeed = 0;
		}
		shooterMotor.set(-firingSpeed*10);
	}

	public void adjustShooter() {
		double rightTrigger = operatorController.getRawAxis(3);
		boolean rightTriggerButton = false;

		if (rightTrigger > 0.5) {
			rightTriggerButton = true;
		}
		if (rightTriggerButton) {
			if (rightTriggerLast == false) {
				rightTriggerLast = true;
				if (shooterDown) {
					shooterSolenoid.set(DoubleSolenoid.Value.kForward);
					shooterDown = false;
					shooterUp = true;
				} else {
					shooterSolenoid.set(DoubleSolenoid.Value.kReverse);
					shooterDown = true;
					shooterUp = false;
				}
			}
		} else {
			rightTriggerLast = false;
			shooterSolenoid.set(DoubleSolenoid.Value.kOff);
		}
	}
	
	public boolean forward(double desiredSpeed, double desiredDist) {
	/* This will check to see if the robot has moved the desired distance
	 * forward.
	 */
		boolean distance = true;
		if (leftMotor1.getPosition() < desiredDist){
			leftMotor1.set(desiredSpeed);
			leftMotor2.set(desiredSpeed);
			leftMotor3.set(desiredSpeed);
			rightMotor1.set(-desiredSpeed);
			rightMotor2.set(-desiredSpeed);
			rightMotor3.set(-desiredSpeed);
		} else {
			distance = false;
		}
		return distance;
	}
	public boolean turningLeft(double desiredSpeed, double desiredTurnDist){
   /* Checks if the encoder for the left gear box has made it to the desired
	* distance rotating. It will keep running until the desire distance is met
	*/
		boolean rotation = true;
		 if (leftMotor1.getPosition() < desiredTurnDist){
			leftMotor1.set(desiredSpeed);
			leftMotor2.set(desiredSpeed);
			leftMotor3.set(desiredSpeed);
			rightMotor1.set(desiredSpeed);
			rightMotor2.set(desiredSpeed);
			rightMotor3.set(desiredSpeed);
		 } else {
			 rotation = false;
		 }
		 return rotation;
	}
	
	public boolean turningRight(double desiredSpeed, double desiredTurnDist){
	/* Checks if the encoder for the right gear box has made it to the desired
	 * distance rotating. It will keep running until the desire distance is met
	 */
		boolean rotation = true;
		 if (leftMotor1.getPosition() < desiredTurnDist){
			leftMotor1.set(-desiredSpeed);
			leftMotor2.set(-desiredSpeed);
			leftMotor3.set(-desiredSpeed);
			rightMotor1.set(-desiredSpeed);
			rightMotor2.set(-desiredSpeed);
			rightMotor3.set(-desiredSpeed);
		 } else {
			 rotation = false;
		 }
		 return rotation;
	}
}
