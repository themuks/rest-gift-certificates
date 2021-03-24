package com.epam.esm.controller;

import com.epam.esm.controller.exception.ControllerException;
import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDto;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller for performing operations with {@link Tag} objects.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/tags")
public class TagControllerV1 {
    private static final String TAG_ENTITY_CODE = "02";
    private static final String DELETE = "delete";
    private final TagService tagService;
    private final ModelMapper modelMapper;

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
     * @throws ControllerException if error occurs while finding all {@link Tag} objects
     */
    @GetMapping()
    @PreAuthorize("hasAuthority('all:read')")
    public CollectionModel<TagDto> findAll(@RequestParam Integer offset,
                                           @RequestParam Integer limit,
                                           @RequestParam(required = false) List<String> sortField,
                                           @RequestParam(required = false) List<String> sortType,
                                           @RequestParam(required = false) List<String> searchField,
                                           @RequestParam(required = false) List<String> searchExpression) {
        Link link = linkTo(methodOn(TagControllerV1.class)
                .findAll(offset, limit, sortField, sortType, searchField, searchExpression))
                .withSelfRel();
        try {
            List<TagDto> tags = tagService.findAll(sortField, sortType, searchField, searchExpression, offset, limit)
                    .stream()
                    .map(this::convertTagToDto)
                    .collect(Collectors.toList());
            for (TagDto tagDto : tags) {
                Long id = tagDto.getId();
                Link self = linkTo(methodOn(TagControllerV1.class).findById(id)).withSelfRel();
                Link delete = linkTo(methodOn(TagControllerV1.class).delete(id)).withRel(DELETE);
                tagDto.add(self, delete);
            }
            return CollectionModel.of(tags, link);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link Tag} by id.
     *
     * @param id id to search by
     * @return found tag
     * @throws ControllerException if error occurs while finding {@link Tag} objects by id
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('all:read')")
    public EntityModel<Tag> findById(@PathVariable long id) {
        Link self = linkTo(methodOn(TagControllerV1.class).findById(id)).withSelfRel();
        Link delete = linkTo(methodOn(TagControllerV1.class).delete(id)).withRel(DELETE);
        try {
            Tag tag = tagService.findById(id).orElseThrow(() -> new EntityNotFoundException(id, TAG_ENTITY_CODE));
            return EntityModel.of(tag, self, delete);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Adds {@link Tag} to repository.
     *
     * @param tag gift certificate to add
     * @return added {@link Tag}
     * @throws ControllerException if error occurs while adding {@link Tag} object
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    @PreAuthorize("hasAuthority('all:write')")
    public EntityModel<Tag> add(@Valid @NotNull @RequestBody Tag tag) {
        Link self = linkTo(methodOn(TagControllerV1.class).add(tag)).withSelfRel();
        try {
            Tag addedTag = tagService.add(tag);
            Link delete = linkTo(methodOn(TagControllerV1.class).delete(tag.getId())).withRel(DELETE);
            return EntityModel.of(addedTag, self, delete);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Deletes {@link Tag} object with provided id.
     *
     * @param id object id to be deleted
     * @return deleted {@link Tag}
     * @throws ControllerException if error occurs while deleting {@link Tag} object
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('all:write')")
    public EntityModel<Tag> delete(@PathVariable long id) {
        Link self = linkTo(methodOn(TagControllerV1.class).delete(id)).withSelfRel();
        try {
            tagService.findById(id).orElseThrow(() -> new EntityNotFoundException(id, TAG_ENTITY_CODE));
            Tag deletedTag = tagService.delete(id);
            return EntityModel.of(deletedTag, self);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    /**
     * Find most used tag among users with highest order cost sum.
     *
     * @return the tag
     * @throws ControllerException if error occurs while finding most used {@link Tag}
     */
    @GetMapping("/popular")
    @PreAuthorize("hasAuthority('all:read')")
    public EntityModel<Tag> findMostUsedTag() {
        Link self = linkTo(methodOn(TagControllerV1.class).findMostUsedTag()).withSelfRel();
        try {
            Tag mostUsedTag = tagService.findMostUsedTag();
            return EntityModel.of(mostUsedTag, self);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), TAG_ENTITY_CODE);
        }
    }

    private TagDto convertTagToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }
}
