package optest.gaussian;

import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 高斯双边模糊
 * */
public class GaussianDemo2 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\example.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		Mat dst = new Mat();
		Imgproc.bilateralFilter(src, dst, 0, 100, 15);
		ImageUI gaussan = new ImageUI();
		gaussan.imShow("bilater image",dst);
	}
}
