package com.example.Gateway.Service;

import com.example.Gateway.Models.ProxyData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@Service
public class NginxConfigUpdater {

    @Autowired
    private ScriptLoader scriptLoader;

    @Autowired
    private ResourceLoader resourceLoader;
    public String nginxConfPath = "nginx.conf";
    public  String nginxTemplatePath = "nginx_temp.conf";

    public  List<ProxyData> proxies = new ArrayList<ProxyData>();

    public NginxConfigUpdater() {
    }

    public  void updateNginxConfig() throws IOException {
        // Read the template nginx.conf file
        StringBuilder nginxConfigBuilder = new StringBuilder();
        File temp_conf = scriptLoader.loadScriptFile(nginxTemplatePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(temp_conf))) {
            String line;
            while ((line = reader.readLine()) != null) {
                nginxConfigBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading template nginx.conf file: " + e.getMessage());
            return;
        }

        // Find the last occurrence of the brace
        int lastIndex = nginxConfigBuilder.lastIndexOf("}");
        if (lastIndex == -1) {
            System.err.println("Failed to locate the closing brace in nginx.conf file.");
            return;
        }

        // Find the second last occurrence of the brace
        int secondLastIndex = nginxConfigBuilder.substring(0, lastIndex).lastIndexOf("}");
        if (secondLastIndex == -1) {
            System.err.println("Failed to locate the second last closing brace in nginx.conf file.");
            return;
        }


        StringBuilder locationDirectives = new StringBuilder();
        for (ProxyData proxy : proxies) {
            String newLocationDirective = "     location " + proxy.getPath() + " {\n" +
                    "                   proxy_pass http://" + proxy.getIp() + ":" + proxy.getPort() + ";\n" +
                    "                   proxy_set_header Host $host;\n" +
                    "                   proxy_set_header X-Real-IP $remote_addr;\n" +
                    "                   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n" +
                    "                   proxy_set_header X-Forwarded-Proto $scheme;\n" +
                    "           }\n";
            locationDirectives.append(newLocationDirective);
        }

        nginxConfigBuilder.insert(secondLastIndex, locationDirectives);




        // Write content to a script file
        writeScriptFile(nginxConfPath, nginxConfigBuilder.toString());
//       writeScriptFile(nginxConfPath, );
    }




    public void writeScriptFile(String scriptName, String content) throws IOException {
        // Create a temporary file to write the content
        File tempFile = File.createTempFile(scriptName, null);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            // Write the content to the temporary file
            writer.write(content);
        }

        // Get the path to the classpath resource
        String resourcePath = resourceLoader.getResource("classpath:/" + scriptName).getFile().getPath();

        log.info("resource path:: {}" , resourcePath);

        // Copy the content of the temporary file to the classpath resource
        Files.copy(tempFile.toPath(), new File(resourcePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Delete the temporary file
        tempFile.delete();
    }

    public  void addProxy(ProxyData proxy) throws IOException {
        proxies.add(proxy);
        updateNginxConfig();
    }

    public  void removeProxy(ProxyData proxy) throws IOException {
        proxies.remove(proxy);
        updateNginxConfig();
    }


}
