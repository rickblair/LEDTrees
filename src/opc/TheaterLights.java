package opc;

import opc.Animation;
import opc.OpcClient;
import opc.OpcDevice;
import opc.PixelStrip;

/**
 * Pairs of lights traveling down the strip.
 */
public class TheaterLights extends Animation {
	
	protected static final int FAST = 200; // twenty pixels per second
	protected static final int SLOW = 1000; // one pixel per second
	
	int N = 2;
	int state;
	long timePerCycle = 100L;
	int color;
	
	/** Time for the next state change. */
	long changeTime;
	
	public TheaterLights(int c) {
		color = c;
	}

	@Override
	public void reset(PixelStrip strip) {
		state = 0;
		changeTime = millis();
	}

	@Override
	public boolean draw(PixelStrip strip) {
		if (millis() < changeTime) { return false; }
			
		state = (state + 1) % (N * 2);
		for (int i=0; i<strip.getPixelCount(); i++)  {
			int j = (i+state) % (N * 2);
			strip.setPixelColor(i, j>=N ? color : BLACK);
		}
		
		changeTime = millis() + timePerCycle;
		return true;
	}
	
	/**
	 * @param n value between -1.0 and 1.0;
	 */
	@Override
	public void setValue(double n) {
		timePerCycle = Math.round(SLOW - (SLOW - FAST) * Math.abs(n));
		timePerCycle = Math.min(Math.max(FAST, timePerCycle), SLOW);
	}
	
	
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// This is just test code.  Write your own code that sets up your pixel configuratio,
	// animation combinations, and interactions.
	
	public static void main(String[] args) throws Exception {
		String FC_SERVER_HOST = System.getProperty("fadecandy.server", "xmaslights.local");
		int FC_SERVER_PORT = Integer.parseInt(System.getProperty("fadecandy.port", "7890"));
		int STRIP1_COUNT = Integer.parseInt(System.getProperty("fadecandy.strip1.count", "200"));
		
		OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
		OpcDevice fadeCandy = server.addDevice();
		PixelStrip strip1 = fadeCandy.addPixelStrip(0, 100);
		PixelStrip strip2 = fadeCandy.addPixelStrip(0, 100);
		System.out.println(server.getConfig());
		
		TheaterLights a = new TheaterLights(0x00DD00);
		TheaterLights b = new TheaterLights(0xDD0000);
		a.setValue(0.8);
		strip1.setAnimation(a);
		b.setValue(0.8);
		strip2.setAnimation(b);
		
		for (int i=0; i<1000; i++) {
			server.animate();
			Thread.sleep(FAST/2);
		}

		a = new TheaterLights(0xDD0000);
		a.setValue(0.8);
		strip1.setAnimation(a);
		
		for (int i=0; i<1000; i++) {
			server.animate();
			Thread.sleep(SLOW/2);
		}
		
		strip1.clear();
		server.show();
		server.close();
	}

}
