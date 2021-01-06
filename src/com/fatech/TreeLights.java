package com.fatech;
import opc.*;

public class TreeLights
{

    public static void main(String[] args)
            throws InterruptedException
    {
    String host = "xmaslights.local";
    int port = 7890;
        TheaterLights redLights = new TheaterLights(0xdd0000);
        TheaterLights greenLights = new TheaterLights(0x00dd00);
    OpcClient server = new OpcClient(host, port);
    OpcDevice fc = server.addDevice();
    PixelStrip s1 = fc.addPixelStrip(0,50);
    PixelStrip s2 = fc.addPixelStrip(0,50);
    PixelStrip s3 = fc.addPixelStrip(0,50);
    PixelStrip s4 = fc.addPixelStrip(0,50);
  //  PixelStrip s5 = fc.addPixelStrip(0,10);
  //  PixelStrip s6 = fc.addPixelStrip(0,10);
   // PixelStrip s7 = fc.addPixelStrip(0,10);
   // PixelStrip s8 = fc.addPixelStrip(0,10);
    //PixelStrip s9 = fc.addPixelStrip(0,10);
    //PixelStrip s10 = fc.addPixelStrip(0,10);
   // PixelStrip s11   = fc.addPixelStrip(0,100);
    s1.setAnimation(new RandomPixels());
    s2.setAnimation(new RandomPixels());
    s3.setAnimation(new RandomPixels());
    s4.setAnimation(new RandomPixels());
   // s2.setAnimation(new Pulsing());
   // s11.setAnimation(redLights);
    //s4.setAnimation(new RandomPixels());
    //s3.setAnimation(new Spark());
   // s5.setAnimation(new RandomPixels());
  //  s6.setAnimation(new RandomPixels());
   // s7.setAnimation(new Pulsing());
   // s8.setAnimation(new RandomPixels());
   // s9.setAnimation(new Pulsing());
   // s10.setAnimation(new RandomPixels());
	// write your code here
         System.out.println(server.getConfig());
         int i = 0;
         int red = 0xdd0000;
         int green = 0x00dd00;
         int current = red;
         for(;;)
            {
            /**
            i+= 1;
            if(i % 1000 == 0)
                {
                    if(current == red)
                        {
                        System.err.println("RED");
                        s11.setAnimation(redLights);
                        current = green;
                        }
                    else
                        {
                        System.err.println("GREEN");
                        s11.setAnimation(greenLights);
                        current = red;
                        }
                }
             **/
            server.animate();
            Thread.sleep(50);
            }

    }
}
