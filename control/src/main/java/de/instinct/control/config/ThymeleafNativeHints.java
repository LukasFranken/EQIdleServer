package de.instinct.control.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.thymeleaf.spring6.expression.Mvc;
import org.thymeleaf.spring6.view.ThymeleafView;

@Configuration
@ImportRuntimeHints(ThymeleafNativeHints.ThymeleafHints.class)
public class ThymeleafNativeHints {

	static class ThymeleafHints implements RuntimeHintsRegistrar {
		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			hints.reflection().registerType(ThymeleafView.class,
					MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
					MemberCategory.INVOKE_PUBLIC_METHODS);

			hints.reflection().registerType(Mvc.class, MemberCategory.INVOKE_PUBLIC_METHODS);

			hints.reflection().registerTypeIfPresent(classLoader,
					"org.thymeleaf.spring6.expression.Mvc$Spring41MvcUriComponentsBuilderDelegate",
					MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
					MemberCategory.INVOKE_PUBLIC_METHODS);
		}
	}
}