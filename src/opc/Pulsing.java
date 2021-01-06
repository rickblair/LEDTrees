package opc;

import opc.Animation;
import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;

/**
 * Example animation that pulses all pixels through an array of colors.
 */
public class Pulsing extends Animation {

	/** Milliseconds for each pulse cycle. */
	long timeCycle = 10000;
	int color[] = { 
			makeColor(0, 0xdd, 0), // Green
			makeColor(0xdd, 0, 0), // Red
//			makeColor(64, 64, 64), // White
	};
	int colorLen = color.length;

	
	
	
	@Override
	public void reset(PixelStrip strip) {
		// Nothing
	}
	private int color_num = 0;
	@Override
	public boolean draw(PixelStrip strip) {
		long currentTime = millis() % timeCycle;
		boolean isBlack = false;
		boolean reverse = false;
		for (int p = 0; p < strip.getPixelCount(); p++) {

			int timeShift = (int)(color_num * (timeCycle / colorLen));
			int brightness = pulseOverTime((currentTime + timeShift) % timeCycle);
			int c1 = color[color_num];
			int c2 = fadeColor(c1, brightness);
			strip.setPixelColor(p, c2);
			isBlack = c2 == 0;
		}

		//color_num = (color_num +1) % colorLen;
		if(isBlack)
			{
			color_num  = (color_num + 1) % colorLen;
			reverse = true;

			}
		return true;
	}
	
	
	
	
	/**
	 * Return a brightness value as a function of time. The input value is the
	 * number of milliseconds into the cycle, from zero to timeCycle. 
	 * Cycle over a sine function, so it's nice and smooth.
	 * 
	 * @param timeNow time within the cycle.
	 * @return brightness value from 0 to 255
	 */
	private int pulseOverTime(long timeNow) {
	  double theta = 6.283 * timeNow / timeCycle;   // Angle in radians
	  double s = (Math.sin(theta) + 1.0) / 2.0;     // Value from 0.0 to 1.0
	  return (int)Math.round(s * 256);
	}

	
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// This is just test code.  Write your own code that sets up your pixel configuratio,
	// animation combinations, and interactions.
	
	public static void main(String[] args) throws Exception {
		String FC_SERVER_HOST = System.getProperty("fadecandy.server", "smalltree.local");
		int FC_SERVER_PORT = Integer.parseInt(System.getProperty("fadecandy.port", "7890"));
		int STRIP1_COUNT = Integer.parseInt(System.getProperty("fadecandy.strip1.count", "100"));
		
		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
		PixelStrip strip1 = fadeCandy.addPixelStrip(0, STRIP1_COUNT);
		System.out.println(server.getConfig());

		Animation a = new Pulsing();
		strip1.setAnimation(a);

		for (int i = 0; i < 1000; i++) {
			server.animate();
			Thread.sleep(100);
		}

		server.clear();
		server.show();
		server.close();
	}

}
