package com.joindata.inf.common.support.fastdfs.support.component.web;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.joindata.inf.common.support.fastdfs.support.component.FastDfsClient;
import com.joindata.inf.common.util.log.Logger;

/**
 * 自定义的文件上传解析器，支持 FastDFS
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月2日 上午11:17:15
 */
public class FastDfsMultipartResolver extends CommonsMultipartResolver
{
    private static final Logger log = Logger.get();

    private FastDfsClient client;

    public FastDfsMultipartResolver(FastDfsClient client)
    {
        this.client = client;
    }

    private String determineEncoding(String contentTypeHeader, String defaultEncoding)
    {
        if(!StringUtils.hasText(contentTypeHeader))
        {
            return defaultEncoding;
        }
        MediaType contentType = MediaType.parseMediaType(contentTypeHeader);
        Charset charset = contentType.getCharset();
        return (charset != null ? charset.name() : defaultEncoding);
    }

    /**
     * Parse the given List of Commons FileItems into a Spring MultipartParsingResult, containing Spring MultipartFile instances and a Map of multipart parameter.
     * 
     * @param fileItems the Commons FileIterms to parse
     * @param encoding the encoding to use for form fields
     * @return the Spring MultipartParsingResult
     * @see FastDfsMultipartFile#FastDfsMultipartFile(org.apache.commons.fileupload.FileItem)
     */
    @Override
    protected MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding)
    {
        MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();
        Map<String, String[]> multipartParameters = new HashMap<String, String[]>();
        Map<String, String> multipartParameterContentTypes = new HashMap<String, String>();

        // Extract multipart files and multipart parameters.
        for(FileItem fileItem: fileItems)
        {
            if(fileItem.isFormField())
            {
                String value;
                String partEncoding = this.determineEncoding(fileItem.getContentType(), encoding);
                if(partEncoding != null)
                {
                    try
                    {
                        value = fileItem.getString(partEncoding);
                    }
                    catch(UnsupportedEncodingException ex)
                    {
                        log.warn("Could not decode multipart item '" + fileItem.getFieldName() + "' with encoding '" + partEncoding + "': using platform default");
                        value = fileItem.getString();
                    }
                }
                else
                {
                    value = fileItem.getString();
                }
                String[] curParam = multipartParameters.get(fileItem.getFieldName());
                if(curParam == null)
                {
                    // simple form field
                    multipartParameters.put(fileItem.getFieldName(), new String[]{value});
                }
                else
                {
                    // array of simple form fields
                    String[] newParam = StringUtils.addStringToArray(curParam, value);
                    multipartParameters.put(fileItem.getFieldName(), newParam);
                }
                multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
            }
            else
            {
                // multipart file field
                FastDfsMultipartFile file = new FastDfsMultipartFile(fileItem, client);
                multipartFiles.add(file.getName(), file);
                log.debug("Found multipart file [" + file.getName() + "] of size " + file.getSize() + " bytes with original filename [" + file.getOriginalFilename() + "], stored " + file.getStorageDescription());
            }
        }
        return new MultipartParsingResult(multipartFiles, multipartParameters, multipartParameterContentTypes);
    }

    /**
     * Cleanup the Spring MultipartFiles created during multipart parsing, potentially holding temporary data on disk.
     * <p>
     * Deletes the underlying Commons FileItem instances.
     * 
     * @param multipartFiles Collection of MultipartFile instances
     * @see org.apache.commons.fileupload.FileItem#delete()
     */
    @Override
    protected void cleanupFileItems(MultiValueMap<String, MultipartFile> multipartFiles)
    {
        for(List<MultipartFile> files: multipartFiles.values())
        {
            for(MultipartFile file: files)
            {
                if(file instanceof FastDfsMultipartFile)
                {
                    FastDfsMultipartFile cmf = (FastDfsMultipartFile)file;
                    cmf.getFileItem().delete();
                    log.debug("Cleaning up multipart file [" + cmf.getName() + "] with original filename [" + cmf.getOriginalFilename() + "], stored " + cmf.getStorageDescription());
                }
            }
        }
    }

}
