package optest.mat;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;

public class MatLearn4 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\lena.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		Mat dst = new Mat();
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2HSV);
		
		ImageUI output = new ImageUI();
		output.imShow("output image",dst);
	}
}
