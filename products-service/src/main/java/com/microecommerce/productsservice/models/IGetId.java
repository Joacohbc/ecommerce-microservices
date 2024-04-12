package com.microecommerce.productsservice.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface IGetId {
    Long getId();

    // Get a list of IDs from a list of items (can be repeated)
    static <T extends IGetId> List<Long> getIds(List<T> items) {
        return items.stream().map(IGetId::getId).collect(Collectors.toList());
    }

    // Short form of: new ArrayList<>(getSetIds(items)) to get a list (no Set) of unique IDs
    static <T extends IGetId> List<Long> getUniqueIdList(List<T> items) {
        return new ArrayList<>(getSetIds(items));
    }

    // Get a set of IDs from a list of items (unique)
    static <T extends IGetId> Set<Long> getSetIds(List<T> items) {
        return items.stream().map(IGetId::getId).collect(Collectors.toSet());
    }

    // Check if a list of items contains an ID
    static <T extends IGetId> boolean containsId(List<T> items, Long id) {
        return items.stream().anyMatch(i -> i.getId().equals(id));
    }

    // Get the first item that matches an ID
    static <T extends IGetId> T getFirstMatch(List<T> items, Long id) {
        for (T item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
