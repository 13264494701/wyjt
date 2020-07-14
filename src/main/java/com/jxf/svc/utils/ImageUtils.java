package com.jxf.svc.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.springframework.util.Assert;

import com.jxf.svc.config.Setting;


/**
 * Utils - 图片处理(支持JDK、GraphicsMagick、ImageMagick)
 * 
 * @author JINXINFU
 * @version 2.0
 */
public final class ImageUtils {

	/**
	 * 方式
	 */
	private enum Method {

		/** 自动 */
		auto,

		/** Jdk */
		jdk,

		/**
		 * GraphicsMagick
		 */
		graphicsMagick,

		/**
		 * ImageMagick
		 */
		imageMagick
	}

	/** 方式 */
	private static ImageUtils.Method method = ImageUtils.Method.auto;

	/** GraphicsMagick程序路径 */
	private static String graphicsMagickPath;

	/** ImageMagick程序路径 */
	private static String imageMagickPath;

	/** 背景颜色 */
	private static final Color BACKGROUND_COLOR = Color.white;

	/** 目标图片品质(取值范围: 0 - 100) */
	private static final int DEST_QUALITY = 88;

	static {
		if (SystemUtils.IS_OS_WINDOWS) {
			String pathVariable = System.getenv("Path");
//			System.out.print(pathVariable);
			if (StringUtils.isNotEmpty(pathVariable)) {
				String[] paths = StringUtils.split(pathVariable, ";");
				if (graphicsMagickPath == null) {
					for (String path : paths) {
						File gmFile = new File(path.trim() + "/gm.exe");
						File gmdisplayFile = new File(path.trim() + "/gmdisplay.exe");
						if (gmFile.exists() && gmdisplayFile.exists()) {
							graphicsMagickPath = path.trim();
							break;
						}
					}
				}
				if (imageMagickPath == null) {
					for (String path : paths) {
						File convertFile = new File(path.trim() + "/convert.exe");
						File compositeFile = new File(path.trim() + "/composite.exe");
						if (convertFile.exists() && compositeFile.exists()) {
							imageMagickPath = path.trim();
							break;
						}
					}
				}
			}
		}

		if (ImageUtils.Method.auto.equals(method)) {
			try {
				IMOperation operation = new IMOperation();
				operation.version();
				IdentifyCmd identifyCmd = new IdentifyCmd(true);
				if (graphicsMagickPath != null) {
					identifyCmd.setSearchPath(graphicsMagickPath);
				}
				identifyCmd.run(operation);
				method = ImageUtils.Method.graphicsMagick;
			} catch (Throwable e1) {
				try {
					IMOperation operation = new IMOperation();
					operation.version();
					IdentifyCmd identifyCmd = new IdentifyCmd(false);
					identifyCmd.run(operation);
					if (imageMagickPath != null) {
						identifyCmd.setSearchPath(imageMagickPath);
					}
					method = ImageUtils.Method.imageMagick;
				} catch (Throwable e2) {
					method = ImageUtils.Method.jdk;
				}
			}
		}
	}

	/**
	 * 不可实例化
	 */
	private ImageUtils() {
	}

	/** 
	 *  
	 * 根据坐标裁剪图片 
	 *  
	 * @param srcPath   要裁剪图片的路径 
	 * @param destPath   裁剪图片后的路径 
	 * width：  裁剪的宽度 
	 * height： 裁剪的高度 
	 * x：       裁剪的横坐标 
	 * y：       裁剪的挫坐标 
	 */  

