package com.epam.esm.controller;

import com.epam.esm.controller.exception.ControllerException;
import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateDto;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller for performing operations with {@link GiftCertificate} objects.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/gift-certificates")
@Validated
public class GiftCertificateController {
    private static final String GIFT_CERTIFICATE_ENTITY_CODE = "01";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private final GiftCertificateService giftCertificateService;
    private final ModelMapper modelMapper;

    /**
     * Finds all {@link GiftCertificate} objects. There is ability to provide search field names with search
     * expressions and sort field names with sort type.
     *
     * @param offset           count of records to skip
     * @param limit            maximum count of records to return
     * @param tagName          the tag name
     * @param sortField        the sort field
     * @param sortType         the sort type
     * @param searchField      the search field
     * @param searchExpression the search expression
     * @return list of {@link GiftCertificate} objects
     * @throws ControllerException if error occurs while finding all {@link GiftCertificate} objects
     */
    @GetMapping()
    public CollectionModel<GiftCertificateDto> findAll(@RequestParam Integer offset,
                                                       @RequestParam Integer limit,
                                                       @RequestParam(required = false) List<String> tagName,
                                                       @RequestParam(required = false) List<String> sortField,
                                                       @RequestParam(required = false) List<String> sortType,
                                                       @RequestParam(required = false) List<String> searchField,
                                                       @RequestParam(required = false) List<String> searchExpression) {
        try {
            if (tagName != null) {
                Link link = linkTo(methodOn(GiftCertificateController.class)
                        .findAll(offset, limit, tagName, sortField, sortType, searchField, searchExpression))
                        .withSelfRel();
                List<GiftCertificateDto> giftCertificates = giftCertificateService.findByTagName(tagName,
                        sortField, sortType, searchField, searchExpression, offset, limit)
                        .stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList());
                for (GiftCertificateDto giftCertificateDto : giftCertificates) {
                    Long id = giftCertificateDto.getId();
                    Link self = linkTo(methodOn(GiftCertificateController.class).findById(id)).withSelfRel();
                    Link update = linkTo(methodOn(GiftCertificateController.class).update(id, null)).withRel(
                            UPDATE);
                    Link delete = linkTo(methodOn(GiftCertificateController.class).delete(id)).withRel(DELETE);
                    giftCertificateDto.add(self, update, delete);
                }
                return CollectionModel.of(giftCertificates, link);
            }
            Link link = linkTo(methodOn(GiftCertificateController.class)
                    .findAll(offset, limit, null, sortField, sortType, searchField, searchExpression))
                    .withSelfRel();
            List<GiftCertificateDto> giftCertificates = giftCertificateService.findAll(sortField, sortType, searchField,
                    searchExpression, offset, limit)
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            for (GiftCertificateDto giftCertificateDto : giftCertificates) {
                Long id = giftCertificateDto.getId();
                Link self = linkTo(methodOn(GiftCertificateController.class).findById(id)).withSelfRel();
                Link update = linkTo(methodOn(GiftCertificateController.class).update(id, null)).withRel(UPDATE);
                Link delete = linkTo(methodOn(GiftCertificateController.class).delete(id)).withRel(DELETE);
                giftCertificateDto.add(self, update, delete);
            }
            return CollectionModel.of(giftCertificates, link);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link GiftCertificate} by id.
     *
     * @param id id to search by
     * @return found {@link GiftCertificate}
     * @throws ControllerException if error occurs while finding {@link GiftCertificate} objects by id
     */
    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> findById(@PathVariable long id) {
        Link self = linkTo(methodOn(GiftCertificateController.class).findById(id)).withSelfRel();
        Link update = linkTo(methodOn(GiftCertificateController.class).update(id, null)).withRel(UPDATE);
        Link delete = linkTo(methodOn(GiftCertificateController.class).delete(id)).withRel(DELETE);
        try {
            GiftCertificate giftCertificate = giftCertificateService.findById(id).orElseThrow(
                    () -> new EntityNotFoundException(id, GIFT_CERTIFICATE_ENTITY_CODE));
            return EntityModel.of(giftCertificate, self, update, delete);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Adds {@link GiftCertificate} to repository.
     *
     * @param giftCertificate gift certificate to add
     * @return added {@link GiftCertificate} with HTTP code status
     * @throws ControllerException if error occurs while adding {@link GiftCertificate} object
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public EntityModel<GiftCertificate> add(@Valid @NotNull @RequestBody GiftCertificate giftCertificate) {
        Link self = linkTo(methodOn(GiftCertificateController.class).add(giftCertificate)).withSelfRel();
        try {
            GiftCertificate addedEntity = giftCertificateService.add(giftCertificate);
            Link update = linkTo(methodOn(GiftCertificateController.class).update(giftCertificate.getId(), null))
                    .withRel(UPDATE);
            Link delete = linkTo(methodOn(GiftCertificateController.class).delete(giftCertificate.getId()))
                    .withRel(DELETE);
            return EntityModel.of(addedEntity, self, update, delete);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Updates {@link GiftCertificate} object with provided id to the supplied object. If supplied object's field
     * is null therefore it will not be updated.
     *
     * @param id    id of object to be updated
     * @param patch patch object
     * @return updated {@link GiftCertificate}
     * @throws ControllerException if error occurs while updating {@link GiftCertificate} object
     */
    @PatchMapping("/{id}")
    public EntityModel<GiftCertificate> update(@PathVariable long id,
                                               @Valid @NotNull @RequestBody GiftCertificate patch) {
        Link self = linkTo(methodOn(GiftCertificateController.class).update(id, patch)).withSelfRel();
        Link delete = linkTo(methodOn(GiftCertificateController.class).delete(id)).withRel(DELETE);
        try {
            GiftCertificate updatedGiftCertificate = giftCertificateService.update(id, patch);
            return EntityModel.of(updatedGiftCertificate, self, delete);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Deletes {@link GiftCertificate} object with provided id.
     *
     * @param id object id to be deleted
     * @return deleted {@link GiftCertificate}
     * @throws ControllerException if error occurs while deleting {@link GiftCertificate} object
     */
    @DeleteMapping("/{id}")
    public EntityModel<GiftCertificate> delete(@PathVariable long id) {
        Link self = linkTo(methodOn(GiftCertificateController.class).delete(id)).withSelfRel();
        Link update = linkTo(methodOn(GiftCertificateController.class).update(id, null)).withRel(UPDATE);
        try {
            GiftCertificate deletedGiftCertificate = giftCertificateService.delete(id);
            return EntityModel.of(deletedGiftCertificate, self, update);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    private GiftCertificateDto convertToDto(GiftCertificate giftCertificate) {
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }
}
