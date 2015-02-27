package system.A;

/******************************************************************************************************************
 * File:Plumber.java
 * Description:
 *
 * This class serves instantiates and connects a set of four filters: a source, an altitude filter, a temperature filter 
 * and a sink filter
 *
 * Parameters: None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/
public class Plumber {
	public static void main(String argv[]) {
		/****************************************************************************
		 * Here we instantiate three filters.
		 ****************************************************************************/

		SourceFilter source = new SourceFilter();
		TemperatureFilter temperatureFilter = new TemperatureFilter();
		AltitudeFilter altitudeFilter = new AltitudeFilter();
		SinkFilter sink = new SinkFilter();

		/****************************************************************************
		 * Here we connect the filters starting with the sink filter (Filter 1)
		 * which we connect to Filter2 the middle filter. Then we connect
		 * Filter2 to the source filter (Filter3).
		 ****************************************************************************/

		sink.Connect(altitudeFilter);
		sink.Connect(temperatureFilter);
		temperatureFilter.Connect(source);
		altitudeFilter.Connect(source);

		/****************************************************************************
		 * Here we start the filters up. All-in-all,... its really kind of
		 * boring.
		 ****************************************************************************/
		source.start();
		altitudeFilter.start();
		temperatureFilter.start();
		sink.start();



	} // main

} // Plumber