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

/**
 * Controller for performing operations with {@link Tag} objects.
 */
@RestController
@RequestMapping("/tags")
public class TagController {
    private static final Logger log = Logger.getLogger(TagController.class);
    private static final String TAG_ENTITY_CODE = "02";
    private final TagService tagService;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagService the tag service
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Finds all {@link Tag} objects. There is ability to provide {@code MultiValueMap<String, String>}
     * to sort or search objects (for more information see {@link QueryCustomizer}).
     *
     * @param parameters the parameters
     * @return list of {@link Tag} objects
     * @throws ServerInternalErrorException if error occurs while finding all {@link Tag} objects
     */
    @GetMapping()
    public List<Tag> findAll(@RequestParam(required = false) MultiValueMap<String, String> parameters) {
        QueryCustomizer queryCustomizer = new QueryCustomizer(parameters);
        try {
            return tagService.findAll(queryCustomizer);
        } catch (ServiceException e) {
            log.error("Error while finding all tags", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link Tag} by id.
     *
     * @param id id to search by
     * @return found tag
     * @throws ServerInternalErrorException if error occurs while finding {@link Tag} objects by id
     */
    @GetMapping("/{id}")
    public Tag findById(@PathVariable long id) {
        try {
            return tagService.findById(id).orElseThrow(() -> new EntityNotFoundException(id, TAG_ENTITY_CODE));
        } catch (ServiceException e) {
            log.error("Error while finding tag by id", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Adds {@link Tag} to repository.
     *
     * @param tag gift certificate to add
     * @throws ServerInternalErrorException if error occurs while adding {@link Tag} object
     */
    @PostMapping("/add")
    public void add(@Valid @NotNull @RequestBody Tag tag) {
        try {
            tagService.add(tag);
        } catch (ServiceException e) {
            log.error("Error while adding tag", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Deletes {@link Tag} object with provided id.
     *
     * @param id object id to be deleted
     * @throws ServerInternalErrorException if error occurs while deleting {@link Tag} object
     */
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
