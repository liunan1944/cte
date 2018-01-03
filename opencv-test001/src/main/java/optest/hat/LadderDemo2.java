package optest.hat;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 形态学梯度
 * 内部梯度
 * 外部梯度
 * */
public class LadderDemo2 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//1、加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\00000001.jpg");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		//3、膨胀
		Mat dst = new Mat();
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		
		//外部梯度,膨胀后与原图差值
//		Mat exter = new Mat();
//		Imgproc.dilate(src, dst, kernel);
//		Core.subtract(dst, src, exter);
//		ImageUI exterShow = new ImageUI();
//		exterShow.imShow("外部梯度:",exter);
		
		//内部梯度,腐蚀后与原图差值
		Mat internal = new Mat();
		Imgproc.erode(src, dst, kernel);
		Core.subtract(src, dst, internal);
		ImageUI internalShow = new ImageUI();
		internalShow.imShow("内部梯度:",internal);
	}

}
