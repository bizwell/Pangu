package com.joindata.inf.common.util.basic;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;

import com.joindata.inf.common.util.tools.UuidUtil;
import com.xiaoleilu.hutool.util.ZipUtil;

/**
 * 文件操作相关的工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月4日 下午3:35:14
 */
public class FileUtil
{
    /**
     * 将文件读取到字符串中，使用 UTF-8 编码
     * 
     * @param filePath 路径
     * @return 文件内容作为字符串返回，文件路径为 null，返回 null
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String readFile(String filePath) throws IOException
    {
        if(filePath == null)
        {
            return null;
        }

        return FileUtils.readFileToString(new File(filePath), "UTF-8");
    }

    /**
     * 制作行遍历器
     * 
     * @param filePath 文件路径
     * @return 行遍历器，文件路径为 null，返回 null
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final LineIterator iterateLine(String filePath) throws IOException
    {
        if(filePath == null)
        {
            return null;
        }

        return FileUtils.lineIterator(new File(filePath), "UTF-8");
    }

    /**
     * 将文件读取到字节数组
     * 
     * @param filePath 路径
     * @return 文件内容的字节数组，文件路径为 null，返回 null
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final byte[] readFileToBytes(String filePath) throws IOException
    {
        if(filePath == null)
        {
            return null;
        }

        return FileUtils.readFileToByteArray(new File(filePath));
    }

    /**
     * 将文件读取到字符串中，并指定读取后的编码
     * 
     * @param filePath 路径
     * @param encoding 编码，如 UTF-8、GBK
     * @return 文件内容作为字符串返回，文件路径为null，返回 null
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String readFile(String filePath, String encoding) throws IOException
    {
        if(filePath == null)
        {
            return null;
        }

        return FileUtils.readFileToString(new File(filePath), encoding);
    }

    /**
     * 将输入流写到文件中，使用 UTF-8 编码
     * 
     * @param filePath 路径
     * @param in 要写入的流
     * @throws IOException IO 错误，均抛出该异常
     */
    public static final void writeTo(String filePath, InputStream in) throws IOException
    {
        if(filePath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        com.xiaoleilu.hutool.io.FileUtil.writeFromStream(in, filePath);
    }

    /**
     * 将字符串写到文件中，使用 UTF-8 编码
     * 
     * @param filePath 路径
     * @param content 要写入的内容
     * @throws IOException 路径为 null，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final void writeTo(String filePath, String content) throws IOException
    {
        if(content == null)
        {
            content = "";
        }
        if(filePath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        content = StringUtil.replaceAll(content, "\r", "");

        FileUtils.writeStringToFile(new File(filePath), content, Charset.forName("UTF-8"));
    }

    /**
     * 将字节数组写到文件中
     * 
     * @param filePath 路径
     * @param content 要写入的内容
     * @throws IOException 路径为 null，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final void writeTo(String filePath, byte content[]) throws IOException
    {
        if(filePath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        FileUtils.writeByteArrayToFile(new File(filePath), content);
    }

    /**
     * 将字符串写到文件中，并指定编码
     * 
     * @param filePath 路径
     * @param content 要写入的内容
     * @param encoding 编码，如 UTF-8、GBK
     * @throws IOException 路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final void writeTo(String filePath, String content, String encoding) throws IOException
    {
        if(content == null)
        {
            content = "";
        }
        if(filePath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        FileUtils.writeStringToFile(new File(filePath), content, Charset.forName(encoding));
    }

    /**
     * 将字符串写到文件中，使用 UTF-8 编码
     * 
     * @param filePath 路径
     * @param content 要追加的内容
     * @throws IOException 路径为 null，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final void appendTo(String filePath, String content) throws IOException
    {
        if(content == null)
        {
            content = "";
        }
        if(filePath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        FileUtils.writeStringToFile(new File(filePath), content, Charset.forName("UTF-8"), true);
    }

    /**
     * 将字符串写到文件中，并指定编码
     * 
     * @param filePath 路径
     * @param content 要追加的内容
     * @param encoding 编码，如 UTF-8、GBK
     * @throws IOException 路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final void appendTo(String filePath, String content, String encoding) throws IOException
    {
        if(content == null)
        {
            content = "";
        }
        if(filePath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        FileUtils.writeStringToFile(new File(filePath), content, Charset.forName(encoding), true);
    }

    /**
     * 复制文件或文件夹<br />
     * <i>如果目标文件存在，则替换之。<i><br />
     * <i>如果目标文件夹存在，则合并之。</i>
     * 
     * @param src 源文件名
     * @param dest 目标文件名
     * @throws IOException 传入的路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final void copy(String src, String dest) throws IOException
    {
        if(src == null || dest == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        File srcFile = new File(src);
        File destFile = new File(dest);

        if(srcFile.isDirectory())
        {
            FileUtils.copyDirectory(srcFile, destFile);
        }
        else
        {
            FileUtils.copyFile(srcFile, destFile);
        }
    }

    /**
     * 复制文件或文件夹，到另外一个目录下<br />
     * 
     * <i>如果已存在同名文件，则替换之。<i><br />
     * <i>如果已存在同名文件夹，则合并之。</i>
     * 
     * @param src 源文件或文件夹路径
     * @param dest 目标文件夹
     * @throws IOException 传入的路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final void copyToDir(String src, String dest) throws IOException
    {
        if(src == null || dest == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        File srcFile = new File(src);
        File destDir = new File(dest);

        if(srcFile.isDirectory())
        {
            FileUtils.copyDirectoryToDirectory(srcFile, destDir);
        }
        else
        {
            FileUtils.copyFileToDirectory(srcFile, destDir);
        }
    }

    /**
     * 复制文件或文件夹，到另外一个目录下<br />
     * 
     * <i>如果已存在同名文件，则替换之。<i><br />
     * <i>如果已存在同名文件夹，则合并之。</i>
     * 
     * @param src 源文件或文件夹路径
     * @param dest 目标文件夹
     * @throws IOException 传入的路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final void copyToDir(File srcFile, File destDir) throws IOException
    {
        if(srcFile == null || destDir == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        if(srcFile.isDirectory())
        {
            FileUtils.copyDirectoryToDirectory(srcFile, destDir);
        }
        else
        {
            FileUtils.copyFileToDirectory(srcFile, destDir);
        }
    }

    /**
     * 移动文件或文件夹<br />
     * 
     * <i>如果目标文件存在，则替换之。<i><br />
     * <i>如果目标文件夹存在，则合并之。</i>
     * 
     * @param src 源文件名
     * @param dest 目标文件名
     * @throws IOException 发生任何其他 IO 错误，均抛出该异常
     */
    public static final void move(String src, String dest) throws IOException
    {
        File srcFile = new File(src);
        File destFile = new File(dest);

        if(srcFile.isDirectory())
        {
            FileUtils.moveDirectory(srcFile, destFile);
        }
        else
        {
            if(destFile.exists())
            {
                FileUtils.forceDelete(destFile);
            }
            FileUtils.moveFile(srcFile, destFile);
        }
    }

    /**
     * 移动文件或文件夹，到另外一个目录下<br />
     * 
     * <i>如果已存在同名文件，则替换之。<i><br />
     * <i>如果已存在同名文件夹，则合并之。</i>
     * 
     * @param src 源文件或文件夹路径
     * @param dest 目标文件夹
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final void moveToDir(String src, String dest) throws IOException
    {
        File srcFile = new File(src);
        File destDir = new File(dest);

        if(srcFile.isDirectory())
        {
            FileUtils.moveDirectoryToDirectory(srcFile, destDir, false);
        }
        else
        {
            String fileName = FileUtil.getName(src);
            if(hasFile(dest, fileName))
            {
                FileUtil.delete(destDir + "/" + fileName);
            }
            FileUtils.moveFileToDirectory(srcFile, destDir, false);
        }
    }

    /**
     * 创建一个空文件 <br />
     * <i>如果文件存在，则啥也不干</i>
     * 
     * @param filePath 路径
     * @return 和参数相同
     * @throws IOException 传入的路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final String createFile(String filePath) throws IOException
    {
        if(filePath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        if(FileUtil.isExists(filePath))
        {
            return filePath;
        }

        FileUtils.touch(new File(filePath));

        return filePath;
    }

    /**
     * 删除文件或文件夹
     * 
     * @param filePath 文件或文件夹路径
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final void delete(String filePath) throws IOException
    {
        if(filePath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        if(!FileUtil.isExists(filePath))
        {
            return;
        }

        FileUtils.forceDelete(new File(filePath));
    }

    /**
     * 创建文件夹
     * 
     * @param dirPath 文件夹路径
     * @return 和参数一样
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String mkdirs(String dirPath) throws IOException
    {

        if(dirPath == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        FileUtils.forceMkdir(new File(dirPath));

        return dirPath;

    }

    /**
     * 获取文件或文件夹的内容大小
     * 
     * @param path 文件或文件夹路径
     * 
     * @throws IOException 传入的路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final long getSize(String path) throws IOException
    {

        if(path == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        return FileUtils.sizeOf(new File(path));
    }

    /**
     * 将文件大小解析为可读性高的字符串<br />
     * <i>如，转换为 K、M、G、T、P 单位</i>
     * 
     * @param size 字节数
     * @return 可读的文件大小
     */
    public static final String getDisplaySize(long size)
    {
        return FileUtils.byteCountToDisplaySize(size);
    }

    /**
     * 列出文件夹内的所有文件名
     * 
     * @param dirPath 文件夹路径
     * @return 下属文件名列表数组，不会是 null
     */
    public static final String[] listDir(String dirPath)
    {
        File file = new File(dirPath);
        if(dirPath == null || !file.isDirectory())
        {
            return new String[]{};
        }

        return file.list();
    }

    /**
     * 列出文件夹内的所有文件
     * 
     * @param dirPath 文件夹路径
     * @return 下属文件名列表数组，不会是 null
     */
    public static final String[] listDirFiles(String dirPath)
    {
        File file = new File(dirPath);
        if(dirPath == null || !file.isDirectory())
        {
            return new String[]{};
        }

        File files[] = file.listFiles(new FileFilter()
        {

            @Override
            public boolean accept(File pathname)
            {
                if(pathname.isDirectory())
                {
                    return false;
                }
                return true;
            }
        });

        if(files != null)
        {
            String fileNames[] = new String[files.length];
            for(int i = 0; i < files.length; i++)
            {
                fileNames[i] = files[i].getName();
            }

            return fileNames;
        }
        else
        {
            return new String[]{};
        }
    }

    /**
     * 列出文件夹内的所有文件夹
     * 
     * @param dirPath 文件夹路径
     * @return 下属文件夹名列表数组，不会是 null
     */
    public static final String[] listDirDirs(String dirPath)
    {
        File file = new File(dirPath);
        if(dirPath == null || !file.isDirectory())
        {
            return new String[]{};
        }

        File files[] = file.listFiles(new FileFilter()
        {

            @Override
            public boolean accept(File pathname)
            {
                if(pathname.isDirectory())
                {
                    return true;
                }
                return false;
            }
        });

        if(files != null)
        {
            String fileNames[] = new String[files.length];
            for(int i = 0; i < files.length; i++)
            {
                fileNames[i] = files[i].getName();
            }

            return fileNames;
        }
        else
        {
            return new String[]{};
        }
    }

    /**
     * 列出文件夹内的所有文件路径
     * 
     * @param dirPath 文件夹路径
     * @return 下属文件路径列表数组，不会是 null
     */
    public static final String[] listDirAsFullPath(String dirPath)
    {
        File file = new File(dirPath);
        if(dirPath == null || !file.isDirectory())
        {
            return new String[]{};
        }

        File files[] = file.listFiles();
        String paths[] = new String[files.length];

        for(int i = 0; i < files.length; i++)
        {
            paths[i] = files[i].getPath();
        }

        return paths;
    }

    /**
     * 判断文件 A 是否比文件 B 创建时间晚
     * 
     * @param fileA 文件 A
     * @param fileB 文件 B
     * @return A 比 B 晚（新），返回 true
     */
    public static final boolean isNewerThan(String fileA, String fileB)
    {
        File a = new File(fileA);
        File b = new File(fileB);

        return FileUtils.isFileNewer(a, b);
    }

    /**
     * 判断文件夹下是否有指定的文件或文件夹
     * 
     * @param dir 文件夹名
     * @param name 要判断的文件名
     * @return 如果有，返回 true
     * 
     * @throws IOException 传入的路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final boolean hasFile(String dir, String name) throws IOException
    {
        if(dir == null)
        {
            throw new FileNotFoundException("没有指定正确的路径！");
        }

        if(name == null)
        {
            return false;
        }

        return new File(dir + "/" + name).exists();
    }

    /**
     * 判断文件或文件夹是否存在
     * 
     * @param path 文件路径
     * @return 如果有，返回 false
     */
    public static final boolean isExists(String path)
    {
        if(path == null)
        {
            return false;
        }
        return new File(path).exists();
    }

    /**
     * 获取父级路径
     * 
     * @param path 文件路径
     * @return 父级路径
     */
    public static final String getParentPath(String path)
    {
        if(path == null)
        {
            return null;
        }

        File f = new File(path);

        if(!f.exists())
        {
            return null;
        }

        return f.getParent();
    }

    /**
     * 获取文件或文件夹全名<br />
     * <i>只返回名字，不返回扩展名</i>
     * 
     * @param path 文件或文件夹路径
     * @return 文件或文件夹名
     */
    public static final String getBaseName(String path)
    {
        return FilenameUtils.getBaseName(path);
    }

    /**
     * 获取文件或文件夹名<br />
     * <i>如果是文件，返回文件名称+扩展名</i>
     * 
     * @param path 文件或文件夹路径
     * @return 文件或文件夹名
     */
    public static final String getName(String path)
    {
        return FilenameUtils.getName(path);
    }

    /**
     * 获取文件扩展名<br />
     * <i>如果没有扩展名，返回空字符串</i>
     * 
     * @param path 文件路径
     * @return 文件扩展名
     */
    public static final String getExtension(String path)
    {
        return FilenameUtils.getExtension(path);
    }

    /**
     * 压缩文件/文件夹
     * 
     * @param path 文件/文件夹路径
     * @param destPath 目标文件
     * @return 压缩后的文件路径
     * @throws IOException 传入的路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final String zip(String path, String destPath) throws IOException
    {
        if(StringUtil.isBlank(path) || StringUtil.isBlank(destPath))
        {
            throw new FileNotFoundException("文件路径不对");
        }

        File zipFile = ZipUtil.zip(path);
        move(zipFile.getPath(), destPath);

        return destPath;
    }

    /**
     * 压缩文件/文件夹
     * 
     * @param path 生成的 ZIP 文件
     * @param files 要添加到 ZIP 的文件
     * @return 压缩后的文件路径
     * @throws IOException 传入的路径为 null ，或发生任何其他 IO 错误，均抛出该异常
     */
    public static final String zip(File zipFile, File... files) throws IOException
    {
        if(ArrayUtil.isEmpty(files) || zipFile == null)
        {
            throw new FileNotFoundException("文件路径不对");
        }

        ZipUtil.zip(zipFile, true, files);

        return zipFile.getPath();
    }

    /**
     * 解压文件
     * 
     * @param zipFile 要解压的文件
     * @param destDir 目标文件夹，解压后的内容都会放到这里
     * @return 目标文件夹
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String unzip(String zipFile, String destDir) throws IOException
    {
        mkdirs(destDir);
        ZipUtil.unzip(zipFile, destDir);
        return destDir;
    }

    /**
     * 判断给定路径是否是个文件夹
     * 
     * @param filePath 文件路径
     * @return true，如果是文件夹
     */
    public static boolean isDir(String filePath)
    {
        return new File(filePath).isDirectory();
    }

    /**
     * 给一个文件重命名一个随机文件名，扩展名不变
     * 
     * @param fileName 文件全名
     * @return 随机文件全名，格式为“当前毫秒数-当前纳秒数.扩展名”
     */
    public static final String randomRename(String fileName)
    {
        return UuidUtil.make() + "." + FileUtil.getExtension(fileName);
    }

    public static void main(String[] args) throws IOException
    {
        System.out.println(readFile("/temp/test/read.txt"));
        System.out.println(readFileToBytes("/temp/test/read.txt").length);
        System.out.println(readFile("/temp/test/read.txt", "GBK"));
        {
            writeTo("/temp/test/write1.txt", "卖女孩的小火柴1。");
            System.out.println(readFile("/temp/test/write1.txt"));
        }
        {
            writeTo("/temp/test/write2.txt", "卖女孩的小火柴1，once upon a time", "GBK");
            System.out.println(readFile("/temp/test/write2.txt", "GBK"));
        }
        {
            writeTo("/temp/test/write2-b.txt", "卖女孩的小火柴2-b，once upon a time".getBytes());
            System.out.println(readFile("/temp/test/write2-b.txt", "GBK"));
        }
        {
            appendTo("/temp/test/append1.txt", "卖女孩的小火柴2。");
            System.out.println(readFile("/temp/test/append1.txt"));
        }
        {
            appendTo("/temp/test/append2.txt", "卖女孩的小火柴2，once upon a time", "GBK");
            System.out.println(readFile("/temp/test/append2.txt", "GBK"));
        }
        {
            copy("/temp/test/copy-src.txt", "/temp/test/copy-dest.txt");
        }
        {
            mkdirs("/temp/test/copyToDir");
            copyToDir("/temp/test/copy-src.txt", "/temp/test/copyToDir");
        }
        {
            writeTo("/temp/test/move-src.txt", "测试移动");
            move("/temp/test/move-src.txt", "/temp/test/move-dest.txt");
        }
        {
            mkdirs("/temp/test/moveToDir");
            writeTo("/temp/test/move-src.txt", "测试移动到文件夹");
            moveToDir("/temp/test/move-src.txt", "/temp/test/moveToDir");
        }
        System.out.println(createFile("/temp/test/createFile.txt"));
        {
            createFile("/temp/test/testDelete.txt");
            System.out.print(isExists("/temp/test/testDelete.txt") + " >> ");
            delete("/temp/test/testDelete.txt");
            System.out.println(isExists("/temp/test/testDelete.txt"));
        }
        System.out.println(mkdirs("/temp/test/mkdirs"));
        {
            delete("/temp/test/size.txt");
            for(int i = 0; i < 2000; i++)
            {
                appendTo("/temp/test/size.txt", "阿拉斯加的法拉克三等奖法拉克交水电费老卡死的冷风机阿斯兰的咖啡机\n");
            }
            System.out.println(getSize("/temp/test/size.txt"));
            System.out.println(getDisplaySize(getSize("/temp/test/size.txt")));
        }
        {
            for(String name: listDir("/temp/test"))
            {
                System.out.println(name);
            }
            for(String path: listDirAsFullPath("/temp/test"))
            {
                System.out.println(path);
            }
        }

        {
            createFile("/temp/test/older-file.txt");
            createFile("/temp/test/newer-file.txt");
            System.out.println(isNewerThan("/temp/test/older-file.txt", "/temp/test/newer-file.txt"));
            System.out.println(isNewerThan("/temp/test/newer-file.txt", "/temp/test/older-file.txt"));
        }

        {
            createFile("/temp/test/hasFile/test-has-file/1.txt");
            createFile("/temp/test/hasFile/test-has-file/2.txt");
            createFile("/temp/test/hasFile/test-has-file/3.txt");
            createFile("/temp/test/hasFile/test-has-file/4.txt");
            createFile("/temp/test/hasFile/test-has-file/5.txt");
            System.out.println(hasFile("/temp/test/hasFile/test-has-file/", "1.txt"));
            System.out.println(hasFile("/temp/test/hasFile/test-has-file/", "10.txt"));
        }
        {
            createFile("/temp/test/hasFile/exist.txt");
            System.out.println(isExists("/temp/test/hasFile/exist.txt"));
            System.out.println(isExists("/temp/test/hasFile/not-exist.txt"));
        }
        {
            createFile("/temp/test/get-parent-path.txt");
            System.out.println(getParentPath("/temp/test/get-parent-path.txt"));
        }

        {
            createFile("/temp/test/test-get-name.txt");
            System.out.println(getName("/temp/test/test-get-name.txt"));
            System.out.println(getBaseName("/temp/test/test-get-name.txt"));
            System.out.println(getExtension("/temp/test/test-get-name.txt"));
        }

        {
            System.out.println(zip("/temp/test/zip/src-dir", "/temp/test/zip/dest.zip"));
            System.out.println(unzip("/temp/test/zip/src.zip", "/temp/test/zip/dest-dir"));
        }

    }

}
