package system.B;

import utils.Field;
import utils.FilterFramework;


/******************************************************************************************************************
 * File:SinkFilter.java
 * Course: 17655
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *	1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example for using the SinkFilterTemplate for creating a sink filter. This particular
 * filter reads some input from the filter's input port and does the following:
 *
 *	1) It parses the input stream and "decommutates" the measurement ID
 *	2) It parses the input steam for measurments and "decommutates" measurements, storing the bits in a long word.
 *
 * This filter illustrates how to convert the byte stream data from the upstream filterinto useable data found in
 * the stream: namely time (long type) and measurements (double type).
 *
 *
 * Parameters: 	None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/
import java.util.*; // This class is used to interpret time words
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat; // This class is used to format and write time in a string format.

public class SinkFilter extends FilterFramework {

	public void run() {
		/************************************************************************************
		 * TimeStamp is used to compute time using java.util's Calendar class.
		 * TimeStampFormat is used to format the time value so that it can be
		 * easily printed to the terminal.
		 *************************************************************************************/

		Calendar timeStamp = Calendar.getInstance();
		SimpleDateFormat timeStampFormat = new SimpleDateFormat(
				"yyyy MM dd::hh:mm:ss:SSS");

		/*************************************************************
		 * First we announce to the world that we are alive...
		 **************************************************************/

		System.out.print("\n" + this.getName() + "::Sink Reading ");

		boolean temperatureFlag = true;
		boolean altitudeFlag = true;
		boolean pressureFlag = true;
		Field temperatureField = new Field();
		Field altitudeField = new Field();
		Field pressureField = new Field();
		String row = "";

		try {
			File outputB = new File("OutputB.dat");

			// if file doesnt exists, then create it
			if (!outputB.exists()) {
				outputB.createNewFile();
			}

			FileWriter fwOutputB = new FileWriter(outputB.getAbsoluteFile());
			BufferedWriter bwOutputB = new BufferedWriter(fwOutputB);

			while (true) {
				if (temperatureFlag == true) {
					try {
						temperatureField.unmarshal(listPipeIn
								.get("TemperatureFilter"));
					} catch (EndOfStreamException e) {
						temperatureFlag = false;
						listPipeIn.get("TemperatureFilter").closePort();
					}
				}
				if (altitudeFlag == true) {
					try {
						altitudeField.unmarshal(listPipeIn
								.get("AltitudeFilter"));
					} catch (EndOfStreamException e) {
						altitudeFlag = false;
						listPipeIn.get("AltitudeFilter").closePort();
					}
				}
				if (pressureFlag == true) {
					try {
						pressureField.unmarshal(listPipeIn
								.get("PressureFilter"));
					} catch (EndOfStreamException e) {
						pressureFlag = false;
						listPipeIn.get("PressureFilter").closePort();
					}
				}

				if (!temperatureFlag && !altitudeFlag && !pressureFlag) {
					System.out.print("\n" + this.getName() + "::Sink Exiting;");
					break;
				}

				if (pressureField.id == 0) {
					row = "";
					timeStamp.setTimeInMillis(pressureField.measurement);
					row += "Timestamp: "
							+ timeStampFormat.format(timeStamp.getTime());
				} else {
					row += " temperature: "
							+ Double.longBitsToDouble(temperatureField.measurement);
					row += " altitude: "
							+ Double.longBitsToDouble(altitudeField.measurement);
					row += " pressure: "
							+ Double.longBitsToDouble(pressureField.measurement);
					if (pressureField.id < 0) {
						row += " *";
					}
					bwOutputB.write(row+"\n");
					System.out.println(row);
				}
			} // while
			bwOutputB.close();
		} catch (IOException e) {
			System.out.println("IO exception: some problem with file creation");
			e.printStackTrace();
		}

	} // run

} // SingFilter