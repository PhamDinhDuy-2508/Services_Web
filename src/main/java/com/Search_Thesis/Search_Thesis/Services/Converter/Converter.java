package com.Search_Thesis.Search_Thesis.Services.Converter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Converter<T, U> {
    private final Function<T, U> fromDto;
    private final Function<U, T> fromEntity;

    public Converter(Function<T, U> fromDto, Function<U, T> fromEntity) {
        this.fromDto = fromDto;
        this.fromEntity = fromEntity;
    }

    public U convertFromDto(final T dto) {
        return fromDto.apply(dto);
    }

    public T convertFromEntity(final U entity) {
        return  fromEntity.apply(entity) ;
    }
    public  List<U> convertFromEntityListType(final Collection<T> dtos) {
        return dtos.stream().map(this::convertFromDto).collect(Collectors.toList());
    }
}



