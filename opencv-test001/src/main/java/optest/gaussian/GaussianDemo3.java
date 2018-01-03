package optest.gaussian;

import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 自适应二值化
 * */
public class GaussianDemo3 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\text.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		
		Mat dst = new Mat();
		Imgproc.adaptiveThreshold(gray, dst, 255, 
				Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, 
				Imgproc.THRESH_BINARY, 15, 10);
		ImageUI gaussan = new ImageUI();
		gaussan.imShow("bilater image",dst);
	}
}
