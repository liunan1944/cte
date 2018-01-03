package optest.convolution;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;

public class Convolutiaon01 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\lenanoise.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		//均值模糊
//		Mat dst = new Mat();
//		Imgproc.blur(src, dst, new Size(15,15));
//		ImageUI output = new ImageUI();
//		output.imShow("output image",dst);
		//中值模糊可以去除噪点
		Mat dstMiddule = new Mat();
		Imgproc.medianBlur(src, dstMiddule, 5);
		ImageUI outputMiddule = new ImageUI();
		outputMiddule.imShow("outputMiddule image",dstMiddule);
		//自定义模糊，锐化图片
		Mat result = new Mat();
		Mat k = new Mat(3,3,CvType.CV_32FC1);
		float[] data = new float[]{0,-1,0,-1,5,-1,0,-1,0};
		k.put(0, 0, data);
		Imgproc.filter2D(dstMiddule, result, CvType.CV_8U, k);
		ImageUI result_all = new ImageUI();
		result_all.imShow("result_all image",result);
	}
}
