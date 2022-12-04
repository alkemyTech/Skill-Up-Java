package com.alkemy.wallet.config;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

public class SwaggerConfig {

    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiDetails())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiDetails(){
        return new ApiInfo("Skill-Up Java Wallet",
                "Multicurrency wallet",
                "1.0",
                "https://images.prismic.io/vivawallet/e19ce070-6569-4b4e-ba42-29d9fd7a4162_OG_Terms-and-conditions_web.png?auto=compress,format&rect=0,0,1200,630&w=1200&h=630",
                new Contact("Turco",
                        "https://discord.com/channels/1034506048620474428/1045048505217077338",
                        "contra3000@gmail.com"),
                "MIT mir",
                "https://www.outlookindia.com/outlooktraveller/photos/a-sample-drivers-license/20358",
                Collections.emptyList());
    }
}
