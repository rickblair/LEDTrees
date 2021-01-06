package com.fatech;

import opc.*;

public class RedGreenFade
    {
    static long timeCycle = 15000;
    
    static private int pulseOverTime(long timeNow)
    {
        double theta = 6.283 * timeNow / timeCycle;   // Angle in radians
        double s = (Math.sin(theta) + 1.0) / 2.0;     // Value from 0.0 to 1.0
        return (int) Math.round(s * 0xdd);
    }
    
    static protected long millis()
    {
        return System.currentTimeMillis() - programStartTime;
    }
    
    static private long programStartTime = System.currentTimeMillis();
    
    public static void main(String[] args)
    throws InterruptedException
    {
        Pulsing pulsing = new Pulsing();
       // String host = "smalltree.local";
        String host = "xmaslights.local";
        int port = 7890;
        int treeCount = 100;
        OpcClient server = new OpcClient(host, port);
        OpcDevice fc = server.addDevice();
        PixelStrip s1 = fc.addPixelStrip(0, treeCount);
        PixelStrip s2 = fc.addPixelStrip(0, treeCount);
        long programStartTime = System.currentTimeMillis();
        // write your code here
        System.out.println(server.getConfig());
        int i = 0;
        int red = 0xdd0000;
        int green = 0x00dd00;
        PixelStrip redStrip = s1;
        PixelStrip greenStrip = s2;
        boolean reverse = false;
        int previous = 0;
        
        for (; ; )
            {
                long currentTime = millis() % timeCycle;
                int brightness = pulseOverTime((currentTime) % timeCycle);
                if ((brightness == 0) && (brightness != previous))
                    {
                        reverse = true;
                    }
                else
                    {
                        reverse = false;
                    }
                previous = brightness;
                for (int p = 0; p < treeCount; p++)
                    {
                        int c2 = pulsing.fadeColor(red, brightness);
                        int c3 = pulsing.fadeColor(green, brightness);
                        greenStrip.setPixelColor(p, c3);
                        redStrip.setPixelColor(p, c2);
                        // reverse = c2 == 0;
                    }
                if (reverse)
                    {
                        //System.err.println("REVERSE");
                        if (greenStrip == s1)
                            {
                                redStrip = s1;
                                greenStrip = s2;
                            }
                        else
                            {
                                redStrip = s2;
                                greenStrip = s1;
                            }
                        reverse = false;
                    }
                server.show();
                Thread.sleep(5);
            }
        
    }
    }
