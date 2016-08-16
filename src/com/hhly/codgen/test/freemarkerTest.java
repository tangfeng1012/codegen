package com.hhly.codgen.test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hhly.codgen.util.StringtemplateLoader;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class freemarkerTest {
	
	@Test
	public void testTemplate(){
		Configuration cfg = new Configuration();  
        StringTemplateLoader stringLoader = new StringTemplateLoader();  
        String templateContent="»¶Ó­£º${name}";  
        stringLoader.putTemplate("myTemplate",templateContent);  
//          
//        cfg.setTemplateLoader(stringLoader);  
		cfg.setTemplateLoader(new StringtemplateLoader("»¶Ó­£º${name}"));  
          
        try {  
            Template template = cfg.getTemplate("");  
            Map root = new HashMap<>();    
            root.put("name", "javaboy2012");  
              
            StringWriter writer = new StringWriter();    
            try {  
                template.process(root, writer);  
                System.out.println(writer.toString());    
            } catch (TemplateException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }    
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
	}

}
