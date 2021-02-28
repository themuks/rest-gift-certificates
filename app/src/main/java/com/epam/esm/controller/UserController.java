package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller for performing operations with {@link User} objects.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private static final String USER_ENTITY_CODE = "03";
    private final UserService userService;

    /**
     * Finds all {@link User} objects. There is ability to provide search field names with search
     * expressions and sort field names with sort type
     *
     * @param offset           count of records to skip
     * @param limit            maximum count of records to return
     * @param sortField        the sort field
     * @param sortType         the sort type
     * @param searchField      the search field
     * @param searchExpression the search expression
     * @return list of {@link User} objects
     * @throws ServerInternalErrorException if error occurs while finding all {@link User} objects
     */
    @GetMapping()
    public CollectionModel<User> findAll(@RequestParam Integer offset,
                                         @RequestParam Integer limit,
                                         @RequestParam(required = false) List<String> sortField,
                                         @RequestParam(required = false) List<String> sortType,
                                         @RequestParam(required = false) List<String> searchField,
                                         @RequestParam(required = false) List<String> searchExpression) {
        Link link = linkTo(methodOn(UserController.class)
                .findAll(offset, limit, sortField, sortType, searchField, searchExpression))
                .withSelfRel();
        try {
            List<User> users = userService.findAll(sortField, sortType, searchField, searchExpression, offset, limit);
            for (User user : users) {
                Long id = user.getId();
                Link self = linkTo(methodOn(UserController.class).findById(id)).withSelfRel();
                user.add(self);
            }
            return CollectionModel.of(users, link);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link User} by id.
     *
     * @param id id to search by
     * @return found {@link User}
     * @throws ServerInternalErrorException if error occurs while finding {@link User} objects by id
     */
    @GetMapping("/{id}")
    public EntityModel<User> findById(@PathVariable long id) {
        Link self = linkTo(methodOn(UserController.class).findById(id)).withSelfRel();
        try {
            User user = userService.findById(id).orElseThrow(() -> new EntityNotFoundException(id, USER_ENTITY_CODE));
            user.add(self);
            return EntityModel.of(user);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link User} orders.
     *
     * @param id     to search by
     * @param offset count of records to skip
     * @param limit  maximum count of records to return
     * @return found {@link User} orders
     * @throws ServerInternalErrorException if error occurs while finding {@link User} orders
     */
    @GetMapping("/{id}/orders")
    public CollectionModel<Order> findOrdersOfUser(@PathVariable long id,
                                                   @RequestParam Integer offset,
                                                   @RequestParam Integer limit) {
        Link self = linkTo(methodOn(UserController.class).findOrdersOfUser(id, offset, limit)).withSelfRel();
        try {
            List<Order> ordersOfUser = userService.findOrdersOfUser(id, offset, limit);
            return CollectionModel.of(ordersOfUser, self);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }
}
