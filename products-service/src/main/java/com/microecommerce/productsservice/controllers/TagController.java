package com.microecommerce.productsservice.controllers;

import com.microecommerce.productsservice.services.interfaces.ITagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }
//
//    @GetMapping
//    public List<Tag> getAllTags() {
//        return tagService.getAll();
//    }

//    @GetMapping("/{id}")
//    public Tag getTagById(@PathVariable Long id) {
//        return tagService.getById(id);
//    }

//    @PostMapping
//    // TODO: Internally manage Exception
//    public Tag addTag(@RequestBody Tag tag) throws Exception {
//        return tagService.create(tag);
//    }
//
//    @PostMapping("/batch")
//    // TODO: Internally manage Exception
//    public List<Tag> addTags(@RequestBody List<Tag> tags) throws Exception {
//        return tagService.createBatch(tags);
//    }
//
//    @PutMapping("/{id}")
//    public Tag updateTag(@PathVariable Long id, @RequestBody Tag tag) {
//        tag.setId(id);
//        return tagService.update(tag);
//    }
//
//    @PutMapping("/batch")
//    public List<Tag> updateTags(@RequestBody List<Tag> tags) {
//        return tagService.updateBatch(tags);
//    }

//    @DeleteMapping("/{id}")
//    public void deleteTag(@PathVariable Long id) {
//        tagService.deleteById(id);
//    }
}
