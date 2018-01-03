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
public class DectLineDemo3 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//1、加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\1sdsdk.jpg");
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
//		ImageUI binaryShow = new ImageUI();
//		binaryShow.imShow("二值化",binary);
		
		Mat open = new Mat();
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, 
				new Size(3,3));
		
		Imgproc.morphologyEx(binary, open, Imgproc.MORPH_OPEN, 
				kernel);
		
		ImageUI close = new ImageUI();
		close.imShow("去除干扰线",open);
		
		Mat subSrc = new Mat();
		Core.subtract(binary, open, subSrc);
		ImageUI internalShow = new ImageUI();
		internalShow.imShow("差值得到图像:",subSrc);
		
		findLinePexel(subSrc,src);
		
//		Mat subSrc2 = new Mat();
//		Core.subtract(src, subSrc1, subSrc2);
//		ImageUI subShow2 = new ImageUI();
//		subShow2.imShow("差值3得到图像:",subSrc2);
	}
	/**
	 * 获取二值化曲线像素点位置
	 * 将subSrcToBGR填充为获取到的像素
	 * */
	public static void findLinePexel(Mat gray,Mat src){
		int height = gray.rows();
		int width = gray.cols();
		int dims = gray.channels();
		System.out.println("二值化图像通道:"+gray.channels());
		byte[] data = new byte[width*height*dims];
		gray.get(0, 0, data);
		
		int b=0;
		int src_row=0,src_height=0;

		for(int row=0;row<height;row++){
			for(int col=0;col<width;col++){
				b = data[row*width*dims+col*dims]&0xff;
//				System.out.println("第"+row+"行第"+col+"列 "+"获取b:"+b);
				if(b == 255){
					src_row = row;
					src_height = col;
					setMatWight(src_row,src_height,src);
				}
			}
		}
		ImageUI subShow = new ImageUI();
		subShow.imShow("最终输出图像:",src);
	}
	//设置指定像素点像素值
	public static void setMatWight(int src_row,int src_height,Mat src){
		int height = src.rows();
		int width = src.cols();
		int dims = src.channels();
		byte[] data = new byte[width*height*dims];
		src.get(0, 0, data);
		
		int b=0,g=0,r=0;
		boolean findpexel = false;
		for(int row=0;row<height;row++){
			for(int col=0;col<width;col++){
				b = data[row*width*dims+col*dims]&0xff;
				g = data[row*width*dims+col*dims+1]&0xff;
				r = data[row*width*dims+col*dims+2]&0xff;
//				System.out.println("第"+row+"行第"+col+"列 "+"获取b:"+b);
				if(src_row==row && src_height==col){
					data[row*width*dims+col*dims] = (byte)255;
					data[row*width*dims+col*dims+1] = (byte)255;
					data[row*width*dims+col*dims+2] = (byte)255;
					findpexel = true;
					break;
				}
			}
			if(findpexel)
				break;
		}
		src.put(0, 0, data);
	}
}
