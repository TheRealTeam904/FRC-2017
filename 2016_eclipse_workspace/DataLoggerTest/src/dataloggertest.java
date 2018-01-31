import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class dataloggertest {

	private static NetworkTable voltageTable;
	public static void main(String[] args) {
		NetworkTable.setServerMode();
		NetworkTable.setIPAddress("localhost");
		voltageTable = NetworkTable.getTable("shooterStatistics");
		while(true){
			voltageTable.putNumber("voltageWanted", 1);
			voltageTable.putNumber("currentVoltage", 2);
			voltageTable.putNumber("motorVoltage", 3);
			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException itsNotWorking)
			{
				System.out.println("The thread was interrupted");
			}
		}

	}

}
