package com.epam.esm.controller;

import com.epam.esm.controller.exception.ControllerException;
import com.epam.esm.controller.exception.EntityNotFoundException;
import com.epam.esm.entity.*;
import com.epam.esm.model.service.ServiceException;
import com.epam.esm.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller for performing operations with {@link User} objects.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
@Validated
public class UserControllerV1 {
    private static final String USER_ENTITY_CODE = "03";
    private static final String ORDER_ENTITY_CODE = "04";
    private static final String USER_NOT_CREATED = "User with such email is already registered";
    private final UserService userService;
    private final ModelMapper modelMapper;

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
     * @throws ControllerException if error occurs while finding all {@link User} objects
     */
    @GetMapping()
    @PreAuthorize("hasAuthority('all:read')")
    public CollectionModel<UserDto> findAll(@RequestParam Integer offset,
                                            @RequestParam Integer limit,
                                            @RequestParam(required = false) List<String> sortField,
                                            @RequestParam(required = false) List<String> sortType,
                                            @RequestParam(required = false) List<String> searchField,
                                            @RequestParam(required = false) List<String> searchExpression) {
        Link link = linkTo(methodOn(UserControllerV1.class)
                .findAll(offset, limit, sortField, sortType, searchField, searchExpression))
                .withSelfRel();
        try {
            List<UserDto> users = userService.findAll(sortField, sortType, searchField, searchExpression, offset, limit)
                    .stream()
                    .map(this::convertUserToDto)
                    .collect(Collectors.toList());
            for (UserDto userDto : users) {
                Long id = userDto.getId();
                Link self = linkTo(methodOn(UserControllerV1.class).findById(id)).withSelfRel();
                userDto.add(self);
            }
            return CollectionModel.of(users, link);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link User} by id.
     *
     * @param id id to search by
     * @return found {@link User}
     * @throws ControllerException if error occurs while finding {@link User} objects by id
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('all:read')")
    public EntityModel<User> findById(@PathVariable long id) {
        Link self = linkTo(methodOn(UserControllerV1.class).findById(id)).withSelfRel();
        try {
            User user = userService.findById(id).orElseThrow(() -> new EntityNotFoundException(id, USER_ENTITY_CODE));
            return EntityModel.of(user, self);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }

    /**
     * Finds {@link User} orders.
     *
     * @param id     to search by
     * @param offset count of records to skip
     * @param limit  maximum count of records to return
     * @return found {@link User} orders
     * @throws ControllerException if error occurs while finding {@link User} orders
     */
    @GetMapping("/{id}/orders")
    @PreAuthorize("hasAuthority('all:read')")
    public CollectionModel<Order> findOrdersOfUser(@PathVariable long id,
                                                   @RequestParam Integer offset,
                                                   @RequestParam Integer limit) {
        Link self = linkTo(methodOn(UserControllerV1.class).findOrdersOfUser(id, offset, limit)).withSelfRel();
        try {
            List<Order> ordersOfUser = userService.findOrdersOfUser(id, offset, limit);
            return CollectionModel.of(ordersOfUser, self);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('order:create') or hasAuthority('all:write')")
    public EntityModel<OrderDto> makeOrderOnGiftCertificate(@PathVariable long userId,
                                                            @Valid @NotNull @RequestBody GiftCertificate giftCertificate) {
        Link self = linkTo(methodOn(UserControllerV1.class).makeOrderOnGiftCertificate(userId, giftCertificate))
                .withSelfRel();
        try {
            OrderDto orderDto =
                    convertOrderToDto(userService.makeOrderOnGiftCertificate(userId, giftCertificate.getId()));
            return EntityModel.of(orderDto, self);
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), ORDER_ENTITY_CODE);
        }
    }

    @PostMapping()
    @PermitAll
    public ResponseEntity<String> register(@RequestBody RegistrationRequestDto registrationRequest) {
        try {
            if (!userService.register(registrationRequest.getEmail(),
                    registrationRequest.getPassword(),
                    registrationRequest.getName(),
                    registrationRequest.getSurname())) {
                return ResponseEntity.status(HttpStatus.CREATED).body(USER_NOT_CREATED);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ServiceException e) {
            throw new ControllerException(e.getLocalizedMessage(), USER_ENTITY_CODE);
        }
    }

    private UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private OrderDto convertOrderToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
