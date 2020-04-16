package minhyuk.springboot.importer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("batch")
@ConstructorBinding
public class ConfigBatchYamlProperties {
    private String jobName;

    public ConfigBatchYamlProperties(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    @Override
    public String toString() {
        return "ConfigBatchYamlProperties{" +
                "jobName='" + jobName + '\'' +
                '}';
    }
}
