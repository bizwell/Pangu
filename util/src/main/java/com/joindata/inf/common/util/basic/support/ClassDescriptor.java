package com.joindata.inf.common.util.basic.support;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;

/**
 * Class 描述器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 14, 2016 12:34:52 PM
 */
public class ClassDescriptor extends ClassVisitor
{

    public ClassDescriptor()
    {
        super(Opcodes.ASM4);
    }

    /**
     * 获取一个 Class 描述器
     * 
     * @param inputStream
     * @return
     */
    public static ClassDescriptor createClassDescriptor(InputStream inputStream)
    {
        try
        {
            ClassReader classReader = new ClassReader(inputStream);
            ClassDescriptor classDescriptor = new ClassDescriptor();
            classReader.accept(classDescriptor, ClassReader.SKIP_CODE);
            return classDescriptor;
        }
        catch(IOException ex)
        {
            return null;
        }
    }
}
