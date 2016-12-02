package com.joindata.inf.common.util.major;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 图像处理实用工具<br />
 * <b>支持的类型：BMP(不支持 BMP32), JPG, PNG, WBMP, GIF(不支持动画)</b>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月10日 下午3:03:03
 */
public class ImageUtil
{
    /**
     * 指定宽高上限，缩放图片，比例不变<br />
     * <b>这里的宽度、高度上限是指，根据原始宽高采取最大的一边，缩放至这两个参数值中最小的一个，保持图片原始比例进行缩放，另一个参数将无效</b><br />
     * <i>听起来<strong>略</strong>拗口，其实用的时候，只要按照自己需求的最大宽度或高度传参即可得到最合适的图片</i>
     * 
     * @param srcFile 原始图片路径
     * @param maxWidth 宽度上限
     * @param maxHeight 高度上限
     * @param destFile 缩放后另存为的文件，如果存在将覆盖
     * @return 缩放后的文件路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String zoomTo(String srcFile, int maxWidth, int maxHeight, String destFile) throws IOException
    {
        Thumbnails.of(srcFile).size(maxWidth, maxHeight).toFile(destFile);
        return destFile;
    }

    /**
     * 强制拉伸至指定宽高<br />
     * <b>图片可能会失真</b>
     * 
     * @param srcFile 原始图片路径
     * @param width 宽度
     * @param height 高度
     * @param destFile 缩放后另存为的文件，如果存在将覆盖
     * @return 拉伸后的文件路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String resizeTo(String srcFile, int width, int height, String destFile) throws IOException
    {
        Thumbnails.of(srcFile).forceSize(width, height).toFile(destFile);
        return destFile;
    }

    /**
     * 按给定系数，等比缩放<br />
     * <b>效果是宽度、高度统统乘以 ratio</b>
     * 
     * @param srcFile 原始图片路径
     * @param ratio 缩放系数
     * @param destFile 缩放后另存为的文件，如果存在将覆盖
     * @return 缩放后的文件路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String zoomByRatio(String srcFile, double ratio, String destFile) throws IOException
    {
        Thumbnails.of(srcFile).scale(ratio).toFile(destFile);
        return destFile;
    }

    /**
     * 旋转图片<br />
     * <b>旋转角度可以是正数也可以是负数，正数表示顺时针转，负数逆时针。如 ±180 就会颠倒过来，如果是 ±90 图片宽高就会互换。</b>
     * 
     * @param srcFile 原始图片路径
     * @param rotate 旋转角度
     * @param destFile 缩放后另存为的文件，如果存在将覆盖
     * @return 旋转后的文件路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String rotate(String srcFile, double rotate, String destFile) throws IOException
    {
        Thumbnails.of(srcFile).scale(1).rotate(rotate).toFile(destFile);
        return destFile;
    }

    /**
     * 裁剪图片<br />
     * <b>指定一部分区域，从原图中剥离出来形成新的图片。</b><br />
     * <b>如果裁剪宽度高度范围超出图片边距，将最多裁到边距，而不会用黑色或其他颜色来填补</b>
     * 
     * @param srcFile 原始图片路径
     * @param x 水平起点
     * @param y 垂直起点
     * @param width 裁切宽度
     * @param heigth 裁切高度
     * @param destFile 缩放后另存为的文件，如果存在将覆盖
     * @return 旋转后的文件路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String cut(String srcFile, int x, int y, int width, int height, String destFile) throws IOException
    {
        Thumbnails.of(srcFile).scale(1).sourceRegion(x, y, width, height).toFile(destFile);
        return destFile;
    }

    /**
     * 转换格式
     * 
     * @param srcFile 原始图片路径
     * @param targetFormat 目标图片类型
     * @param destFile 缩放后另存为的文件，如果存在将覆盖
     * @return 旋转后的文件路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String convertFormat(String srcFile, String targetFormat, String destFile) throws IOException
    {
        Thumbnails.of(srcFile).scale(1).outputFormat(targetFormat).toFile(destFile);
        return destFile;
    }

    /**
     * 压缩图片（降低质量）
     * 
     * @param srcFile 原始图片路径
     * @param destFile 缩放后另存为的文件，如果存在将覆盖
     * @param quality 图片质量，0.0~1.0之间，1.0最高
     * @return 旋转后的文件路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String compress(String srcFile, float quality, String destFile) throws IOException
    {
        Thumbnails.of(srcFile).scale(1).outputQuality(quality).toFile(destFile);
        return destFile;
    }

    /**
     * 加水印
     * 
     * @param srcFile 原始图片路径
     * @param watermarkFile 水印图片路径
     * @param position 位置，请参阅 net.coobird.thumbnailator.geometry.Positions
     * @param opacity 不透明度，0.0~1.0之间，1.0不透明
     * @param destFile 缩放后另存为的文件，如果存在将覆盖
     * @return 加水印后的文件路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String addWatermark(String srcFile, String watermarkFile, Position position, float opacity, String destFile) throws IOException
    {
        Thumbnails.of(srcFile).scale(1).watermark(position, ImageIO.read(new File(watermarkFile)), opacity).toFile(destFile);
        return destFile;
    }

    /**
     * 转换成 BufferedImage<br />
     * <i>以供其他 API 使用</i>
     * 
     * @param imageFile 原始图片路径
     * @return BufferedImage 对象
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final BufferedImage asBufferedImage(String imageFile) throws IOException
    {
        return Thumbnails.of(imageFile).scale(1).asBufferedImage();
    }

    public static void main(String[] args) throws IOException
    {
        // jpg
        System.out.println(zoomTo("/temp/test/image/jpg-src.jpg", 300, 200, "/temp/test/image/zoomTo-jpg-dest.jpg"));
        System.out.println(resizeTo("/temp/test/image/jpg-src.jpg", 300, 200, "/temp/test/image/resizeTo-jpg-dest.jpg"));
        System.out.println(zoomByRatio("/temp/test/image/jpg-src.jpg", 1.777777777777777d, "/temp/test/image/zoomByRatio-jpg-dest.jpg"));
        System.out.println(rotate("/temp/test/image/jpg-src.jpg", 110, "/temp/test/image/rotate-jpg-dest.jpg"));
        System.out.println(cut("/temp/test/image/jpg-src.jpg", 0, 14, 200, 500, "/temp/test/image/cut-jpg-dest.jpg"));
        System.out.println(convertFormat("/temp/test/image/jpg-src.jpg", "png", "/temp/test/image/convertFormat(png)-jpg-dest.png"));
        System.out.println(compress("/temp/test/image/jpg-src.jpg", .5f, "/temp/test/image/compress-jpg-dest.jpg"));
        System.out.println(addWatermark("/temp/test/image/jpg-src.jpg", "/temp/test/image/watermark-jpg-src.jpg", Positions.BOTTOM_RIGHT, .4f, "/temp/test/image/watermark-jpg-dest.jpg"));
        System.out.println(addWatermark("/temp/test/image/jpg-src.jpg", "/temp/test/image/watermark-png-src.png", Positions.BOTTOM_LEFT, .6f, "/temp/test/image/watermark-png-dest.jpg"));
        System.out.println(asBufferedImage("/temp/test/image/jpg-src.jpg"));

        // png
        System.out.println(zoomTo("/temp/test/image/png-src.png", 600, 400, "/temp/test/image/zoomTo-png-dest.png"));
        System.out.println(resizeTo("/temp/test/image/png-src.png", 600, 400, "/temp/test/image/resizeTo-png-dest.png"));
        System.out.println(zoomByRatio("/temp/test/image/png-src.png", 1.777777777777777d, "/temp/test/image/zoomByRatio-png-dest.png"));
        System.out.println(rotate("/temp/test/image/png-src.png", 110, "/temp/test/image/rotate-png-dest.png"));
        System.out.println(cut("/temp/test/image/png-src.png", 0, 100, 400, 800, "/temp/test/image/cut-png-dest.png"));
        System.out.println(convertFormat("/temp/test/image/png-src.png", "jpg", "/temp/test/image/convertFormat(jpg)-png-dest.jpg"));
        System.out.println(compress("/temp/test/image/png-src.png", .5f, "/temp/test/image/compress-png-dest.png"));
        System.out.println(addWatermark("/temp/test/image/png-src.png", "/temp/test/image/watermark-jpg-src.jpg", Positions.BOTTOM_LEFT, .4f, "/temp/test/image/watermark-jpg-dest.png"));
        System.out.println(addWatermark("/temp/test/image/png-src.png", "/temp/test/image/watermark-png-src.png", Positions.BOTTOM_RIGHT, 1f, "/temp/test/image/watermark-png-dest.png"));
        System.out.println(asBufferedImage("/temp/test/image/png-src.png"));
    }

}
