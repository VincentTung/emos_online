package com.vincent.emos.wx.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(){

        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("EMOS在线办公");
        builder.description("Vincent制作");
        docket.apiInfo(builder.build());

        ApiSelectorBuilder apiSelectorBuilder = docket.select();
        apiSelectorBuilder.paths(PathSelectors.any());
        //注意是methodannotation not class
        apiSelectorBuilder.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
        docket = apiSelectorBuilder.build();

        // 界面显示的名字  key名字 往哪里放
        ApiKey apiKey = new ApiKey("token","token","header");
        List<ApiKey> apiKeyList  = new ArrayList<>();
        apiKeyList.add(apiKey);
        docket.securitySchemes(apiKeyList);

        AuthorizationScope scope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope[] scopes = {scope};
        //todo JWT ??
        SecurityReference securityReference  = new SecurityReference("token",scopes);
        List<SecurityReference> referenceList = new ArrayList();
        referenceList.add(securityReference);
        SecurityContext context = SecurityContext.builder().securityReferences(referenceList).build();
        List<SecurityContext> contextList = new ArrayList<>();
        contextList.add(context);
        docket.securityContexts(contextList);

        return docket;
    }
}
