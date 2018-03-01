package com.ge.digital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        String vcap_application = System.getenv("VCAP_APPLICATION");
        String instanceName = "local instance";
        try {
            if (vcap_application != null) {
                Map<String, Object> vcapEnv = objectMapper.readValue(vcap_application,
                        TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class));
                instanceName = vcapEnv.get("application_name").toString();
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        MDC.put("instance_name", instanceName);
        SpringApplication.run(Application.class, args);
    }
}
