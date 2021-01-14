package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final static Logger log = Logger.getLogger(TagController.class);
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    public List<Tag> findAll() {
        try {
            return tagService.findAll();
        } catch (ServiceException e) {
            log.error("Error while finding all tags", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public Tag findById(@PathVariable long id) {
        try {
            return tagService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (ServiceException e) {
            log.error("Error while finding tag by id", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public void add(@RequestBody Tag tag) {
        try {
            tagService.add(tag);
        } catch (ServiceException e) {
            log.error("Error while adding tag", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            tagService.delete(id);
        } catch (ServiceException e) {
            log.error("Error while deleting tag", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
