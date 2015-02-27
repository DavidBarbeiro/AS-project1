package utils;

import java.nio.ByteBuffer;

import utils.FilterFramework.EndOfStreamException;
import utils.FilterFramework.PipeIn;
import utils.FilterFramework.PipeOut;

/******************************************************************************************************************
 * File:Field.java
 *
 * Description:
 *
 * This class represents the atomic structure that flows through the pipeline: a Field represented by an ID
 * and a measurement. We thought of two options: we could make this class serializable and send each field
 * as an object, or we could make two methods, marshal and unmarshal, to send data as bytes and commute and decommute
 * the id and measurement. We chose the second option because this way we could send the information as bytes
 * to a system in a different language. With serialized objects that would be impossible, because those objects are specific
 * to the java language.
 *
 * Parameters: None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/

public class Field{
	//if id is negative it means that this Field is a wildpoint
	public int id;
	public long measurement;
	
	public int bytesread = 0; // This is the number of bytes read from the stream
	public int byteswritten = 0; //This is the number of bytes written to the stream
	
	public Field() {
		// TODO Auto-generated constructor stub
	}
	
	public Field(int id,long measurement) {
		this.id=id;
		this.measurement=measurement;
	}
	

	/****************************************************************************
	 * // This method reads from PipeIn and decomutes the ID and Measurement
	 *****************************************************************************/
	public void unmarshal(PipeIn pipeIn) throws EndOfStreamException  {
		
		int MeasurementLength = 8; // This is the length of all measurements
									// (including time) in bytes
		int IdLength = 4; // This is the length of IDs in the byte stream
	
		byte databyte = 0; // This is the data byte read from the stream
	
		long measurement; // This is the word used to store all measurements -
							// conversions are illustrated.
		int id; // This is the measurement id
		int i; // This is a loop counter

		/***************************************************************************
		 * // We know that the first data coming to this filter is going
		 * to be an ID and // that it is IdLength long. So we first
		 * decommutate the ID bytes.
		 ****************************************************************************/

		id = 0;

		for (i = 0; i < IdLength; i++) {
			databyte = pipeIn.ReadFilterInputPort();
																			

			id = id | (databyte & 0xFF); // We append the byte on to
											// ID...

			if (i != IdLength - 1) // If this is not the last byte, then
									// slide the
			{ // previously appended byte to the left by one byte
				id = id << 8; // to make room for the next byte we
								// append to the ID

			} // if

			bytesread++; // Increment the byte count

		} // for

		/****************************************************************************
		 * // Here we read measurements. All measurement data is read as
		 * a stream of bytes // and stored as a long value. This permits
		 * us to do bitwise manipulation that // is neccesary to convert
		 * the byte stream into data words. Note that bitwise //
		 * manipulation is not permitted on any kind of floating point
		 * types in Java. // If the id = 0 then this is a time value and
		 * is therefore a long value - no // problem. However, if the id
		 * is something other than 0, then the bits in the // long value
		 * is really of type double and we need to convert the value
		 * using // Double.longBitsToDouble(long val) to do the
		 * conversion which is illustrated. // below.
		 *****************************************************************************/

		measurement = 0;

		for (i = 0; i < MeasurementLength; i++) {
			databyte = pipeIn.ReadFilterInputPort();
			measurement = measurement | (databyte & 0xFF); // We append
															// the byte
															// on to
															// measurement...

			if (i != MeasurementLength - 1) // If this is not the last
											// byte, then slide the
			{ // previously appended byte to the left by one byte
				measurement = measurement << 8; // to make room for the
												// next byte we append
												// to the
												// measurement
			} // if

			bytesread++; // Increment the byte count

		} // if
		
		this.id=id;
		this.measurement=measurement;

	}
	
	/****************************************************************************
	 * // This method allocates two byte arrays in which the id and measurement
	 * are stored. Then, those bytes are sent to the right filter using PipeOut
	 *****************************************************************************/
	
	public void marshal(PipeOut pipeOut){
		byte[] array1 = ByteBuffer.allocate(4).putInt(this.id).array();
		byte[] array2 = ByteBuffer.allocate(8).putLong(this.measurement).array();
		
		for (byte b : array1) {
			pipeOut.WriteFilterOutputPort(b);
			byteswritten++;
		}
		for (byte b : array2) {
			pipeOut.WriteFilterOutputPort(b);
			byteswritten++;
		}
	}
}
