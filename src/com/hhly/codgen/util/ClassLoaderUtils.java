package com.hhly.codgen.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




public class ClassLoaderUtils {
	
	private static Log log = LogFactory.getLog(ClassLoaderUtils.class);
	
	private static ClassLoader getClassLoader(){
		return ClassLoaderUtils.class.getClassLoader();
	}
	
	/**
	 * 从classpath路径查找资源，返回资源的输入流
	 * @param path
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static InputStream getStream(String path) throws MalformedURLException, IOException{
		if(!path.contains("../")){
			return getClassLoader().getResourceAsStream(path);
		}
		return ClassLoaderUtils.getStreamByExtendResource(path);
	}
	
	 public static InputStream getStream(URL url) throws IOException{
         if(url!=null){
             return url.openStream();
         }else{
             return null;
         }
     }
	
//	private static InputStream getStreamByExtendResource(String path){
//		return null;
//	}
	
	/**
    *
    *@paramrelativePath必须传递资源的相对路径。是相对于classpath的路径。如果需要查找classpath外部的资源，需要使用../来查找
    *@return
    *@throwsMalformedURLException
    *@throwsIOException
    */
  public static InputStream getStreamByExtendResource(String relativePath) throws MalformedURLException, IOException{
     return ClassLoaderUtils.getStream(ClassLoaderUtils.getExtendResource(relativePath));
  }
  
	  /**
	  *
	  *@paramrelativePath 必须传递资源的相对路径。是相对于classpath的路径。如果需要查找classpath外部的资源，需要使用../来查找
	  *@return资源的绝对URL
	*@throwsMalformedURLException
	  */
	public static URL getExtendResource(String relativePath) throws MalformedURLException{
	
		ClassLoaderUtils.log.info("传入的相对路径："+relativePath) ;
	    //ClassLoaderUtil.log.info(Integer.valueOf(relativePath.indexOf("../"))) ;
	    if(!relativePath.contains("../")){
	        return ClassLoaderUtils.getResource(relativePath);
	        
	    }
	    String classPathAbsolutePath=ClassLoaderUtils.getAbsolutePathOfClassLoaderClassPath();
	    if(relativePath.substring(0, 1).equals("/")){
	        relativePath=relativePath.substring(1);
	    }
	    ClassLoaderUtils.log.info(Integer.valueOf(relativePath.lastIndexOf("../"))) ;
	   
	    String wildcardString=relativePath.substring(0,relativePath.lastIndexOf("../")+3);
	   relativePath=relativePath.substring(relativePath.lastIndexOf("../")+3);
	    int containSum=ClassLoaderUtils.containSum(wildcardString, "../");
	    classPathAbsolutePath= ClassLoaderUtils.cutLastString(classPathAbsolutePath, "/", containSum);
	    String resourceAbsolutePath=classPathAbsolutePath+relativePath;
	    ClassLoaderUtils.log.info("绝对路径："+resourceAbsolutePath) ;
	    URL resourceAbsoluteURL=new URL(resourceAbsolutePath);
	    return resourceAbsoluteURL;
	}

	/**
	 *得到本Class所在的ClassLoader的Classpat的绝对路径。
	 *URL形式的
	 *@return
	 */
	public static String getAbsolutePathOfClassLoaderClassPath(){
	   
		ClassLoaderUtils.log.info(ClassLoaderUtils.getClassLoader().getResource("").toString());
	   return ClassLoaderUtils.getClassLoader().getResource("").toString();
	   
	}
	
	private static int containSum(String source,String dest){
        int containSum=0;
        int destLength=dest.length();
        while(source.contains(dest)){
            containSum=containSum+1;
            source=source.substring(destLength);
        }
        return containSum;
    }
	
	private static String cutLastString(String source,String dest,int num){
        // String cutSource=null;
        for(int i=0;i<num;i++){
            source=source.substring(0, source.lastIndexOf(dest, source.length()-2)+1);
        }
        return source;
    }
	
	public static URL getResource(String resource){
		ClassLoaderUtils.log.info("传入的相对于classpath的路径："+resource) ;
        return ClassLoaderUtils.getClassLoader().getResource(resource);
    }
	
	public static void main(String[] args) {
		
		try {
//			InputStream in = getStream("com/hhly/codgen/resources/codgen-default.xml");
			InputStream in = getStream("codgen-config.xml");
			System.out.println(in == null);
			System.out.println(IOUtils.toString(in));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
