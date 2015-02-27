package system.A;



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
		
		
		boolean temperatureFlag=true;
		boolean altitudeFlag=true;
		Field temperatureField=new Field();
		Field altitudeField=new Field();
		String row="";
		
		while (true) {
			if( temperatureFlag==true){
				try {
					temperatureField.unmarshal(listPipeIn.get("TemperatureFilter"));
				} catch (EndOfStreamException e) {
					temperatureFlag=false;
					listPipeIn.get("TemperatureFilter").closePort();
				}
			}
			if( altitudeFlag==true){
				try {
					altitudeField.unmarshal(listPipeIn.get("AltitudeFilter"));
				} catch (EndOfStreamException e) {
					altitudeFlag=false;
					listPipeIn.get("AltitudeFilter").closePort();
				}
			}
			
			if(!temperatureFlag && !altitudeFlag){
				System.out.print( "\n" + this.getName() + "::Sink Exiting;" );
				break;
			}

			if(temperatureField.id==0){
				row="\n";
				timeStamp.setTimeInMillis(temperatureField.measurement);
				row+="Timestamp: " + timeStampFormat.format(timeStamp.getTime());
			}else{
				row+=" temperature: "+ Double.longBitsToDouble(temperatureField.measurement);
				row+=" altitude: " + Double.longBitsToDouble(altitudeField.measurement);
				System.out.println(row);
			}


		} // while
		

	} // run
	


} // SingFilter