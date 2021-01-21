package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.dao.QueryCustomizer;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
@Validated
public class GiftCertificateController {
    private static final Logger log = Logger.getLogger(GiftCertificateController.class);
    private static final String GIFT_CERTIFICATE_ENTITY_CODE = "01";
    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

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

    @GetMapping("/{id}")
    public GiftCertificate findById(@PathVariable long id) {
        try {
            return giftCertificateService.findById(id).orElseThrow(() -> new EntityNotFoundException(id, GIFT_CERTIFICATE_ENTITY_CODE));
        } catch (ServiceException e) {
            log.error("Error while finding gift certificate by id", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    @PostMapping("/add")
    public void add(@Valid @NotNull @RequestBody GiftCertificate giftCertificate) {
        try {
            giftCertificateService.add(giftCertificate);
        } catch (ServiceException e) {
            log.error("Error while adding gift certificate", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable long id, @Valid @NotNull @RequestBody GiftCertificate patch) {
        try {
            giftCertificateService.update(id, patch);
        } catch (ServiceException e) {
            log.error("Error while updating gift certificate", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            giftCertificateService.delete(id);
        } catch (ServiceException e) {
            log.error("Error while deleting gift certificate", e);
            throw new ServerInternalErrorException(e.getLocalizedMessage(), GIFT_CERTIFICATE_ENTITY_CODE);
        }
    }

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
