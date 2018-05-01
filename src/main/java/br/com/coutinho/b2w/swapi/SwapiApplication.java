package br.com.coutinho.b2w.swapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/** Simple class to start up the application.
 *
 * @SpringBootApplication adds:
 *  @Configuration
 *  @EnableAutoConfiguration
 *  @ComponentScan
 */
@SpringBootApplication
@EnableSpringDataWebSupport
public class SwapiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SwapiApplication.class, args);
    }

}
