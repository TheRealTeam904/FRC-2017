package datalogger;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class DataLogger {

	private static NetworkTable dataTable;
	private static CSVWriter fileWriter;
	public static void main(String[] args) {
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("localhost");
		dataTable = NetworkTable.getTable("shooterStatistics");
		try
		{
			fileWriter = new CSVWriter(new FileWriter("shooterStatistics.csv"));
		}
		catch(IOException somethingBroke)
		{
			System.out.println("File could not be found");
			return;
		}
		while (true){
			double voltageWanted = dataTable.getNumber("voltageWanted", Double.NaN);
			double currentVoltage = dataTable.getNumber("currentVoltage", Double.NaN);
			double motorVoltage = dataTable.getNumber("motorVoltage", Double.NaN);
			long currentTime = System.currentTimeMillis();
			String voltageNeeded = Double.toString(voltageWanted);
			String voltageCurrently = Double.toString(currentVoltage);
			String voltageOfMotor = Double.toString(motorVoltage);
			String timeAsOfNow = Long.toString(currentTime);
			String[] voltageList = new String[4];
			voltageList[0] = voltageNeeded;
			voltageList[1] = voltageCurrently;
			voltageList[2] = voltageOfMotor;
			voltageList[3] = timeAsOfNow;
			fileWriter.writeNext(voltageList);
			try
			{
				Thread.sleep(10);
			}	
			catch(InterruptedException somethingElseBroke)
			{
				System.out.println("The thread was interrupted");
			}
			
			}

	}

}