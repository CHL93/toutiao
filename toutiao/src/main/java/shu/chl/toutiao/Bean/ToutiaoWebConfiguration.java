package shu.chl.toutiao.Bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import shu.chl.toutiao.Interceptor.PassportInterceptor;

@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
       //  registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/msg/*");
        super.addInterceptors(registry);
    }
}
