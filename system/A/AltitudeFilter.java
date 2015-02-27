package system.A;

import utils.Field;
import utils.FilterFramework;
/******************************************************************************************************************
 * File:AltitudeFilter.java
 *
 * Description:
 *
 * This class serves as a filter of timestamps and altitude fields (id 0 and 2). It also converts feet to meters before
 * sending the stream to the sink filter
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

		System.out.print("\n" + this.getName() + "::Altitude Reading ");
		
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

} // AltitudeFilter