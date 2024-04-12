package com.microecommerce.productsservice.models;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface IGetId {
    Long getId();

    static <T extends IGetId> List<Long> getIds(List<T> items) {
        return items.stream().map(IGetId::getId).collect(Collectors.toList());
    }

    static <T extends IGetId> Set<Long> getSetIds(List<T> items) {
        return items.stream().map(IGetId::getId).collect(Collectors.toSet());
    }

    static <T extends IGetId> boolean containsId(List<T> items, Long id) {
        return items.stream().anyMatch(i -> i.getId().equals(id));
    }

    static <T extends IGetId> T getFirstMatch(List<T> items, Long id) {
        for (T item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
