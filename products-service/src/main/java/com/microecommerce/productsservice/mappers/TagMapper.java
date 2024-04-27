package com.microecommerce.productsservice.mappers;

import com.microecommerce.dtoslibrary.products_service.TagDTO;
import com.microecommerce.productsservice.models.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class TagMapper {
    public static TagDTO fromEntity(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .description(tag.getDescription())
//                .parentTag(tag.getParentTag() != null ? fromEntity(tag.getParentTag()) : null)
                .build();
    }

    public static Tag toEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        tag.setDescription(tagDTO.getDescription());
//        tag.setParentTag(tagDTO.getParentTag() != null ? TagDTO.toEntity(tagDTO) : null);
        return tag;
    }

    public static List<TagDTO> fromEntities(List<Tag> tags) {
        return tags.stream().map(TagMapper::fromEntity).collect(Collectors.toList());
    }

    public static List<Tag> toEntities(List<TagDTO> tagDTOs) {
        return tagDTOs.stream().map(TagMapper::toEntity).collect(Collectors.toList());
    }
}
