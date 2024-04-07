package com.example.Gateway.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
public class ScriptLoader {

    private final ResourceLoader resourceLoader;



    public ScriptLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }



    public File loadScriptFile(String scriptName) throws IOException {


//        System.out.println("message2");
        String address="classpath:/" + scriptName;
        log.info(address);
        Resource resource = resourceLoader.getResource("classpath:/" + scriptName);
        System.out.println("message2");
        try (InputStream inputStream = resource.getInputStream()) {
            log.info("project sourcedir found");
            File tempFile = File.createTempFile(scriptName, null);
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        }
        catch(Error e){
            log.info("error",e);
        }
        log.info("project sourcedir not found");
        return null;
    }
}
