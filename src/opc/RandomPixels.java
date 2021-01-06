package opc;

public class RandomPixels extends Animation {

    java.util.Random rand;

    public void reset(PixelStrip strip) {
        rand = new java.util.Random();
    }

    public boolean draw(PixelStrip strip) {
        int randomPixel = rand.nextInt(strip.getPixelCount());
        int randomColor = makeColor(rand.nextInt(0xaa),
                rand.nextInt(0xaa), rand.nextInt(0x0808));

        strip.setPixelColor(randomPixel, randomColor);
        return true;
    }
}