	public static void cutImage(String srcPath, String destPath, int x, int y,int width,int height) throws Exception {  

	    IMOperation op = new IMOperation();  
	    op.addImage(srcPath);  
	    op.crop(width, height, x, y);  
	    op.addImage(destPath);  
	    ConvertCmd convert = new ConvertCmd(true);  
	    convert.run(op);  
	}  
	/**
	 * 等比例图片缩放
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param rate
	 *            伸缩比例
	 * @throws IOException 
	 */
	public static void zoom(File srcFile, File destFile,float scale) throws IOException {

		BufferedImage srcBufferedImage = ImageIO.read(srcFile);
		int srcWidth = srcBufferedImage.getWidth();
		int srcHeight = srcBufferedImage.getHeight();
		int destWidth = (int) (srcWidth * scale);
		int destHeight = (int) (srcHeight * scale);
		
		
		if (ImageUtils.Method.jdk.equals(method)) {
			Graphics2D graphics2D = null;
			ImageOutputStream imageOutputStream = null;
			ImageWriter imageWriter = null;
			try {				

				BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
				graphics2D = destBufferedImage.createGraphics();
				graphics2D.setBackground(BACKGROUND_COLOR);
				graphics2D.clearRect(0, 0, destWidth, destHeight);
				graphics2D.drawImage(srcBufferedImage.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH), 0, 0, null);

				imageOutputStream = ImageIO.createImageOutputStream(destFile);
				imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
				imageWriter.setOutput(imageOutputStream);
				ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality((float) (DEST_QUALITY / 100.0));
				imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
				imageOutputStream.flush();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				if (graphics2D != null) {
					graphics2D.dispose();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
				try {
					if (imageOutputStream != null) {
						imageOutputStream.close();
					}
				} catch (IOException e) {
				}
			}
		} else {
			IMOperation operation = new IMOperation();
			operation.thumbnail(destWidth, destHeight);
			operation.gravity("center");
			operation.background(toHexEncoding(BACKGROUND_COLOR));
			operation.extent(destWidth, destHeight);
			operation.quality((double) DEST_QUALITY);
			try {
				operation.addImage(srcFile.getCanonicalPath());
				operation.addImage(destFile.getCanonicalPath());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			if (ImageUtils.Method.graphicsMagick.equals(method)) {
				ConvertCmd convertCmd = new ConvertCmd(true);
				if (graphicsMagickPath != null) {
					convertCmd.setSearchPath(graphicsMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			} else {
				ConvertCmd convertCmd = new ConvertCmd(false);
				if (imageMagickPath != null) {
					convertCmd.setSearchPath(imageMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}
	/**
	 * 等比例图片缩放
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param destWidth
	 *            目标宽度
	 * @param destHeight
	 *            目标高度
	 */
	public static void zoom(File srcFile, File destFile, int destWidth, int destHeight) {
		Assert.notNull(srcFile);
		Assert.state(srcFile.exists());
		Assert.state(srcFile.isFile());
		Assert.notNull(destFile);
		Assert.state(destWidth > 0);
		Assert.state(destHeight > 0);

		if (ImageUtils.Method.jdk.equals(method)) {
			Graphics2D graphics2D = null;
			ImageOutputStream imageOutputStream = null;
			ImageWriter imageWriter = null;
			try {
				BufferedImage srcBufferedImage = ImageIO.read(srcFile);
				int srcWidth = srcBufferedImage.getWidth();
				int srcHeight = srcBufferedImage.getHeight();
				int width = destWidth;
				int height = destHeight;
				if (srcHeight >= srcWidth) {
					width = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
				} else {
					height = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
				}
				BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
				graphics2D = destBufferedImage.createGraphics();
				graphics2D.setBackground(BACKGROUND_COLOR);
				graphics2D.clearRect(0, 0, destWidth, destHeight);
				graphics2D.drawImage(srcBufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), (destWidth / 2) - (width / 2), (destHeight / 2) - (height / 2), null);

				imageOutputStream = ImageIO.createImageOutputStream(destFile);
				imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
				imageWriter.setOutput(imageOutputStream);
				ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality((float) (DEST_QUALITY / 100.0));
				imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
				imageOutputStream.flush();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				if (graphics2D != null) {
					graphics2D.dispose();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
				try {
					if (imageOutputStream != null) {
						imageOutputStream.close();
					}
				} catch (IOException e) {
				}
			}
		} else {
			IMOperation operation = new IMOperation();
			operation.thumbnail(destWidth, destHeight);
			operation.gravity("center");
			operation.background(toHexEncoding(BACKGROUND_COLOR));
			operation.extent(destWidth, destHeight);
			operation.quality((double) DEST_QUALITY);
			try {
				operation.addImage(srcFile.getCanonicalPath());
				operation.addImage(destFile.getCanonicalPath());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			if (ImageUtils.Method.graphicsMagick.equals(method)) {
				ConvertCmd convertCmd = new ConvertCmd(true);
				if (graphicsMagickPath != null) {
					convertCmd.setSearchPath(graphicsMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			} else {
				ConvertCmd convertCmd = new ConvertCmd(false);
				if (imageMagickPath != null) {
					convertCmd.setSearchPath(imageMagickPath);
				}
				try {
					convertCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}
	/**
	 * 保持图片尺寸不变压缩图片
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param quality
	 *            压缩比例
	 */
	public static final void compression(File srcFile, File destFile, float quality){
		Assert.notNull(srcFile);
		Assert.state(srcFile.exists());
		Assert.state(srcFile.isFile());
		Assert.notNull(destFile);
		Assert.state(0<quality);
		Assert.state(quality<=1);

		ImageOutputStream imageOutputStream = null;
		ImageWriter imageWriter = null;
		try {

	        //开始处理图片，避免图片压缩之后出现变色情况
	        int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
	        ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
	        java.awt.Image OrgImg = Toolkit.getDefaultToolkit().createImage(srcFile.getAbsolutePath());
	        PixelGrabber pg = new PixelGrabber(OrgImg,0,0,-1,-1,true);	
			pg.grabPixels();	
	        int width = pg.getWidth();
	        int height = pg.getHeight();
	        DataBuffer buffer = new DataBufferInt((int[])pg.getPixels(),pg.getWidth()*pg.getHeight());
	        WritableRaster raster = Raster.createPackedRaster(buffer,width,height,width,RGB_MASKS,null);
	        BufferedImage image = new BufferedImage(RGB_OPAQUE,raster,false,null);
	        //处理结束
		
			imageOutputStream = ImageIO.createImageOutputStream(destFile);
			imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
			imageWriter.setOutput(imageOutputStream);
			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
			imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			imageWriteParam.setCompressionQuality((float)quality);
			imageWriter.write(null, new IIOImage(image, null, null), imageWriteParam);
			imageOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		finally {
			if (imageWriter != null) {
				imageWriter.dispose();
			}
			try {
				if (imageOutputStream != null) {
					imageOutputStream.close();
				}
			} catch (IOException e) {
			}
		}		
	}
	public static final void compression(File file, float quality){
		File tempFile = new File(file.getParent(), UUID.randomUUID() + ".jpg"); 	
		ImageUtils.compression(file, tempFile, quality);
		file.delete();
		tempFile.renameTo(file);	
	}
	/**
	 * 添加水印
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param watermarkFile
	 *            水印文件
	 * @param watermarkPosition
	 *            水印位置
	 * @param alpha
	 *            水印透明度
	 */
	public static void addWatermark(File srcFile, File destFile, File watermarkFile, Setting.WatermarkPosition watermarkPosition, int alpha) {
		Assert.notNull(srcFile);
		Assert.state(srcFile.exists());
		Assert.state(srcFile.isFile());
		Assert.notNull(destFile);
		Assert.state(alpha >= 0);
		Assert.state(alpha <= 100);

		if (watermarkFile == null || !watermarkFile.exists() || !watermarkFile.isFile() || watermarkPosition == null || Setting.WatermarkPosition.no.equals(watermarkPosition)) {
			try {
				if (!StringUtils.equals(srcFile.getCanonicalPath(), destFile.getCanonicalPath())) {
					FileUtils.copyFile(srcFile, destFile);
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return;
		}
		if (ImageUtils.Method.jdk.equals(method)) {
			Graphics2D graphics2D = null;
			ImageOutputStream imageOutputStream = null;
			ImageWriter imageWriter = null;
			try {
				BufferedImage srcBufferedImage = ImageIO.read(srcFile);
				int srcWidth = srcBufferedImage.getWidth();
				int srcHeight = srcBufferedImage.getHeight();
				BufferedImage destBufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
				graphics2D = destBufferedImage.createGraphics();
				graphics2D.setBackground(BACKGROUND_COLOR);
				graphics2D.clearRect(0, 0, srcWidth, srcHeight);
				graphics2D.drawImage(srcBufferedImage, 0, 0, null);
				graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100F));

				BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
				int watermarkImageWidth = watermarkBufferedImage.getWidth();
				int watermarkImageHeight = watermarkBufferedImage.getHeight();
				int x;
				int y;
				switch (watermarkPosition) {
				case topLeft:
					x = 0;
					y = 0;
					break;
				case topRight:
					x = srcWidth - watermarkImageWidth;
					y = 0;
					break;
				case center:
					x = (srcWidth - watermarkImageWidth) / 2;
					y = (srcHeight - watermarkImageHeight) / 2;
					break;
				case bottomLeft:
					x = 0;
					y = srcHeight - watermarkImageHeight;
					break;
				case bottomRight:
					x = srcWidth - watermarkImageWidth;
					y = srcHeight - watermarkImageHeight;
					break;
				default:
					x = srcWidth - watermarkImageWidth;
					y = srcHeight - watermarkImageHeight;
					break;
				}
				graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);

				imageOutputStream = ImageIO.createImageOutputStream(destFile);
				imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
				imageWriter.setOutput(imageOutputStream);
				ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
				imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriteParam.setCompressionQuality(DEST_QUALITY / 100F);
				imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
				imageOutputStream.flush();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				if (graphics2D != null) {
					graphics2D.dispose();
				}
				if (imageWriter != null) {
					imageWriter.dispose();
				}
				try {
					if (imageOutputStream != null) {
						imageOutputStream.close();
					}
				} catch (IOException e) {
				}
			}
		} else {
			String gravity;
			switch (watermarkPosition) {
			case topLeft:
				gravity = "NorthWest";
				break;
			case topRight:
				gravity = "NorthEast";
				break;
			case center:
				gravity = "Center";
				break;
			case bottomLeft:
				gravity = "SouthWest";
				break;
			case bottomRight:
				gravity = "SouthEast";
				break;
			default:
				gravity = "SouthEast";
				break;
			}
			IMOperation operation = new IMOperation();
			operation.gravity(gravity);
			operation.dissolve(alpha);
			operation.quality((double) DEST_QUALITY);
			try {
				operation.addImage(watermarkFile.getCanonicalPath());
				operation.addImage(srcFile.getCanonicalPath());
				operation.addImage(destFile.getCanonicalPath());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			if (ImageUtils.Method.graphicsMagick.equals(method)) {
				CompositeCmd compositeCmd = new CompositeCmd(true);
				if (graphicsMagickPath != null) {
					compositeCmd.setSearchPath(graphicsMagickPath);
				}
				try {
					compositeCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			} else {
				CompositeCmd compositeCmd = new CompositeCmd(false);
				if (imageMagickPath != null) {
					compositeCmd.setSearchPath(imageMagickPath);
				}
				try {
					compositeCmd.run(operation);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IM4JavaException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 转换颜色为十六进制代码
	 * 
	 * @param color
	 *            颜色
	 * @return 十六进制代码
	 */
	private static String toHexEncoding(Color color) {
		Assert.notNull(color);

		String r, g, b;
		StringBuilder result = new StringBuilder();
		r = Integer.toHexString(color.getRed());
		g = Integer.toHexString(color.getGreen());
		b = Integer.toHexString(color.getBlue());
		r = r.length() == 1 ? "0" + r : r;
		g = g.length() == 1 ? "0" + g : g;
		b = b.length() == 1 ? "0" + b : b;
		result.append("#").append(r).append(g).append(b);
		return result.toString();
	}
	
	 public static void main(String[] args) {
		 File file = new File("G:/src.jpg"); 
		 
		 ImageUtils.compression(file, 0.2F);
		 
	 }

}