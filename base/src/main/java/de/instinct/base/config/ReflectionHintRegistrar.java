package de.instinct.base.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.instinct.api.core.reflection.RegisterReflection;

@Configuration
@ImportRuntimeHints(ReflectionHintRegistrar.class)
public class ReflectionHintRegistrar implements RuntimeHintsRegistrar {

    private static final String BASE_PACKAGE = "de.instinct.api";

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RegisterReflection.class));
        scanner.setResourceLoader(resourceLoader(classLoader));

        for (BeanDefinition bd : scanner.findCandidateComponents(BASE_PACKAGE)) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                hints.reflection()
                     .registerType(clazz,
                                   MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                                   MemberCategory.PUBLIC_FIELDS,
                                   MemberCategory.INVOKE_PUBLIC_METHODS);
            }
            catch (ClassNotFoundException e) {
                // ignore or log warning
            }
        }
    }

    private ResourceLoader resourceLoader(ClassLoader cl) {
        DefaultResourceLoader loader = new DefaultResourceLoader();
        loader.setClassLoader(cl);
        return loader;
    }
    
}