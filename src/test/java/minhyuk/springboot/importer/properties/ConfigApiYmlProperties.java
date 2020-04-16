package minhyuk.springboot.importer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("api")
@ConstructorBinding
public class ConfigApiYmlProperties {
    private String ping;

    public ConfigApiYmlProperties(String ping) {
        this.ping = ping;
    }

    public String getPing() {
        return ping;
    }

    @Override
    public String toString() {
        return "ConfigApiYmlProperties{" +
                "ping='" + ping + '\'' +
                '}';
    }
}
