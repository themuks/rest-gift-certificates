package com.epam.esm.controller;

import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.controller.exception.ServerInternalErrorException;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for performing operations with {@link com.epam.esm.entity.User} objects.
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
    public List<User> findAll(@RequestParam Integer offset,
                              @RequestParam Integer limit,
                              @RequestParam(required = false) List<String> sortField,
                              @RequestParam(required = false) List<String> sortType,
                              @RequestParam(required = false) List<String> searchField,
                              @RequestParam(required = false) List<String> searchExpression) {
        try {
            return userService.findAll(sortField, sortType, searchField, searchExpression, offset, limit);
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
    public User findById(@PathVariable long id) {
        try {
            return userService.findById(id).orElseThrow(
                    () -> new EntityNotFoundException(id, USER_ENTITY_CODE));
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
    @GetMapping("/{id}/orders")
    public List<Order> findOrdersOfUser(@PathVariable long id,
                                        @RequestParam Integer offset,
                                        @RequestParam Integer limit) {
        try {
            return userService.findOrdersOfUser(id, offset, limit);
        } catch (ServiceException e) {
            throw new ServerInternalErrorException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }
}
