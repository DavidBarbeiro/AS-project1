package system.C;

/******************************************************************************************************************
 * File:SourceFilter.java
 * Course: 17655
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *	1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example for how to use the SourceFilterTemplate to create a source filter. This particular
 * filter is a source filter that reads some input from the FlightData.dat file and writes the bytes up stream.
 *
 * Parameters: 		None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/

import java.io.*; // note we must add this here since we use BufferedReader class to read from the keyboard

import utils.FilterFramework;

public class SourceB extends FilterFramework {


	public void run() {
		
		String filename ="SubSetB.dat"; // Input data file.

		int bytesread = 0; // Number of bytes read from the input file.
		int byteswritten = 0; // Number of bytes written to the stream.
		DataInputStream in = null; // File stream reference.
		byte databyte = 0; // The byte of data read from the file

		try {
			/***********************************************************************************
			 * Here we open the file and write a message to the terminal.
			 ***********************************************************************************/

			in = new DataInputStream(new FileInputStream(filename));
			System.out.println("\n" + this.getName()
					+ "::Source reading file...");

			while(true){
				databyte = in.readByte();
				bytesread++;
				listPipeOut.get("SortFilter").WriteFilterOutputPort(databyte);
				byteswritten++;
			} //while

		} // try

		/***********************************************************************************
		 * The following exception is raised when we hit the end of input file.
		 * Once we reach this point, we close the input file, close the filter
		 * ports and exit.
		 ***********************************************************************************/

		catch (EOFException eoferr) {
			System.out.println("\n" + this.getName()
					+ "::End of file reached...");
			try {
				in.close();
				listPipeOut.get("SortFilter").closePort();
				System.out.println("\n" + this.getName()
						+ "::Read file complete, bytes read::" + bytesread
						+ " bytes written: " + byteswritten);

			}
			/***********************************************************************************
			 * The following exception is raised should we have a problem
			 * closing the file.
			 ***********************************************************************************/
			catch (Exception closeerr) {
				System.out.println("\n" + this.getName()
						+ "::Problem closing input data file::" + closeerr);

			} // catch

		} // catch

		/***********************************************************************************
		 * The following exception is raised should we have a problem opening
		 * the file.
		 ***********************************************************************************/

		catch (IOException iox) {
			System.out.println("\n" + this.getName()
					+ "::Problem reading input data file::" + iox);

		} // catch

	} // run

} // SourceFilter