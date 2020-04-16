package minhyuk.springboot.importer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PropertySourceImporter implements EnvironmentPostProcessor, Ordered {
    private final YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
    private final List<String> propertySourceLocationPatterns = new ArrayList<>(2);

    public PropertySourceImporter() {
        addPropertySourcePatten("classpath*:/**/config-*.yml");
        addPropertySourcePatten("classpath*:/**/config-*.yaml");
    }

    private void addPropertySourcePatten(String pattern) {
        if (Objects.isNull(pattern) || pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("pattern cannot be null or empty");
        }
        propertySourceLocationPatterns.add(pattern);
    }


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        PathMatchingResourcePatternResolver patternResolver = generatePathMatchingResourcePatternResolver(application);
        MutablePropertySources propertySources = environment.getPropertySources();
        final List<PropertySource<?>> defaultProfilePropertySources = new ArrayList<>();
        propertySourceLocationPatterns.stream()
                                      .map(locationPattern -> getPropertySourceResource(patternResolver, locationPattern))
                                      .flatMap(Arrays::stream)
                                      .filter(resource -> Objects.nonNull(resource.getFilename()))
                                      .flatMap(resource -> loadYaml(resource, environment, defaultProfilePropertySources).stream())
                                      .forEach(propertySources::addLast);


        defaultProfilePropertySources.forEach(propertySources::addLast);
    }

    private PathMatchingResourcePatternResolver generatePathMatchingResourcePatternResolver(SpringApplication application) {
        return Optional.ofNullable(application.getResourceLoader())
                       .map(PathMatchingResourcePatternResolver::new)
                       .orElseGet(PathMatchingResourcePatternResolver::new);
    }

    private Resource[] getPropertySourceResource(PathMatchingResourcePatternResolver resolver, String locationPattern) {
        try {
            return resolver.getResources(locationPattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<PropertySource<?>> loadYaml(Resource resource,
                                             ConfigurableEnvironment environment,
                                             List<PropertySource<?>> defaultProfilePropertySources) {

        if (!resource.exists()) throw new IllegalArgumentException("Resource " + resource + " does not exist");
        try {
            return yamlPropertySourceLoader.load(resource.getFilename(), resource)
                                           .stream()
                                           .filter(propertySource -> isNotDuplicatePropertySource(environment, propertySource))
                                           .filter(propertySource -> {
                                               if (isAcceptsProfiles(environment, propertySource)) {
                                                   return true;
                                               }
                                               defaultProfilePropertySources.add(propertySource);
                                               return false;
                                           })
                                           .collect(Collectors.toList());

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load yaml configuration from " + resource, e);
        }
    }

    private boolean isNotDuplicatePropertySource(ConfigurableEnvironment environment, PropertySource<?> propertySource) {
        String propertySourceName = propertySource.getName();
        return environment.getPropertySources()
                          .stream()
                          .map(PropertySource::getName)
                          .noneMatch(existentPropertySourceName ->
                                  existentPropertySourceName.contains(propertySource.getName().substring(0, propertySourceName.lastIndexOf(".")))
                          );
    }

    private boolean isAcceptsProfiles(ConfigurableEnvironment environment, PropertySource<?> propertySource) {
        Binder binder = new Binder(ConfigurationPropertySources.from(propertySource));
        return binder.bind("spring.profiles", Bindable.of(String[].class))
                     .map(Arrays::asList)
                     .orElseGet(ArrayList::new)
                     .stream()
                     .anyMatch(propertyProfile -> environment.acceptsProfiles(Profiles.of(propertyProfile)));
    }
}
