package minhyuk.springboot.importer.config;

import minhyuk.springboot.importer.properties.ConfigApiYmlProperties;
import minhyuk.springboot.importer.properties.ConfigDBYmlProperties;
import minhyuk.springboot.importer.properties.ConfigBatchYamlProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@EnableConfigurationProperties({
        ConfigDBYmlProperties.class,
        ConfigApiYmlProperties.class,
        ConfigBatchYamlProperties.class
})
public class ConfigurationPropertiesConfig {
}
