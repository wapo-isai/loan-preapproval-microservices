package com.loanapproval.applicationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class StreamProducerConfig {

    // This bean's name 'userEvents-out-0' MUST match the binding name in
    // application.yml
    @Bean
    public Supplier<String> appEvents_out_0() { // The method name must match your binding name, replacing '-' with '_'
        // This supplier won't actually be invoked by StreamBridge.send() directly.
        // It simply serves to register the output channel with the binder.
        return () -> null; // Return null as we are imperatively sending
    }
}
