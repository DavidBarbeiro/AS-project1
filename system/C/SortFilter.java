package system.C;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import utils.FilterFramework;
import utils.FilterFramework.EndOfStreamException;
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

public class SortFilter extends FilterFramework {
	public void run() {

		// Next we write a message to the terminal to let the world know we are
		// alive...
		System.out.print("\n" + this.getName() + "::Middle Reading ");

		boolean sourceAflag=true;
		boolean sourceBflag=true;
		ArrayList<Field> array = new ArrayList<Field>();
		
		while (true) {
			if( sourceAflag==true){
				try {
					Field sourceA = new Field();
					sourceA.unmarshal(listPipeIn.get("SourceA"));
					if(sourceA.id==0){
						array.add(sourceA);
					}
				} catch (EndOfStreamException e) {
					sourceAflag=false;
					listPipeIn.get("SourceA").closePort();
				}
			}
			if( sourceBflag==true){
				try {
					Field sourceB = new Field();
					sourceB.unmarshal(listPipeIn.get("SourceB"));
					if(sourceB.id==0){
						array.add(sourceB);
					}
				} catch (EndOfStreamException e) {
					sourceBflag=false;
					listPipeIn.get("SourceB").closePort();
				}
			}
			if(!sourceAflag && !sourceBflag ){
				break;
			}
		}
		
		Comparator<Field> comparator = new Comparator<Field>() {
		    public int compare(Field c1, Field c2) {
		        return ( Double.longBitsToDouble(c2.measurement) < Double.longBitsToDouble(c1.measurement)) ? 1 : -1;
		    }
		};
		
		Collections.sort(array, comparator); // use the comparator as much as u want
		
		for (Field field : array) {
			field.marshal(listPipeOut.get("SinkFilter"));
		}
		
		listPipeOut.get("SinkFilter");

	} // run

} // MiddleFilter