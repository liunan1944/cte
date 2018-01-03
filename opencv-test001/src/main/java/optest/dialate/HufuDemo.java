package optest.dialate;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 膨胀和腐蚀
 * */
public class HufuDemo {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//1、加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\example.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		Mat dst = new Mat();
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		
		//2、腐蚀
		Imgproc.erode(src, dst, kernel,new Point(0,0),5);
		Mat dst2 = new Mat();
		//3、膨胀,todo 锐化处理
		Imgproc.dilate(dst, dst2, kernel,new Point(0,0),5);
		Mat dst3 = new Mat();
		//4、高斯模糊
		Imgproc.bilateralFilter(dst2, dst3, 0, 100, 15);
		ImageUI gaussan = new ImageUI();
		gaussan.imShow("霍夫 image",dst3);
	}

}
