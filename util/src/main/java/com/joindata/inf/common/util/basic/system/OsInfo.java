package com.joindata.inf.common.util.basic.system;

/**
 * 操作系统类： 获取System.getProperty("os.name")对应的操作系统
 * 
 * @author isea533
 */
public class OsInfo
{
    private static String OS = System.getProperty("os.name").toLowerCase();

    private static OsInfo _instance = new OsInfo();

    private SystemPlatform platform;

    private OsInfo()
    {
    }

    public static boolean isLinux()
    {
        return OS.indexOf("linux") >= 0;
    }

    public static boolean isMacOS()
    {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
    }

    public static boolean isMacOSX()
    {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isWindows()
    {
        return OS.indexOf("windows") >= 0;
    }

    public static boolean isOS2()
    {
        return OS.indexOf("os/2") >= 0;
    }

    public static boolean isSolaris()
    {
        return OS.indexOf("solaris") >= 0;
    }

    public static boolean isSunOS()
    {
        return OS.indexOf("sunos") >= 0;
    }

    public static boolean isMPEiX()
    {
        return OS.indexOf("mpe/ix") >= 0;
    }

    public static boolean isHPUX()
    {
        return OS.indexOf("hp-ux") >= 0;
    }

    public static boolean isAix()
    {
        return OS.indexOf("aix") >= 0;
    }

    public static boolean isOS390()
    {
        return OS.indexOf("os/390") >= 0;
    }

    public static boolean isFreeBSD()
    {
        return OS.indexOf("freebsd") >= 0;
    }

    public static boolean isIrix()
    {
        return OS.indexOf("irix") >= 0;
    }

    public static boolean isDigitalUnix()
    {
        return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare()
    {
        return OS.indexOf("netware") >= 0;
    }

    public static boolean isOSF1()
    {
        return OS.indexOf("osf1") >= 0;
    }

    public static boolean isOpenVMS()
    {
        return OS.indexOf("openvms") >= 0;
    }

    /**
     * 获取当前操作系统信息
     * 
     * @return 操作系统平台枚举
     */
    public static SystemPlatform getOsName()
    {
        if(isAix())
        {
            _instance.platform = SystemPlatform.AIX;
        }
        else if(isDigitalUnix())
        {
            _instance.platform = SystemPlatform.Digital_Unix;
        }
        else if(isFreeBSD())
        {
            _instance.platform = SystemPlatform.FreeBSD;
        }
        else if(isHPUX())
        {
            _instance.platform = SystemPlatform.HP_UX;
        }
        else if(isIrix())
        {
            _instance.platform = SystemPlatform.Irix;
        }
        else if(isLinux())
        {
            _instance.platform = SystemPlatform.Linux;
        }
        else if(isMacOS())
        {
            _instance.platform = SystemPlatform.Mac_OS;
        }
        else if(isMacOSX())
        {
            _instance.platform = SystemPlatform.Mac_OS_X;
        }
        else if(isMPEiX())
        {
            _instance.platform = SystemPlatform.MPEiX;
        }
        else if(isNetWare())
        {
            _instance.platform = SystemPlatform.NetWare_411;
        }
        else if(isOpenVMS())
        {
            _instance.platform = SystemPlatform.OpenVMS;
        }
        else if(isOS2())
        {
            _instance.platform = SystemPlatform.OS2;
        }
        else if(isOS390())
        {
            _instance.platform = SystemPlatform.OS390;
        }
        else if(isOSF1())
        {
            _instance.platform = SystemPlatform.OSF1;
        }
        else if(isSolaris())
        {
            _instance.platform = SystemPlatform.Solaris;
        }
        else if(isSunOS())
        {
            _instance.platform = SystemPlatform.SunOS;
        }
        else if(isWindows())
        {
            _instance.platform = SystemPlatform.Windows;
        }
        else
        {
            _instance.platform = SystemPlatform.Others;
        }
        return _instance.platform;
    }
}