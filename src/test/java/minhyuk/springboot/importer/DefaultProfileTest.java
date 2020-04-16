package minhyuk.springboot.importer;

import minhyuk.springboot.importer.config.ConfigurationPropertiesConfig;
import minhyuk.springboot.importer.properties.ConfigApiYmlProperties;
import minhyuk.springboot.importer.properties.ConfigBatchYamlProperties;
import minhyuk.springboot.importer.properties.ConfigDBYmlProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(ConfigurationPropertiesConfig.class)
public class DefaultProfileTest {
    @Autowired
    private ConfigDBYmlProperties configDBProperties;

    @Autowired
    private ConfigApiYmlProperties configApiProperties;

    @Autowired
    private ConfigBatchYamlProperties batchProperties;

    @Test
    void test() {
        assertThat("h2").isEqualTo(configDBProperties.getName());
        assertThat("default").isEqualTo(configApiProperties.getPing());
        assertThat("default-job").isEqualTo(batchProperties.getJobName());
    }
}
