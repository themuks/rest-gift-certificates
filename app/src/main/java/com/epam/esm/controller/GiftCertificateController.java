package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    private static final Logger log = Logger.getLogger(GiftCertificateController.class);
    private final GiftCertificateService giftCertificateService;

    private Validator validator;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping()
    public List<GiftCertificate> findAll() {
        try {
            return giftCertificateService.findAll();
        } catch (ServiceException e) {
            log.error("Error while finding all gift certificates", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public GiftCertificate findById(@PathVariable long id) {
        try {
            return giftCertificateService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            log.error("Error while finding gift certificate by id", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public void add(@RequestBody GiftCertificate giftCertificate) {
        try {
            giftCertificateService.add(giftCertificate);
        } catch (ServiceException e) {
            log.error("Error while adding gift certificate", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody GiftCertificate giftCertificate) {
        try {
            giftCertificateService.update(id, giftCertificate);
        } catch (ServiceException e) {
            log.error("Error while updating gift certificate", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            giftCertificateService.delete(id);
        } catch (ServiceException e) {
            log.error("Error while deleting gift certificate", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
