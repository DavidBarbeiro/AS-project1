package system.A;

import utils.Field;
import utils.FilterFramework;
/******************************************************************************************************************
 * File:TemperatureFilter.java
 *
 * Description:
 *
 * This class serves as a filter of timestamps and Temperature fields (id 0 and 4). It also converts Farhaneit to Celsius before
 * sending the stream to the sink filter
 *
 * Parameters: None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/

public class TemperatureFilter extends FilterFramework {
	public void run() {

		// Next we write a message to the terminal to let the world know we are
		// alive...
		System.out.print("\n" + this.getName() + "::Middle Reading ");
		Field field=new Field();
		try{
			while (true) {
				field.unmarshal(listPipeIn.get("SourceFilter"));
				if(field.id==0 || field.id==4){
					if(field.id==4){
						double measurement = Double.longBitsToDouble(field.measurement);
						measurement = (measurement-32)/1.8;
						field.measurement=Double.doubleToLongBits(measurement);
					}
					field.marshal(listPipeOut.get("SinkFilter"));
				}
			}
		} catch (EndOfStreamException e)
		{
			listPipeIn.get("SourceFilter").closePort();
			listPipeOut.get("SinkFilter").closePort();
			System.out.print("\n" + this.getName() + "::Temperature Exiting; bytes read: " 
			+ field.bytesread + " bytes written: " + field.byteswritten);

		} // catch

	} // run

} // MiddleFilter