package com.microecommerce.productsservice.dtos;

import com.microecommerce.productsservice.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private Long id;
    private String name;
    private String description;
    private TagDTO parentTag;

    public static TagDTO fromEntity(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .description(tag.getDescription())
                .parentTag(tag.getParentTag() != null ? fromEntity(tag.getParentTag()) : null)
                .build();
    }

    public static Tag toEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        tag.setDescription(tagDTO.getDescription());
        tag.setParentTag(tagDTO.getParentTag() != null ? TagDTO.toEntity(tagDTO) : null);
        return tag;
    }

    public static List<TagDTO> fromEntities(List<Tag> tags) {
        return tags.stream().map(TagDTO::fromEntity).collect(Collectors.toList());
    }

    public static List<Tag> toEntities(List<TagDTO> tagDTOs) {
        return tagDTOs.stream().map(TagDTO::toEntity).collect(Collectors.toList());
    }
}
