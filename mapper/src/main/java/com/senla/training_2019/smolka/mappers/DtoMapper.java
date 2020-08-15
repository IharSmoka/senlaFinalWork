package com.senla.training_2019.smolka.mappers;

import com.senla.training_2019.smolka.model.dto.CostDynamicDto;
import com.senla.training_2019.smolka.model.dto.update_dto.CostDynamicChangeDto;
import com.senla.training_2019.smolka.model.entities.CostDynamic;
import com.senla.training_2019.smolka.price_monitoring_utils.csv_parser.PriceMonitoringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public DtoMapper(ModelMapper mapperBean) {
        this.modelMapper = mapperBean;
        TypeMap<CostDynamic, CostDynamicDto> costToDtoTypeMap = this.modelMapper.createTypeMap(CostDynamic.class, CostDynamicDto.class);
        costToDtoTypeMap.addMappings(mapper -> mapper.using(this.toDtoCostConverter()).map(CostDynamic::getCost, CostDynamicDto::setCost));
        TypeMap<CostDynamicChangeDto, CostDynamic> costUpdateDtoToEntityTypeMap = this.modelMapper.createTypeMap(CostDynamicChangeDto.class, CostDynamic.class);
        costUpdateDtoToEntityTypeMap.addMappings(mapper -> mapper.using(this.toEntityCostConverter()).map(CostDynamicChangeDto::getCost, CostDynamic::setCost));
    }

    private Converter<Long, String> toDtoCostConverter() {
        return ctx -> PriceMonitoringUtils.getCostFromMinorVal(ctx.getSource());
    }

    private Converter<String, Long> toEntityCostConverter() {
        return ctx -> PriceMonitoringUtils.getMinorCostFromString(ctx.getSource());
    }

    public <E, D> D mapping(E object, Class<D> clazz) {
        if (object == null) {
            return null;
        }
        return modelMapper.map(object, clazz);
    }

    public <E, D> List<D> toListMapping(List<E> objects, Class<D> cls) {
        if (objects == null) {
            return null;
        }
        return objects.stream().map(i -> mapping(i, cls)).collect(Collectors.toList());
    }
}
