package src;

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MeanFilterParallel extends RecursiveAction{
  public static int window;
  private static int k;
  private static  int h;
  private static int w;
  private static int sx;
  private static int sy;
  public static BufferedImage i1;
  static final int SEQUENTIAL_CUTOFF= 900000;
  public MeanFilterParallel (int stX, int StY ,int height,int width,int key, BufferedImage image) {
    k = key;
    h = height;
    w = width;
    i1 = image;
    sx = stX;
    sy = StY;
  }
    //  System.out.println(" h "+h+" w "+w);
//        System.out.println("starty "+startY+" startx "+startX);
//        System.out.println("image.getHeight()-starty "+(image.getHeight()-startY)+" image.getWidth()-startx "+(image.getWidth()-startX));

  protected void compute() {
     //System.out.println("Compute method => img-hi "+(img.getWidth()-hi)+" squential_cutoff "+SEQUENTIAL_CUTOFF);
     if ((w*h) < SEQUENTIAL_CUTOFF) {
      int window =(k-1)/2;
      for(int y = (sy+window); y < (i1.getHeight()-window); y++) {  
        for(int x = (sx+window); x < (i1.getWidth()-window); x++) {
            if (x >= i1.getWidth()|| y >= i1.getHeight()) {
                continue;
            } 
            else {
          int redT = 0;
          int greenT =0;
          int blueT =0;
          int Newp =0;

          for(int j= (y-window); j<= (y+window); j++) {
            for(int i = (x-window); i <= (x+window); i++) {
              int p = i1.getRGB(i,j);
              //alphaT = alphaT+((p>>24)& 0xff);
              redT = redT+((p>>16)& 0xff);
              greenT = greenT+((p>>8)& 0xff);
              blueT = blueT+(p & 0xff);
            }
            //System.out.print(" i "+i+" ");
            //System.out.println(" i "+i+" j "+j);
          }
           //System.out.println();
          Newp = (redT/(k*k)<<16) | (greenT/(k*k)<<8) | blueT/(k*k);
          i1.setRGB(x, y, Newp);
        }
      }
      }
    }
    else {
      MeanFilterParallel box1 = new MeanFilterParallel(sx, sy, h/2, w/2, k ,i1);
      MeanFilterParallel box2 = new MeanFilterParallel((sx + w/2), sy, h/2, w/2, k, i1);
      MeanFilterParallel box3 = new MeanFilterParallel(sx, (sy + h/2), h/2, w/2, k, i1);
      MeanFilterParallel box4 = new MeanFilterParallel((sx + w/2), (sy + h/2), h/2, w/2, k ,i1);
      invokeAll(box1,box2,box3,box4);
    }
  }
  public static void main(String[] args) {
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
      pool.invoke(new MeanFilterParallel(0, 0, h, w, k, i1));
      long endTime = System.currentTimeMillis();
      System.out.println("Image blur parallel took " + (endTime - startTime) +" milliseconds.");

      try {
        f = new File("./output_mean_parallel.jpg");
          ImageIO.write(i1, "jpg", f);
      }
      catch(IOException e) {
        System.out.println(e);
        System.out.println("Try again............");
      }
    }
  } 