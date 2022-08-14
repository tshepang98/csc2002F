package src;

import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MedianFilterSerial{
    private static int k;
    private static  int h;
    private static int w;
    public static BufferedImage img;
  
    public MedianFilterSerial(int height,int width,int key, BufferedImage image) {
       k = key;
       h = height;
       w = width;
       img = image;

      //  System.out.println(" h "+h+" w "+w);
//        System.out.println("starty "+startY+" startx "+startX);
//        System.out.println("image.getHeight()-starty "+(image.getHeight()-startY)+" image.getWidth()-startx "+(image.getWidth()-startX));

   
    }
    protected  void medianFilter(){
      int window =(k-1)/2;
      //System.out.println("w "+w+" h "+h);
      //System.out.println("sx "+sx+" sy "+sy);
      for(int y = (window); y < (h-window); y++) {  
        for(int x = (window); x < (w-window); x++) {
                int[] redArr =new int[k*k];
                int[] greenArr= new int[k*k];
                int[] blueArr =new int[k*k];
                int Newp;// = i1.getRGB(x, y);
                int index = 0;
                for(int j= (y-window); j<= (y+window); j++) {
                  for(int i = (x-window); i <= (x+window); i++) {
                      //System.out.println(" i => "+i+" and j => "+j);
                      int p = img.getRGB(i,j);
                      //alphaArr[index] = ((p>>24)& 0xff);
                      redArr[index] = ((p>>16)& 0xff);
                      greenArr[index] = ((p>>8)& 0xff);
                      blueArr[index] = (p & 0xff);
                      //System.out.print(" i "+i+" ");
                      index++;
                      // if (index == ((k*k)-1)) {
                      //     index = 0;
                      // }
                  }
              }
              //new Pixel
              Arrays.sort(redArr);
              Arrays.sort(greenArr);
              Arrays.sort(blueArr);
              int middle= (k*k)/2;
              ///System.out.println("k => "+k);
              Newp = (redArr[middle]<<16)| (greenArr[middle]<<8)|blueArr[middle];
              img.setRGB(x, y, Newp);
         // }
         
      }
  }
}
        public static void main(String[] args) {
            BufferedImage img = null;
            File f = null;
            System.out.println("Enter input file name :");
            Scanner sc = new Scanner(System.in);
            String inputImage = sc.nextLine();
            System.out.println("Enter output file name :");
            String outputImage = sc.nextLine();
            System.out.println("Enter windowWidth");
            int k = sc.nextInt();
        
            try{
              f = new File(".\\image4.jpg");
              img = ImageIO.read(f);
            }
            catch(IOException e){
              System.out.println(e);  
            }
            int w = img.getWidth();
            int h = img.getHeight();

            MedianFilterSerial ms = new MedianFilterSerial(h, w, k, img);
 
            long startTime = System.currentTimeMillis();
            ms.medianFilter();
            long endTime = System.currentTimeMillis();
            System.out.println("Image blur serial took " + (endTime - startTime) + 
                " milliseconds.");
          
            try{
              f = new File(".\\serial_output_median.jpg");
              ImageIO.write(img, "jpg", f);
              }
            catch(IOException e){
               System.out.println(e);
              }
           }
    }