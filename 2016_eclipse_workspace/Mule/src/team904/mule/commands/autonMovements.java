package team904.mule.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class autonMovements extends CommandGroup {
	
	public autonMovements() {
		addSequential(new DriveStrafe(0.2, 2));
		addSequential(new DriveForward(0.3, 2));
		addSequential(new DriveStrafe(-0.2, 2));
	}
}
