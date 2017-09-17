package ua.training.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ua.training.model.service", "ua.training.model.util"})
public class ServiceConfig {}
