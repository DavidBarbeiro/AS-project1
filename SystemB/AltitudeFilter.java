package SystemB;

import system.A.Field;
import utils.FilterFramework;
/******************************************************************************************************************
 * File:MiddleFilter.java Course: 17655 Project: Assignment 1 Copyright:
 * Copyright (c) 2003 Carnegie Mellon University Versions: 1.0 November 2008 -
 * Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example for how to use the FilterRemplate to create a
 * standard filter. This particular example is a simple "pass-through" filter
 * that reads data from the filter's input port and writes data out the filter's
 * output port.
 *
 * Parameters: None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/

public class AltitudeFilter extends FilterFramework {
	public void run() {

		// Next we write a message to the terminal to let the world know we are
		// alive...

		System.out.print("\n" + this.getName() + "::Middle Reading ");
		
		Field field=new Field();
		try{
			while (true) {
				field.unmarshal(listPipeIn.get("SourceFilter"));
				if(field.id==0 || field.id==2){
					if(field.id==2){
						double measurement = Double.longBitsToDouble(field.measurement);
						measurement = measurement*0.3048;
						field.measurement=Double.doubleToLongBits(measurement);
					}
					field.marshal(listPipeOut.get("SinkFilter"));
				}
			}
		} catch (EndOfStreamException e)
		{
			listPipeIn.get("SourceFilter").closePort();
			listPipeOut.get("SinkFilter").closePort();
			System.out.print("\n" + this.getName() + "::Altitude Exiting; bytes read: " 
			+ field.bytesread + " bytes written: " + field.byteswritten);

		} // catch
			

	} // run

} // MiddleFilter