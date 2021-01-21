package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.util.QueryCustomizer;
import org.apache.log4j.Logger;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Controller for performing operations with {@link GiftCertificate} objects.
 */
@RestController
@RequestMapping("/gift-certificates")
@Validated
public class GiftCertificateController {
    private static final Logger log = Logger.getLogger(GiftCertificateController.class);
    private static final String GIFT_CERTIFICATE_ENTITY_CODE = "01";
    private final GiftCertificateService giftCertificateService;

    /**
     * Instantiates a new Gift certificate controller.
     *
     * @param giftCertificateService the gift certificate service
     */
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Finds all {@link GiftCertificate} objects. There is ability to provide {@code MultiValueMap<String, String>}
     * to sort or search objects (for more information see {@link QueryCustomizer}).
     *
     * @param parameters the parameters
     * @return list of {@link GiftCertificate} objects
     * @throws ServerInternalErrorException if error occurs while finding all {@link GiftCertificate} objects
     */
    @GetMapping()
    public List<GiftCertificate> findAll(@RequestParam(required = false) MultiValueMap<String, String> parameters) {
        QueryCustomizer queryCustomizer = new QueryCustomizer(parameters);
        try {
            return giftCertificateService.findAll(queryCustomizer);
        } catch (ServiceException e) {
            log.error("Error while finding all gift certificates", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link GiftCertificate} by id.
     *
     * @param id id to search by
     * @return found gift certificate
     * @throws ServerInternalErrorException if error occurs while finding {@link GiftCertificate} objects by id
     */
    @GetMapping("/{id}")
    public GiftCertificate findById(@PathVariable long id) {
        try {
            return giftCertificateService.findById(id).orElseThrow(() -> new EntityNotFoundException(id, GIFT_CERTIFICATE_ENTITY_CODE));
        } catch (ServiceException e) {
            log.error("Error while finding gift certificate by id", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Adds {@link GiftCertificate} to repository.
     *
     * @param giftCertificate gift certificate to add
     * @throws ServerInternalErrorException if error occurs while adding {@link GiftCertificate} object
     */
    @PostMapping("/add")
    public void add(@Valid @NotNull @RequestBody GiftCertificate giftCertificate) {
        try {
            giftCertificateService.add(giftCertificate);
        } catch (ServiceException e) {
            log.error("Error while adding gift certificate", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Updates {@link GiftCertificate} object with provided id to the supplied object. If supplied object's field
     * is null therefore it will not be updated.
     *
     * @param id    id of object to be updated
     * @param patch patch object
     * @throws ServerInternalErrorException if error occurs while updating {@link GiftCertificate} object
     */
    @PatchMapping("/{id}")
    public void update(@PathVariable long id, @Valid @NotNull @RequestBody GiftCertificate patch) {
        try {
            giftCertificateService.update(id, patch);
        } catch (ServiceException e) {
            log.error("Error while updating gift certificate", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Deletes {@link GiftCertificate} object with provided id.
     *
     * @param id object id to be deleted
     * @throws ServerInternalErrorException if error occurs while deleting {@link GiftCertificate} object
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            giftCertificateService.delete(id);
        } catch (ServiceException e) {
            log.error("Error while deleting gift certificate", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link GiftCertificate} object by tag name.
     *
     * @param tagName    tag name to search by
     * @param parameters the parameters
     * @return list of found {@link GiftCertificate} objects
     * @throws ServerInternalErrorException if error occurs while finding {@link GiftCertificate} object by tag name
     */
    @GetMapping("/tag/{tagName}")
    public List<GiftCertificate> findByTagName(@PathVariable String tagName,
                                               @RequestParam(required = false) MultiValueMap<String, String> parameters) {
        QueryCustomizer queryCustomizer = new QueryCustomizer(parameters);
        try {
            return giftCertificateService.findByTagName(tagName, queryCustomizer);
        } catch (ServiceException e) {
            log.error("Error while finding gift certificate by tag name", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }
}
