package minhyuk.springboot.importer;

import minhyuk.springboot.importer.config.ConfigurationPropertiesConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(ConfigurationPropertiesConfig.class)
@ActiveProfiles("local")
public class DuplicatePropertySourceTest {
    @Autowired
    private ConfigurableEnvironment environment;

    @Test
    @DisplayName("does not contains config-db.yaml")
    void test() {
        IntStream.range(0, 10000).forEach(value -> {
            //given when
            boolean matchConfigDbYaml = environment.getPropertySources()
                                                   .stream()
                                                   .map(PropertySource::getName)
                                                   .anyMatch(propertySourceName -> propertySourceName.contains("config-db.yaml"));

            boolean matchConfigDbYml = environment.getPropertySources()
                                                  .stream()
                                                  .map(PropertySource::getName)
                                                  .anyMatch(propertySourceName -> propertySourceName.contains("config-db.yml"));

            //then
            assertThat(matchConfigDbYaml).isFalse();
            assertThat(matchConfigDbYml).isTrue();
        });
    }
}
