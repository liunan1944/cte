package optest;

import org.opencv.core.Core;
import org.opencv.core.Rect;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class MainLearn01 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\lena.png", 
				Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		Rect rect = new Rect(10,10,200,200);
		Imgproc.rectangle(src, rect.tl(), rect.br(), 
				new Scalar(0,0,255),2,Imgproc.LINE_8,0);
		Imgcodecs.imwrite("D:\\credit\\opencv-test\\test-01\\lena_result.png",
				src);
	}
}
