package com.example.Gateway.Controller;


import com.example.Gateway.Models.ProxyData;
import com.example.Gateway.Service.NginxConfigUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/gateway/")
public class NginxConfController {


    @Autowired
    NginxConfigUpdater nginxConfigUpdater;

    @PostMapping("/add")
    public String addProxy(@RequestBody ProxyData proxyData) throws IOException {
        nginxConfigUpdater.addProxy(proxyData);
        return "Proxy added successfully.";
    }

    @PostMapping("/remove")
    public String removeProxy(@RequestBody ProxyData proxyData) throws IOException {
        nginxConfigUpdater.removeProxy(proxyData);
        return "Proxy removed successfully.";
    }


}
