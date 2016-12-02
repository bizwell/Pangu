package com.joindata.inf.common.util.major;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * 条码/二维码工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月10日 下午5:28:56
 */
// TODO 后续有需要可以增加条码工具
public class BarcodeUtil
{
    /**
     * 把 BitMatrix 对象转成 BufferedImage 对象
     * 
     * @param matrix BitMatrix 对象
     * @return BufferedImage 对象
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix)
    {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * 生成一个二维码
     * 
     * @param str 二维码内容字符串
     * @param width 宽度
     * @param height 高度
     * @param targetFormat 目标文件类型
     * @param targetFile 目标文件路径
     * @return 生成的文件路径
     * @throws IOException 保存二维码时发生任何 IO 错误，抛出该异常
     * @throws WriterException 生成二维码时发生任何错误，抛出该异常
     */
    public static final String makeQRCode(String str, int width, int height, String targetFormat, String targetFile) throws IOException, WriterException
    {
        str = new String(str.getBytes("UTF-8"), "ISO-8859-1");
        QRCodeWriter writer = new QRCodeWriter();
        BufferedImage image = toBufferedImage(writer.encode(str, BarcodeFormat.QR_CODE, width, height));
        File f = new File(targetFile);
        f.getParentFile().mkdirs();
        ImageIO.write(image, targetFormat, f);

        return targetFile;
    }

    /**
     * 生成一个二维码
     * 
     * @param str 二维码内容字符串
     * @param width 宽度
     * @param height 高度
     * @param targetFormat 目标文件类型
     * @param targetStream 目标输出流
     * @return 生成的文件路径
     * @throws IOException 保存二维码时发生任何 IO 错误，抛出该异常
     * @throws WriterException 生成二维码时发生任何错误，抛出该异常
     */
    public static final void makeQRCode(String str, int width, int height, String targetFormat, OutputStream targetStream) throws IOException, WriterException
    {
        str = new String(str.getBytes("UTF-8"), "ISO-8859-1");
        QRCodeWriter writer = new QRCodeWriter();
        BufferedImage image = toBufferedImage(writer.encode(str, BarcodeFormat.QR_CODE, width, height));

        ImageIO.write(image, targetFormat, targetStream);
    }

    /**
     * 解析二维码内容
     * 
     * @param imagePath 二维码图片路径
     * @return 二维码中的内容
     * @throws NotFoundException 图片中找不着二维码，抛出该异常
     * @throws ChecksumException 二维码校验失败，抛出该异常
     * @throws FormatException 二维码格式不正确，抛出该异常
     * @throws IOException 读取验证码过程中，发生 IO 错误，抛出该异常
     */
    public static final String parseQRCode(String imagePath) throws NotFoundException, ChecksumException, FormatException, IOException
    {
        QRCodeReader reader = new QRCodeReader();
        LuminanceSource source = new BufferedImageLuminanceSource(ImageIO.read(new File(imagePath)));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return reader.decode(bitmap).getText();
    }

    public static void main(String[] args) throws IOException, WriterException, NotFoundException, ChecksumException, FormatException
    {
        System.out.println(makeQRCode("我是你爸爸，Who is your daddy?", 100, 100, "png", "/temp/test/barcode/makeQRCode.png"));
        System.out.println(parseQRCode("/temp/test/barcode/read.jpg"));
    }
}

class BufferedImageLuminanceSource extends LuminanceSource
{
    private final BufferedImage image;

    private final int left;

    private final int top;

    public BufferedImageLuminanceSource(BufferedImage image)
    {
        this(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public BufferedImageLuminanceSource(BufferedImage image, int left, int top, int width, int height)
    {
        super(width, height);

        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();
        if(left + width > sourceWidth || top + height > sourceHeight)
        {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
        }

        for(int y = top; y < top + height; y++)
        {
            for(int x = left; x < left + width; x++)
            {
                if((image.getRGB(x, y) & 0xFF000000) == 0)
                {
                    image.setRGB(x, y, 0xFFFFFFFF); // = white
                }
            }
        }

        this.image = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
        this.image.getGraphics().drawImage(image, 0, 0, null);
        this.left = left;
        this.top = top;
    }

    public byte[] getRow(int y, byte[] row)
    {
        if(y < 0 || y >= getHeight())
        {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }
        int width = getWidth();
        if(row == null || row.length < width)
        {
            row = new byte[width];
        }
        image.getRaster().getDataElements(left, top + y, width, 1, row);
        return row;
    }

    public byte[] getMatrix()
    {
        int width = getWidth();
        int height = getHeight();
        int area = width * height;
        byte[] matrix = new byte[area];
        image.getRaster().getDataElements(left, top, width, height, matrix);
        return matrix;
    }

    public boolean isCropSupported()
    {
        return true;
    }

    public LuminanceSource crop(int left, int top, int width, int height)
    {
        return new BufferedImageLuminanceSource(image, this.left + left, this.top + top, width, height);
    }

    public boolean isRotateSupported()
    {
        return true;
    }

    public LuminanceSource rotateCounterClockwise()
    {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();
        AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, sourceWidth);
        BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = rotatedImage.createGraphics();
        g.drawImage(image, transform, null);
        g.dispose();
        int width = getWidth();
        return new BufferedImageLuminanceSource(rotatedImage, top, sourceWidth - (left + width), getHeight(), width);
    }
}
