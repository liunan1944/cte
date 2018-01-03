package optest.openAndClose;

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
	}
}
