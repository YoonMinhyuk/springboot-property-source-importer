package minhyuk.springboot.importer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("db")
@ConstructorBinding
public class ConfigDBYmlProperties {
    private String name;

    public ConfigDBYmlProperties(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ConfigDBYmlProperties{" +
                "name='" + name + '\'' +
                '}';
    }
}
