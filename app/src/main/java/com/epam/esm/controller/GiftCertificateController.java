package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Controller for performing operations with {@link GiftCertificate} objects.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/gift-certificates")
@Validated
public class GiftCertificateController {
    private static final String GIFT_CERTIFICATE_ENTITY_CODE = "01";
    private final GiftCertificateService giftCertificateService;

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
     * @throws ServerInternalErrorException if error occurs while finding all {@link GiftCertificate} objects
     */
    @GetMapping()
    public List<GiftCertificate> findAll(@RequestParam Integer offset,
                                         @RequestParam Integer limit,
                                         @RequestParam(required = false) List<String> tagName,
                                         @RequestParam(required = false) List<String> sortField,
                                         @RequestParam(required = false) List<String> sortType,
                                         @RequestParam(required = false) List<String> searchField,
                                         @RequestParam(required = false) List<String> searchExpression) {
        try {
            if (tagName != null) {
                return giftCertificateService.findByTagName(tagName, sortField, sortType, searchField, searchExpression,
                        offset, limit);
            }
            return giftCertificateService.findAll(sortField, sortType, searchField, searchExpression, offset, limit);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link GiftCertificate} by id.
     *
     * @param id id to search by
     * @return found {@link GiftCertificate}
     * @throws ServerInternalErrorException if error occurs while finding {@link GiftCertificate} objects by id
     */
    @GetMapping("/{id}")
    public GiftCertificate findById(@PathVariable long id) {
        try {
            return giftCertificateService.findById(id).orElseThrow(
                    () -> new EntityNotFoundException(id, GIFT_CERTIFICATE_ENTITY_CODE));
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Adds {@link GiftCertificate} to repository.
     *
     * @param giftCertificate gift certificate to add
     * @return added {@link GiftCertificate} with HTTP code status
     * @throws ServerInternalErrorException if error occurs while adding {@link GiftCertificate} object
     */
    @PostMapping()
    public ResponseEntity<GiftCertificate> add(@Valid @NotNull @RequestBody GiftCertificate giftCertificate) {
        try {
            return new ResponseEntity<>(giftCertificateService.add(giftCertificate), HttpStatus.CREATED);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Updates {@link GiftCertificate} object with provided id to the supplied object. If supplied object's field
     * is null therefore it will not be updated.
     *
     * @param id    id of object to be updated
     * @param patch patch object
     * @return updated {@link GiftCertificate}
     * @throws ServerInternalErrorException if error occurs while updating {@link GiftCertificate} object
     */
    @PatchMapping("/{id}")
    public GiftCertificate update(@PathVariable long id,
                                  @Valid @NotNull @RequestBody GiftCertificate patch) {
        try {
            return giftCertificateService.update(id, patch);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Deletes {@link GiftCertificate} object with provided id.
     *
     * @param id object id to be deleted
     * @return deleted {@link GiftCertificate}
     * @throws ServerInternalErrorException if error occurs while deleting {@link GiftCertificate} object
     */
    @DeleteMapping("/{id}")
    public GiftCertificate delete(@PathVariable long id) {
        try {
            return giftCertificateService.delete(id);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }
}
