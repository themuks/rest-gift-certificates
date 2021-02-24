package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.Tag;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Controller for performing operations with {@link Tag} objects.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {
    private static final String TAG_ENTITY_CODE = "02";
    private final TagService tagService;

    /**
     * Finds all {@link Tag} objects. There is ability to provide search field names with search
     * expressions and sort field names with sort type.
     *
     * @param offset           count of records to skip
     * @param limit            maximum count of records to return
     * @param sortField        the sort field
     * @param sortType         the sort type
     * @param searchField      the search field
     * @param searchExpression the search expression
     * @return list of {@link Tag} objects
     * @throws ServerInternalErrorException if error occurs while finding all {@link Tag} objects
     */
    @GetMapping()
    public List<Tag> findAll(@RequestParam Integer offset,
                             @RequestParam Integer limit,
                             @RequestParam(required = false) List<String> sortField,
                             @RequestParam(required = false) List<String> sortType,
                             @RequestParam(required = false) List<String> searchField,
                             @RequestParam(required = false) List<String> searchExpression) {
        try {
            return tagService.findAll(sortField, sortType, searchField, searchExpression, offset, limit);
        } catch (ServiceException e) {
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
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Adds {@link Tag} to repository.
     *
     * @param tag gift certificate to add
     * @return added {@link Tag}
     * @throws ServerInternalErrorException if error occurs while adding {@link Tag} object
     */
    @PostMapping()
    public ResponseEntity<Tag> add(@Valid @NotNull @RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(tagService.add(tag), HttpStatus.CREATED);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Deletes {@link Tag} object with provided id.
     *
     * @param id object id to be deleted
     * @return deleted {@link Tag}
     * @throws ServerInternalErrorException if error occurs while deleting {@link Tag} object
     */
    @DeleteMapping("/{id}")
    public Tag delete(@PathVariable long id) {
        try {
            return tagService.delete(id);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }
}
