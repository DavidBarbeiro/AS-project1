package system.B;

import java.util.ArrayList;
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

public class PressureFilter extends FilterFramework {
	public void run() {

		int byteswritten=0;
		int bytesread=0;
		// Next we write a message to the terminal to let the world know we are
		// alive...

		System.out.print("\n" + this.getName() + "::Pressure Reading ");
		
		ArrayList<Field> wildpoints = new ArrayList<Field>();
		Field field;
		boolean isFirst=true;
		double lastValidPoint=0;
		Field timestamp=null;
		try{
			while (true) {
				field = new Field();
				field.unmarshal(listPipeIn.get("SourceFilter"));
				bytesread+=field.bytesread;
				if(field.id==0 || field.id==3){
					if(field.id==0){
						timestamp=field;
					}
					else{
						double measurement = Double.longBitsToDouble(field.measurement);
						if(measurement<50 || measurement>80){
							wildpoints.add(timestamp);
							wildpoints.add(field);
						}
						else{
							lastValidPoint=measurement;
							if(isFirst){
								isFirst=false;
								for (int i=0; i<wildpoints.size();i++) {
									Field wildpoint=wildpoints.get(i);
									if(wildpoint.id==3){
										wildpoint.measurement = Double.doubleToLongBits(lastValidPoint);
										wildpoint.id*=-1;
									}
									wildpoint.marshal(listPipeOut.get("SinkFilter"));
									byteswritten+=wildpoint.byteswritten;
									wildpoints.remove(i--);
								}
							}else{
								double mean=(field.measurement+lastValidPoint)/2;
								for (int i=0; i<wildpoints.size();i++) {
									Field wildpoint=wildpoints.get(i);
									if(wildpoint.id==3){
										wildpoint.measurement=Double.doubleToLongBits(mean);
										wildpoint.id*=-1;
									}
									wildpoint.marshal(listPipeOut.get("SinkFilter"));
									byteswritten+=wildpoint.byteswritten;
									wildpoints.remove(i--);
								}
							}
							timestamp.marshal(listPipeOut.get("SinkFilter"));
							byteswritten+=timestamp.byteswritten;
							field.marshal(listPipeOut.get("SinkFilter"));
							byteswritten+=field.byteswritten;
						}
					}
				}
			}
		} catch (EndOfStreamException e)
		{
			for (int i=0; i<wildpoints.size();i++) {
				Field wildpoint=wildpoints.get(i);
				if(wildpoint.id==3){
					wildpoint.measurement = Double.doubleToLongBits(lastValidPoint);
					wildpoint.id*=-1;
				}
				wildpoint.marshal(listPipeOut.get("SinkFilter"));
				byteswritten+=wildpoint.byteswritten;
				wildpoints.remove(i--);
			}
			
			listPipeIn.get("SourceFilter").closePort();
			listPipeOut.get("SinkFilter").closePort();
			System.out.print("\n" + this.getName() + "::Pressure Exiting; bytes read: " 
			+ bytesread + " bytes written: " + byteswritten);

		} // catch
			

	} // run

} // MiddleFilter