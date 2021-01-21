package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.util.QueryCustomizer;
import org.apache.log4j.Logger;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final static Logger log = Logger.getLogger(TagController.class);
    private static final String TAG_ENTITY_CODE = "02";
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    public List<Tag> findAll(@RequestParam(required = false) MultiValueMap<String, String> sort) {
        QueryCustomizer queryCustomizer = new QueryCustomizer(sort);
        try {
            return tagService.findAll(queryCustomizer);
        } catch (ServiceException e) {
            log.error("Error while finding all tags", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    @GetMapping("/{id}")
    public Tag findById(@PathVariable long id) {
        try {
            return tagService.findById(id).orElseThrow(() -> new EntityNotFoundException(id, TAG_ENTITY_CODE));
        } catch (ServiceException e) {
            log.error("Error while finding tag by id", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    @PostMapping("/add")
    public void add(@Valid @NotNull @RequestBody Tag tag) {
        try {
            tagService.add(tag);
        } catch (ServiceException e) {
            log.error("Error while adding tag", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            tagService.delete(id);
        } catch (ServiceException e) {
            log.error("Error while deleting tag", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }
}
