package utils;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 */
public class MockMVCGenerator {
    public static MockMvc generate(Object... controllers) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/views/");
        viewResolver.setSuffix(".ftl");
        return MockMvcBuilders
                .standaloneSetup(controllers)
                .setViewResolvers(viewResolver)
                .build();
    }
}
