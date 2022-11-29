package com.company.icons.helpers;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    @Bean
    public ModelMapper getMapper() {
        var modelmapper = new ModelMapper();
        var config = modelmapper.getConfiguration();
        config.setPropertyCondition(Conditions.isNotNull());
        return modelmapper;
    }
}
