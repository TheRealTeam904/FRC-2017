package team904.mule;

import edu.wpi.first.wpilibj.TalonSRX;
import team904.mule.subsystems.MecanumDrivetrain;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	
	public static MecanumDrivetrain drivetrain = new MecanumDrivetrain(
			new TalonSRX(1),
			new TalonSRX(2),
			new TalonSRX(3),
			new TalonSRX(4));
	
	public static
}
