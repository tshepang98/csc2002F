package src;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class MedianFilterParallel extends RecursiveAction{
    public static int window;
    private static int k;
    private static  int h;
    private static int w;
    private static int sx;
    private static int sy;
    public static BufferedImage i1;
    static final int SEQUENTIAL_CUTOFF= 900000;
    // Processing window size; should be odd.
    public MedianFilterParallel(int stX, int StY ,int height,int width,int key, BufferedImage image) {
      k = key;
      h = height;
      w = width;
      i1 = image;
      sx = stX;
      sy = StY;
  }
   protected static void medianFilter(){
        int window =(k-1)/2;
        for(int y = (sy+window); y < (i1.getHeight()-window); y++) {  
          for(int x = (sx+window); x < (i1.getWidth()-window); x++) {
              if (x >= i1.getWidth()|| y >= i1.getHeight()) {
                  continue;
              }
               else {
                  int[] redArr =new int[k*k];
                  int[] greenArr= new int[k*k];
                  int[] blueArr =new int[k*k];
                  int Newp;// = i1.getRGB(x, y);
                  int index = 0;
                  for(int j= (y-window); j<= (y+window); j++) {
                    for(int i = (x-window); i <= (x+window); i++) {
                        int p = i1.getRGB(i,j);
                        redArr[index] = ((p>>16)& 0xff);
                        greenArr[index] = ((p>>8)& 0xff);
                        blueArr[index] = (p & 0xff);
                       // System.out.print(" i "+i+" ");
                        index++;
                    }
              }
                //new Pixel
                Arrays.sort(redArr);
                Arrays.sort(greenArr);
                Arrays.sort(blueArr);
                int middle= (k*k)/2;
                ///System.out.println("k => "+k);
                Newp = (redArr[middle]<<16)| (greenArr[middle]<<8)|blueArr[middle];
                i1.setRGB(x, y, Newp);
            }
           
        }
    }
}
protected void compute() {
   if ((h*w) < SEQUENTIAL_CUTOFF) {
       medianFilter();
   }
   else {

       MedianFilterParallel box1 = new MedianFilterParallel(sx, sy, h/2, w/2, k ,i1);
       MedianFilterParallel box2 = new MedianFilterParallel((sx + w/2), sy, h/2, w/2, k, i1);
       MedianFilterParallel box3 = new MedianFilterParallel(sx, (sy + h/2), h/2, w/2, k, i1);
       MedianFilterParallel box4 = new MedianFilterParallel((sx + w/2), (sy + h/2), h/2, w/2, k ,i1);
       invokeAll(box1,box2,box3,box4);
       
   }
}
public static void main(String[] args) {
   BufferedImage i1 = null;
   File f = null;
   System.out.println("Enter input file name :");
   Scanner sc = new Scanner(System.in);
   String inputImage = sc.nextLine();
   System.out.println("Enter output file name :");
   String outputImage = sc.nextLine();
   System.out.println("Enter windowWidth");
   int k = sc.nextInt();

   try{
     f = new File("./image2.jpg");
     i1 = ImageIO.read(f);
   }
   catch(IOException e){
     System.out.println("Try again............");  
   }
   
   int w = i1.getWidth();
   int h = i1.getHeight();

   ForkJoinPool pool = new ForkJoinPool();
   long startTime = System.currentTimeMillis();
   pool.invoke(new MedianFilterParallel(0, 0, h, w, k, i1));
   long endTime = System.currentTimeMillis();
   System.out.println("Image blur parallel took " + (endTime - startTime) + " milliseconds.");

   try {
     f = new File("./output.jpg");
     ImageIO.write(i1, "jpg", f);
   }
   catch(IOException e) {
      System.out.println("Try again............");
   }
}
}