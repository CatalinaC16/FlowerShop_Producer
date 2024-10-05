package com.example.flowerShop.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Mapper<Source, Target, Target2> {

    Target2 convertToDTO(Source source);

    Source convertToEntity(Target target);

    default List<Target2> convertListToDTO(List<Source> source) {
        return source.stream()
                .map(this::convertToDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    default List<Source> convertListToEntity(List<Target> source) {
        return source.stream()
                .map(this::convertToEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
