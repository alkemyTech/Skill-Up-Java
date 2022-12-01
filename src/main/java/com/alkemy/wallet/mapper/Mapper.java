package com.alkemy.wallet.mapper;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class Mapper {
    @Bean
    public ModelMapper getMapper() {
        var modelmapper = new ModelMapper();
        var config = modelmapper.getConfiguration();
        config.setPropertyCondition(Conditions.isNotNull());
        return modelmapper;
    }
}
