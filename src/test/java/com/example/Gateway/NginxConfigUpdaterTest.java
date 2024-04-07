package com.example.Gateway;

import com.example.Gateway.Models.ProxyData;
import com.example.Gateway.Service.NginxConfigUpdater;
import com.example.Gateway.Service.ScriptLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class NginxConfigUpdaterTest {


    @Autowired
    NginxConfigUpdater nginxConfigUpdater;

    @Autowired
    ScriptLoader scriptLoader;

    private final String nginxConfPath = "nginx.conf";
    private final String nginxTemplatePath = "nginx_temp.conf";

    @BeforeEach
    void setUp() {
        // Create a temporary nginx configuration file for testing
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptLoader.loadScriptFile(nginxTemplatePath)));
             BufferedWriter writer = new BufferedWriter(new FileWriter(scriptLoader.loadScriptFile(nginxConfPath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Content of nginx_temp.conf copied to nginx.conf for testing.");
        } catch (IOException e) {
            System.err.println("Error copying content from nginx_temp.conf to nginx.conf: " + e.getMessage());
        }
    }

    @Test
    void testUpdateNginxConfig() throws IOException {
        // Add some proxy data for testing
        ProxyData proxy1 = new ProxyData( "localhost", 8080, "/api1/");
        ProxyData proxy2 = new ProxyData("localhost", 8081, "/api2/");
        nginxConfigUpdater.proxies.add(proxy1);
        nginxConfigUpdater.proxies.add(proxy2);

        // Update nginx configuration
        nginxConfigUpdater.updateNginxConfig();

        // Check if nginx configuration file is updated successfully


        // TODO: Add more assertions to check the contents of the nginx configuration file
    }

    @Test
    void testAddProxy() throws IOException {
        // Add a proxy and check if nginx configuration is updated
        ProxyData proxy = new ProxyData("localhost", 8082, "/api3/");
        nginxConfigUpdater.addProxy(proxy);

        // Check if nginx configuration file is updated successfully

        // TODO: Add more assertions to check the contents of the nginx configuration file
    }

    @Test
    void testRemoveProxy() throws IOException {
        // Add a proxy for testing removal
        ProxyData proxy = new ProxyData("localhost", 8083, "/api4/");
        nginxConfigUpdater.addProxy(proxy);

        // Remove the added proxy and check if nginx configuration is updated
        nginxConfigUpdater.removeProxy(proxy);

        // Check if nginx configuration file is updated successfully


        // TODO: Add more assertions to check the contents of the nginx configuration file
    }
}
