package test.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;

@Configuration
public class ServiceConfig {

    @Bean
    public TextractClient textractClient() {
        return TextractClient.builder()
                .region(Region.US_EAST_1)  // выбери регион, где доступен Textract
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

}
