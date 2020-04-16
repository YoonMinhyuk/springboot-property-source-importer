package minhyuk.springboot.importer;

import minhyuk.springboot.importer.config.ConfigurationPropertiesConfig;
import minhyuk.springboot.importer.properties.ConfigApiYmlProperties;
import minhyuk.springboot.importer.properties.ConfigDBYmlProperties;
import minhyuk.springboot.importer.properties.ConfigBatchYamlProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(ConfigurationPropertiesConfig.class)
@ActiveProfiles("prod")
public class ProdProfileTest {
    @Autowired
    private ConfigDBYmlProperties configDBProperties;

    @Autowired
    private ConfigApiYmlProperties configApiProperties;

    @Autowired
    private ConfigBatchYamlProperties batchProperties;

    @Test
    void test() {
        assertThat("oracle").isEqualTo(configDBProperties.getName());
        assertThat("prod").isEqualTo(configApiProperties.getPing());
        assertThat("prod-job").isEqualTo(batchProperties.getJobName());
    }
}
