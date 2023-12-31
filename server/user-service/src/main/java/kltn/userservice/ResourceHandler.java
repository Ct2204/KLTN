package kltn.userservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandler implements WebMvcConfigurer {

    @Value("${image.dir.profile}")
    private String profileImageLocation;

    @Value("${image.dir.product}")
    private String productImageLocation;

    @Value("${video.dir.product}")
    private String productVideoLocation;

    @Value("${image.dir.rating}")
    private String ratingImageLocation;

    @Value("${video.dir.rating}")
    private String ratingVideoLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/images/profile/**",
                        "/images/product/**",
                        "/videos/product/**",
                        "/images/rating/**",
                        "/videos/rating/**")
                .addResourceLocations("file:" + profileImageLocation + '/')
                .addResourceLocations("file:" + productImageLocation + '/')
                .addResourceLocations("file:" + productVideoLocation + '/')
                .addResourceLocations("file:" + ratingImageLocation + '/')
                .addResourceLocations("file:" + ratingVideoLocation + '/');
    }
}
