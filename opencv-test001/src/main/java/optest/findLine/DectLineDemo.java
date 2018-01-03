package optest.findLine;

import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 直线提取
 * */
public class DectLineDemo {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//1、加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\00000001.jpg");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		removeLine(src);
	}
	/**
	 * 直线提取方法
	 * */
	public static void detectLine(Mat src){
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		
		Mat binary = new Mat();
		Imgproc.threshold(gray, binary, 0, 255, 
				Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
		ImageUI binaryShow = new ImageUI();
		binaryShow.imShow("二值化",binary);
		
		Mat open = new Mat();
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, 
				new Size(40,1));
		
		Imgproc.morphologyEx(binary, open, Imgproc.MORPH_OPEN, 
				kernel);
		
		ImageUI close = new ImageUI();
		close.imShow("直线提取",open);
	}
	/**
	 * 去除干扰线
	 * */
	public static void removeLine(Mat src){
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		
		Mat binary = new Mat();
		Imgproc.threshold(gray, binary, 0, 255, 
				Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
		ImageUI binaryShow = new ImageUI();
		binaryShow.imShow("二值化",binary);
		
		Mat open = new Mat();
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, 
				new Size(5,5));
		
		Imgproc.morphologyEx(binary, open, Imgproc.MORPH_OPEN, 
				kernel);
		
		ImageUI close = new ImageUI();
		close.imShow("去除干扰线",open);
		
		Mat subSrc = new Mat();
		Core.subtract(binary, open, subSrc);
//		ImageUI internalShow = new ImageUI();
//		internalShow.imShow("差值得到图像:",subSrc);
		
		Mat subSrcToBGR = new Mat();
		Imgproc.cvtColor(subSrc, subSrcToBGR, Imgproc.COLOR_GRAY2BGR);
//		ImageUI subShow = new ImageUI();
//		subShow.imShow("差值2得到图像:",subSrcToBGR);
		
		findLinePexel(subSrc,src,subSrcToBGR);
//		Mat subSrc2 = new Mat();
//		Core.subtract(src, subSrc1, subSrc2);
//		ImageUI subShow2 = new ImageUI();
//		subShow2.imShow("差值3得到图像:",subSrc2);
	}
	/**
	 * 获取二值化曲线像素点位置
	 * 将subSrcToBGR填充为获取到的像素
	 * */
	public static void findLinePexel(Mat gray,Mat src,Mat subSrcToBGR){
		int height = gray.rows();
		int width = gray.cols();
		int dims = gray.channels();
		byte[] data = new byte[width*height*dims];
		gray.get(0, 0, data);
		
		int r=0,g=0,b=0;
		int src_row=0,src_height=0;
		boolean findpexel = false;
		for(int row=0;row<height;row++){
			for(int col=0;col<width;col++){
				b = data[row*width*dims+col*dims]&0xff;
				g = data[row*width*dims+col*dims+1]&0xff;
				r = data[row*width*dims+col*dims+2]&0xff;
//				System.out.println("第"+row+"行第"+col+"列 "+"获取b:"+b+";获取g:"+g+"获取r:"+r);
				if(b == 0 && g == 0 && r == 0){
					
				}else{
					src_row = row;
					src_height = col;
					findpexel = true;
					break;
				}
			}
			if(findpexel)
				break;
		}
		
		int height_src = src.rows();
		int width_src = src.cols();
		int dims_src = src.channels();
		byte[] data_src = new byte[width_src*height_src*dims_src];
		src.get(0, 0, data_src);
		
		int r_src=0,g_src=0,b_src=0;
		boolean src_findpexel = false;
		for(int row=0;row<height_src;row++){
			for(int col=0;col<width_src;col++){
				if(row==src_row && col==src_height){
					b_src = data_src[row*width*dims_src+col*dims_src]&0xff;
					g_src = data_src[row*width*dims_src+col*dims_src+1]&0xff;
					r_src = data_src[row*width*dims_src+col*dims_src+2]&0xff;
					src_findpexel = true;
					break;
				}
			}
			if(src_findpexel)
				break;
		}
		System.out.println("获取b_src:"+b_src+
				";获取g_src"+g_src+";获取r_src"+r_src);
		fillGrayToBGR(b_src,g_src,r_src,subSrcToBGR,src);
	}
	public static void fillGrayToBGR(int src_b,int src_g,
			int src_r,Mat subSrcToBGR,Mat src){
		int height = subSrcToBGR.rows();
		int width = subSrcToBGR.cols();
		int dims = subSrcToBGR.channels();
		byte[] data = new byte[width*height*dims];
		subSrcToBGR.get(0, 0, data);
		
		int r=0,g=0,b=0;

		for(int row=0;row<height;row++){
			for(int col=0;col<width;col++){
				b = data[row*width*dims+col*dims]&0xff;
				g = data[row*width*dims+col*dims+1]&0xff;
				r = data[row*width*dims+col*dims+2]&0xff;
				System.out.println("第"+row+"行第"+col+"列 "+
				"获取b:"+b+";获取g:"+g+"获取r:"+r);
				if(b == 0 && g == 0 && r == 0){
//					data[row*width*dims+col*dims] = (byte)255;
//					data[row*width*dims+col*dims+1] = (byte)255;
//					data[row*width*dims+col*dims+2] = (byte)255;	
				}else{
					data[row*width*dims+col*dims] = (byte)src_b;
					data[row*width*dims+col*dims+1] = (byte)src_g;
					data[row*width*dims+col*dims+2] = (byte)src_r;	
				}
				
			}
		}
		subSrcToBGR.put(0, 0, data);
		ImageUI subShow = new ImageUI();
		subShow.imShow("填充后得到图像:",subSrcToBGR);
		
		Mat subSrc2 = new Mat();
		Core.subtract(subSrcToBGR, src, subSrc2);
		ImageUI subShow2 = new ImageUI();
		subShow2.imShow("差值3得到图像:",subSrc2);
	}
}
