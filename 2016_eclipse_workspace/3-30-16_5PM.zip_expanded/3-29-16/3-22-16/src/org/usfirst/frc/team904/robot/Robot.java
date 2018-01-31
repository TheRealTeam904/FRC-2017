package org.usfirst.frc.team904.robot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDeviceStatus;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String portCullis = "Portcullis";
    final String chevalDeFrise = "Cheval de Frise";
    final String moat = "Moat";
    final String ramparts = "Ramparts";
    final String drawbridge = "Drawbridge";
    final String sallyPort = "Sally Port";
    final String rockWall = "Rock Wall";
    final String roughTerrain = "Rough Terrain";
    final String nullMove = "Don't Go";
    final String positionTwo = "Position Two";
    final String positionThree = "Position Three";
    final String positionFour = "Position Four"; 
    final String positionFive = "Position Five";
    final String doNothing = "Do Nothing";

    String autoSelected, positionSelected;
    SendableChooser chooser, positionChooser;
    FeedbackDeviceStatus sensorStatus;
    Boolean sensorPluggedIn;
    Joystick driver, operator;
    CANTalon hawk, eagle, 
    //Arm
    ostrich, emu,
    //Lift
    chicken, turkey, goose;
    
    AnalogGyro gyro;

    boolean obstacleDone, autonDone, encodersDone, portBool, liftBool, camBool, CAMAuto;
    Encoder enc, liftEnc;
    Timer autonTime, liftTime, portTime, CAMTime;

    
    double motorLeft, motorRight, 
    yLeft, yRight, zLeft, zRight,
    scaleFactor,
    voltage,
    arm, lift,
    autonLoop,
    driveStraight, driveToObstacle,
    changeEncoderValue, currentTimer,
    liftMotor,
    camSafety, safetyBool;
    
    DigitalInput CAMSensor;
    
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addObject("Portcullis", portCullis);
        chooser.addObject("Cheval de Frise", chevalDeFrise);
        chooser.addObject("Moat", moat);
        chooser.addObject("Ramparts", ramparts);
        chooser.addObject("Drawbridge", drawbridge);
        chooser.addObject("Sally Port", sallyPort);
        chooser.addObject("Rock Wall", rockWall);
        chooser.addObject("Rough Terrain", roughTerrain);
        chooser.addDefault("Do Nothing", doNothing);
        SmartDashboard.putData("Auto choices", chooser);
        
        positionChooser = new SendableChooser();
        positionChooser.addDefault("Don't Go", nullMove);
        positionChooser.addObject("Position Two", positionTwo);
        positionChooser.addObject("Position Three", positionThree);
        positionChooser.addObject("Position Four", positionFour);
        positionChooser.addObject("Position Five", positionFive);
        SmartDashboard.putData("Position choices", positionChooser);
        
        //Init Joysticks
        driver = new Joystick(0);
        operator = new Joystick (1);
        //Init stuff
        hawk = new CANTalon(2);
        eagle = new CANTalon(3);
        ostrich = new CANTalon(7);
        emu = new CANTalon(8);
        chicken = new CANTalon(4);
        turkey = new CANTalon(5);
        goose = new CANTalon(6);
        
        //Encoders
        enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
        liftEnc = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
        
        gyro = new AnalogGyro(0);
    	//gyro.calibrate();
    	
        //Timer
        autonTime = new Timer();
        liftTime = new Timer();
        portTime = new Timer();
        CAMTime = new Timer();
        CAMAuto = false;
        CAMSensor = new DigitalInput(4);
        camSafety = .25;
        liftBool = false;
        
        goose.enable();
        CameraServer.getInstance().setQuality(50);
        CameraServer.getInstance().startAutomaticCapture("cam0");

    }
    public void autonomousInit() {
     autoSelected = (String) chooser.getSelected();
     positionSelected = (String) positionChooser.getSelected();
     System.out.println("Auto selected: " + autoSelected);
     autonLoop = 0;
     hawk.setPosition(0);
     eagle.setPosition(0);
     obstacleDone = false;
     autonDone = false;
     encodersDone = false;
     autonLoop = 1;
     driveToObstacle = 7350;
     autonTime.start();
     gyro.reset();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
	SmartDashboard.putNumber("Encoder Left", hawk.getPosition());
    SmartDashboard.putNumber("Encoder Right", eagle.getPosition()); 
	SmartDashboard.putNumber("AutonLoop", autonLoop);
    SmartDashboard.putNumber("Arm Motor", enc.get());
    if(obstacleDone == false){
    switch(autoSelected) {
    case doNothing:
   		 autonArm(0);
   		 autonDrive(0);
   		 obstacleDone = true;
   	     break;
   	//Should drive to obstacle
    case drawbridge:
    	if(autonLoop == 1){
   		 autonDrive(-.2);
   		 if(hawk.getPosition() < -(driveToObstacle + 100)){
           	 autonLoop = 2;
           	 autonDrive(0);
            }
   	 	}
    	else{
   		 autonDrive(0);
   	 	}
    	break;
    //Should drive to obstacle
    case sallyPort:
    	if(autonLoop == 1){
   		 autonDrive(-.2);
   		 if(hawk.getPosition() < -(driveToObstacle + 100)){
           	 autonLoop = 2;
           	 autonDrive(0);
            }
   	 	}
    	else{
      		 autonDrive(0);
      	}
    	break;
    //Should work- needs testing
    case portCullis:
    	//Drive to portcullis
    	 if(hawk.getPosition() > -driveToObstacle && autonLoop == 1){
    		 autonDrive(-.3);
    		 if(hawk.getPosition() < -(driveToObstacle - 100)){
            	 autonLoop = 2;
            	 autonDrive(0);
             }
    	 }
    	 //Lower Arm
    	 else if(autonLoop == 2){
    		 autonArm(-.5);
             if(enc.get() < -800){
            	 autonLoop = 3;
            	 autonArm(0);
        		 changeEncoderValue = hawk.getPosition(); 
        		 currentTimer = autonTime.get();
             }
      	 }
    	 //Drive Forward
    	 //Drive forward and pay attention to encoder values based on time.
    	 //Check time and if encoder value has not changed, start next period.
    	 else if(autonLoop == 3){
    		 autonDrive(-.3);
    		 if(currentTimer + .5 < autonTime.get()){
    			 if(changeEncoderValue == hawk.getPosition()){
    				 autonLoop = 4;
    			 }
    			 else{
    				 changeEncoderValue = hawk.getPosition(); 
            		 currentTimer = autonTime.get();
    			 }
    		 }
    		 /*if(hawk.getPosition() < -(driveToObstacle + 120)){
    			 autonLoop = 4;
             }*/
    	 }
    	 //Start raising portcullis
    	 else if(autonLoop == 4){
    		 autonArm(.75);
    		 autonDrive(-.12);
    		 if(enc.get() > -550){
            	 autonLoop = 5;
            	 autonDrive(0);
        		 autonArm(0);
             }
    	 }
    	 //Finish raising portcullis
    	 else if(enc.get() < 20 && autonLoop == 5){
    		 autonArm(.7);
    		 autonDrive(-.4);
    		 if(enc.get() > 10){
            	 autonLoop = 6;
            	 autonDrive(0);
        		 autonArm(0);
             }
    	 }
    	 //Drive to line
    	 else if(hawk.getPosition() > -(driveToObstacle + 15000) && autonLoop == 6){
    		 autonArm(0);
    		 autonDrive(-.7);
    		 if(hawk.getPosition() < -(driveToObstacle + 14000)){
            	 autonLoop = 7;
            	 autonDrive(0);
        		 autonArm(0);
             }
    	 }
    	 else if(autonLoop == 7){
    		 autonArm(0);
    		 autonDrive(0);
    		 obstacleDone = true;
    	 }
    	 break;
    //Should drive to obstacle
     case chevalDeFrise:
    	 if(autonLoop == 1){
    		 autonDrive(-.2);
    		 if(hawk.getPosition() < -(100)){
            	 autonLoop = 2;
            	 //autonDrive(0);
             }
    	 }
    	 else if(gyro.getAngle() < -14){
    		 autonDrive(-.7);
    	 }
    	 /**
    	 else if(gyro.getAngle() > -7){
    		 autonDrive(-.4);
    	 }
    	 else if(gyro.getAngle() > -2){
    		 autonDrive(0);
    	 }
    	 else if(hawk.getPosition() < -(driveToObstacle + 20000)){
    		 autonDrive(0);
      	 }
    	 else{
    		 autonDrive(0);
    	 }**/
    	 //Drive to thing
    	//lower arms to put things to the floor
    	 	//stop this when the current pull is higher?
    	//drive forward with arm lowered
    	//raise arms
    	//keep driving
    	 break;
     //I THINK this works- should be tested.
     case moat: 
    	 if(hawk.getPosition() > -driveToObstacle && autonLoop == 1){
    		 autonDrive(-.3);
    		 if(hawk.getPosition() < -(driveToObstacle - 100)){
            	 autonLoop = 2;
            	 autonDrive(0);
             }
    	 }
    	 else if(hawk.getPosition() > -(driveToObstacle + 4000)){
    		 autonDrive(-.6);
      	 }
    	 else if(hawk.getPosition() > -(driveToObstacle + 10000)){
    		 autonDrive(-.7);
      	 }
    	 else if(hawk.getPosition() > -(driveToObstacle + 20000)){
    		 autonDrive(-.6);
      	 }
      	 else{
      	     autonDrive(0.0);
      	     obstacleDone = true;
      	 }
      break;
      //Ramparts should be good.
     case ramparts:
    	 if(hawk.getPosition() > -driveToObstacle && autonLoop == 1){
    		 autonDrive(-.3);
    		 if(hawk.getPosition() < -(driveToObstacle - 100)){
            	 autonLoop = 2;
            	 autonDrive(0);
             }
    	 }
    	 else if(hawk.getPosition() > -(driveToObstacle + 18000)){
      	   	 autonDrive(-.525);
      	 }
      	 else{
      		 autonDrive(0.0);
      		 obstacleDone = true;
      	 }
      break;
      //Not Done Yet
     case rockWall:
    	 if(autonLoop == 1){
    		 autonDrive(-.25);
    		 if(hawk.getPosition() < -(100)){
            	 autonLoop = 2;
            	 //autonDrive(0);
             }
    	 }
    	 else if(gyro.getAngle() < -17){
    		 autonLoop = 3;
    		 autonDrive(-.55);
    	 }
    	 /**else if(gyro.getAngle() > -17 && autonLoop ==3){
    		 autonDrive(-.65);
    	 }
    	 /**if(autonLoop == 1){
    		 autonDrive(-.4);
    		 if(hawk.getPosition() < -(driveToObstacle + 500)){
            	 autonLoop = 2;
            	 autonDrive(0);
             }
    	 }
    	 //Do everything else- not correct rn
    	 else if(autonLoop == 2){
   	 		autonDrive(-.425);
	   	   	  if(hawk.getPosition() < -(driveToObstacle + 1000)){
	   	   		  autonLoop = 3;
	   	   		  autonDrive(0);
	   	   	  }
   	     }
    	 else if(autonLoop == 3){
      	   	  autonDrive(-.325);
      	   	  if(hawk.getPosition() < -(driveToObstacle + 1500)){
      	   		  autonLoop = 4;
      	   		  autonDrive(0);
      	   	  }
      	 }

   	 else{
   	   	  autonDrive(0.0);
   	      obstacleDone = true;
   	     }**/
     break;
    //RoughtTerrain not done yet- Might need to go faster
     case roughTerrain:
    	//Drive to Obstacle
    	 if(hawk.getPosition() > -driveToObstacle && autonLoop == 1){
    		 autonDrive(-.4);
    		 if(hawk.getPosition() < -(driveToObstacle - 100)){
            	 autonLoop = 2;
            	 autonDrive(0);
             }
    	 }
    	 if(hawk.getPosition() > -(driveToObstacle + 17000)){
	   	  autonDrive(-.5);
	   	  autonLoop ++;
		 }
	   	 else {
		  autonDrive(0.0); 
		  obstacleDone = true;
		 }
		 break;
     default:
      //Do Nothing if none of these are selected
    	 obstacleDone = true;	
         break;
     }
    }
    else if(obstacleDone == true && encodersDone == false){
    	 hawk.setPosition(0);
  	     eagle.setPosition(0);
  	     encodersDone = true;
  	     autonLoop = 0;
    }
    else if(obstacleDone == true && autonDone == false){
    switch(positionSelected){
	 case positionTwo:
		 if(hawk.getPosition() > -19000){
      	   	 autonDrive(-.325);
      	}
		else if(eagle.getPosition() < 23000){
      	   	 hawkDrive(.35);
      	     eagleDrive(-.9);
      	}
		//Driving up ramp and shooting
		else if(eagle.getPosition() < 32000){
		   	 autonDrive(-.25);
		     }
		else if(enc.get() > -400){
			autonDrive(0);
			autonArm(-.5);
           if(enc.get() < -300){
          	 emu.set(1);
        }
		}
		else{
		  autonArm(0.0);
		  emu.set(0);
	   	  autonDrive(0.0); 
	   	  autonDone = true;
	    }
		break;
	 case positionThree:
		 if(hawk.getPosition() > -19000){
      	   	 autonDrive(-.325);
      	}
		else if(eagle.getPosition() < 23000){
      	   	 hawkDrive(.35);
      	     eagleDrive(-.9);
      	}
		//Driving up ramp and shooting
		else if(eagle.getPosition() < 32000){
		   	 autonDrive(-.25);
		     }
		else if(enc.get() > -400){
			autonDrive(0);
			autonArm(-.5);
           if(enc.get() < -300){
          	 emu.set(1);
        }
		}
		else{
		  autonArm(0.0);
		  emu.set(0);
	   	  autonDrive(0.0); 
	   	  autonDone = true;
	    }
		break;
	 case positionFour:
		if(autonLoop == 0){
      	   	 autonDrive(-.325);
      	   	 if(hawk.getPosition() < -7400){
      	   		 autonLoop = 1;
      	   	 }
      	}
		else if(autonLoop == 1){
      	   	 hawkDrive(-.5);
      	     eagleDrive(.35);
      	     if(eagle.getPosition() < 7300){
    	   		 autonLoop = 2;
    	   	 }
      	}
		else if(autonLoop == 2){
      	   	 autonDrive(-.325);
      	   	 if(hawk.getPosition() < -23500){
      	   		 autonLoop = 3;
      	   	 }
      	}
		else if(autonLoop == 3){
     	   	 hawkDrive(.35);
     	     eagleDrive(-.5);
     	     if(eagle.getPosition() < 7300){
   	   		 autonLoop = 4;
   	   	 }
     	}
		//Driving up ramp and shooting
		else if(eagle.getPosition() < 32000){
		   	 autonDrive(-.25);
		     }
		else if(enc.get() > -400){
			autonDrive(0);
			autonArm(-.5);
           if(enc.get() < -300){
          	 emu.set(1);
        }
		}
		else{
		  autonArm(0.0);
		  emu.set(0);
	   	  autonDrive(0.0); 
	   	  autonDone = true;
	    }
		break;
	 case positionFive:
		if(hawk.getPosition() > -19000){
      	   	 autonDrive(-.325);
      	}
		else if(eagle.getPosition() < 23000){
      	   	 hawkDrive(.35);
      	     eagleDrive(-.9);
      	}
		//Driving up the ramp and shooting
		else if(eagle.getPosition() < 32000){
		   	 autonDrive(-.25);
		     }
		else if(enc.get() > -400){
			autonDrive(0);
			autonArm(-.5);
            if(enc.get() < -300){
           	 emu.set(1);
         }
     	}
		else{
		  autonArm(0.0);
		  emu.set(0);
	   	  autonDrive(0.0); 
	   	  autonDone = true;
	    }
		break;
	 default:
		break;
	 }
    }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit(){
    	gyro.reset();
    }
    public void teleopPeriodic() {	
        SmartDashboard.putNumber("Gyro", gyro.getAngle());
    	SmartDashboard.putBoolean("Sensor CAM", CAMSensor.get());
    	SmartDashboard.putNumber("AutonLoop", autonLoop);
    	
    	drivebase();
        armDrive();
        armPush();
        teleLift();
        //Portcullis automatic
        if(operator.getRawButton(4)){
        	portcullis();
        	autonLoop = 1;
        	portBool = true;
        	portTime.reset();
        	portTime.start();
        	hawk.setPosition(0);
            eagle.setPosition(0);
        	changeEncoderValue = hawk.getPosition();
        	currentTimer = portTime.get();
        } 
        
        if(operator.getRawButton(1)){
        	portBool = false;
        }
        if(portBool == true){
        	portcullis();
        }
        //Cam Auto
        if(operator.getRawButton(5)){
        	CAMAuto();
        	camBool = true;
        }
        if(camBool == true){
        	CAMAuto();
        }
        //Arm Encoder Reset
        if(driver.getRawButton(11) && driver.getRawButton(12)){
        	enc.reset();
        }
        //Lift Automatic
        if(operator.getRawButton(3)){
        	lift();
        	autonLoop = 1;
        	liftTime.reset();
        	liftTime.start();
        	liftBool = true;
        }
        else if(operator.getRawButton(6)){
        	turkey.set(1);
       	 	chicken.set(1);
        }
        else{
        	turkey.set(0);
       	 	chicken.set(0);
        }
        if(operator.getRawButton(2)){
        	liftBool = false; 
        	goose.enable();
        	goose.set(0);
        }
        if(liftBool == true){
        	lift();
        }
        

    }
    /**
     * This function controls the drive during Teleop mode
     */
    public void drivebase(){
    	if(-driver.getY() > .1){
    		yLeft = 1.11*(-driver.getY())-.11;
    		yRight = 1.11*(-driver.getY())-.11;
    	}
    	else if(-driver.getY() < -.1){
    		yLeft = 1.11*(-driver.getY()) +.11;
    		yRight = 1.11*(-driver.getY()) +.11;
    	}
    	else{
    		yLeft = 0;
    		yRight = 0;
    	}
    	if(driver.getZ() > .1){
    		zLeft = driver.getZ();
    		zRight = -driver.getZ();

    		//zLeft = 1.11*(driver.getZ())-.11;
    		//zRight = -1.11*(driver.getZ())+.11;
    	}
    	
    	else if(driver.getZ() < -.1){
    		zLeft = driver.getZ();
    		zRight = -driver.getZ();
    		//zLeft = -1.11*(driver.getZ())+.11;
    		//zRight = 1.11*(driver.getZ())-.11;
		}
    	else{
    		zLeft = 0;
    		zRight = 0;
    	}
    	motorLeft = yLeft + zLeft;
    	motorRight = yRight + zRight;
    	if((Math.max(Math.abs(motorLeft), Math.abs(motorRight)) > 1)){
    		scaleFactor = Math.max(Math.abs(motorLeft), Math.abs(motorRight));
    	}
    	else{
    		scaleFactor = 1;
    	}
    	motorLeft = motorLeft/scaleFactor;
    	motorRight = motorRight/scaleFactor;
		
		//Put out the current data
        SmartDashboard.putNumber("Encoder Left", hawk.getPosition());
        SmartDashboard.putNumber("Encoder Right", eagle.getPosition());
        SmartDashboard.putNumber("Left Motor", motorLeft);
        SmartDashboard.putNumber("Right Motor", -motorRight);
        
        //Set the motors
    	hawk.set(motorLeft);
        eagle.set(-motorRight);
    }
    
    //This controls the arm
    public void armDrive(){
         SmartDashboard.putNumber("Arm Motor", enc.get());
	     if(operator.getRawAxis(5) > .1){
	    	 ostrich.set(operator.getRawAxis(5));
	    	 SmartDashboard.putNumber("arm", operator.getRawAxis(5));
	     }
	     else if(operator.getRawAxis(5) < -.1){
	    	 ostrich.set(operator.getRawAxis(5));
	    	 SmartDashboard.putNumber("arm", operator.getRawAxis(5));
	     }
	     else{
	    	 ostrich.set(0);
	    	 SmartDashboard.putNumber("arm", 0);
	     }
   }
    
    //This controls the CAM
    public void armPush(){
    	if(operator.getRawAxis(3) > .1){
    		emu.set(1);
    	}
    	else if(operator.getRawAxis(2) > .1){
    		emu.set(-1);
    	}
    	else if(CAMSensor.get() == false){
 		   emu.set(.5);
 	   	}
    	else{
    		emu.set(0);
    	}
   }
   //Move the CAM to the correct position
   public void CAMAuto(){
	   if(CAMSensor.get() == false){
		   emu.set(.5);
	   }
	   else{
		   emu.set(0);
		   camBool = false;
	   }
	   if(operator.getRawAxis(3) > 0 || operator.getRawAxis(2) > 0){
		   emu.set(0);
		   camBool = false;
	   }
   }
    //This controls lifting the robot
    public void lift(){
    	/**if(enc.get() > -420){
    		turkey.set(0);
	    	chicken.set(0);
	    	goose.set(0);
    	}**/
    	if(liftTime.get() < 7.0){
    		turkey.set(-1);
	    	chicken.set(-1);
    		goose.disable();
	    	//if(liftTime.get() > 1.0){
	    		//goose.set(((liftMotor/(Math.abs(liftMotor)) * -.2)));
	    		//goose.set(.2);
	    		//SmartDashboard.putNumber("Goose", goose.get());
	    	//}
    	}
    	else{
    		turkey.set(0);
	    	chicken.set(0);
	    	goose.enable();
	    	goose.set(0);
	    	liftBool = false;
    	}

    }
    public void teleLift(){
    	/**if(enc.get() > -490 && Math.abs(operator.getRawAxis(1)) > .1){
   		 	goose.set(0);
    	}**/
    	if(Math.abs(operator.getRawAxis(1)) > .3){
	    	 goose.set(-operator.getRawAxis(1));
	    	 liftMotor = operator.getRawAxis(1);
    	}
    	else{
    		goose.set(0);
    	}
    }
    
    //This is the portcullis drive through
    public void portcullis(){
    	//Check time and if encoder value has not changed, start next period.
   	 if(autonLoop == 1){
   		 autonDrive(-.2);
   		 if(currentTimer + .5 < portTime.get()){
   			 if(changeEncoderValue == hawk.getPosition()){
   				 autonLoop = 2;
   			 }
   			 else{
   				 changeEncoderValue = hawk.getPosition(); 
           		 currentTimer = autonTime.get();
   			 }
   		 }
   	 }
   	 //Start raising portcullis
   	 else if(enc.get() < -530 && autonLoop == 2){
   		 autonArm(.75);
   		 autonDrive(-.12);
   		 if(enc.get() > -550){
           	 autonLoop = 3;
           	 autonDrive(0);
       		 autonArm(0);
            }
   	 }
   	 //Finish raising portcullis
   	 else if(enc.get() < 20 && autonLoop == 3){
   		 autonArm(.7);
   		 autonDrive(-.4);
   		 if(enc.get() > 10){
           	 autonLoop = 4;
           	 autonDrive(0);
       		 autonArm(0);
            }
   	 }
   	 //Drive to line
   	 else if(hawk.getPosition() > -(changeEncoderValue + 25000) && autonLoop == 4){
   		 autonArm(0);
   		 autonDrive(-.7);
   		 if(hawk.getPosition() < -(changeEncoderValue + 24000)){
           	 autonLoop = 5;
           	 autonDrive(0);
       		 autonArm(0);
            }
   	 }
   	 else if(autonLoop == 5){
   		 autonArm(0);
   		 autonDrive(0);
   		 portBool = false;
   	 }
    }
    
    /**
     * These functions controls the autonomous drives
     */
    public void autonDrive(double speed){
    	hawk.set(-speed);
        eagle.set(speed);
    }
    public void hawkDrive(double speed){
    	hawk.set(-speed);
    }
    public void eagleDrive(double speed){
    	eagle.set(speed);
    }
    public void autonArm(double speed){
	     ostrich.set(speed);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
}






/**
 * This will be where the dance function goes
 * 
 * Actually, I'm not sure where it will go.
 * But it should go somewhere, in here (the code)
 * The single lines are a helpful reminder to you, dear programmer, to implement this dance function
 * 
 * THIS PART GOES TO LINE 36
 * "final String Dance = "Dance, baby, Dance!";"
 * THIS PART GOES TO LINE 86
 * "chooser.addDefault("Dance, baby, Dance!", Dance);
 * 
 * THIS GOES TO LINE 320ish
 * case Dance:
 */
