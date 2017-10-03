package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {
    private String port;
    private String memoryLimit;
    private String instanceIndex;
    private String instanceAddr;

    public EnvController(@Value("${PORT:NOT SET}") String port,
                         @Value("${MEMORY_LIMIT:NOT SET}") String memoryLimit,
                         @Value("${CF_INSTANCE_INDEX:NOT SET}") String instanceIndex,
                         @Value("${CF_INSTANCE_ADDR:NOT SET}") String instanceAddr) {
        this.port = port;
        this.memoryLimit = memoryLimit;
        this.instanceIndex = instanceIndex;
        this.instanceAddr = instanceAddr;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        Map<String, String> envMap = new HashMap<String, String>();
        envMap.put("PORT", getPort());
        envMap.put("MEMORY_LIMIT", getMemoryLimit());
        envMap.put("CF_INSTANCE_INDEX", getInstanceIndex());
        envMap.put("CF_INSTANCE_ADDR", getInstanceAddr());
        return envMap;
    }

    public String getMemoryLimit() {
        return memoryLimit;
    }

    public String getInstanceIndex() {
        return instanceIndex;
    }

    public String getInstanceAddr() {
        return instanceAddr;
    }

    public String getPort() {
        return port;
    }
}
