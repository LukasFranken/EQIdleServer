package de.instinct.api.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import de.instinct.api.core.reflection.RegisterReflection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@RegisterReflection
public @interface Dto {

}
