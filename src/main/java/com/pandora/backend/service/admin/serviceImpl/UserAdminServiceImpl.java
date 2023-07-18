package com.pandora.backend.service.admin.serviceImpl;

import com.pandora.backend.error.exception.InvalidArgumentException;
import com.pandora.backend.model.entity.*;
import com.pandora.backend.model.request.admin.user.UserCreationRequest;
import com.pandora.backend.model.request.admin.user.UserUpdateRequest;
import com.pandora.backend.repository.RoleRepository;
import com.pandora.backend.repository.admin.OrderAdminRepository;
import com.pandora.backend.repository.admin.OrderDetailsAdminRepository;
import com.pandora.backend.repository.admin.UserAdminRepository;
import com.pandora.backend.service.admin.UserAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class UserAdminServiceImpl implements UserAdminService {

    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final OrderAdminRepository orderAdminRepository;

    private final OrderDetailsAdminRepository orderDetailsAdminRepository;

    public UserAdminServiceImpl(UserAdminRepository userAdminRepository, PasswordEncoder passwordEncoder,
                                RoleRepository roleRepository, OrderAdminRepository orderAdminRepository,
                                OrderDetailsAdminRepository orderDetailsAdminRepository) {
        this.userAdminRepository = userAdminRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.orderAdminRepository = orderAdminRepository;
        this.orderDetailsAdminRepository = orderDetailsAdminRepository;
    }

    @Override
    public User newUser(UserCreationRequest userCreationRequest) {
        if (Boolean.TRUE.equals(userAdminRepository.existsByEmail(userCreationRequest.getEmail()))) {
            throw new InvalidArgumentException("Já existe uma conta com este e-mail");
        }

        String encryptedPassword = passwordEncoder.encode(userCreationRequest.getPassword());

        User newUser = new User();
        newUser.setEmail(userCreationRequest.getEmail());
        newUser.setFirstName(userCreationRequest.getFirstName());
        newUser.setLastName(userCreationRequest.getLastName());
        newUser.setAddress(userCreationRequest.getAddress());
        newUser.setCity(userCreationRequest.getCity());
        newUser.setState(userCreationRequest.getState());
        newUser.setZip(userCreationRequest.getZip());
        newUser.setPhone(userCreationRequest.getPhone());
        newUser.setCountry(userCreationRequest.getCountry());
        newUser.setEnabled(userCreationRequest.getEnabled());
        newUser.setSocialProvider("ADMIN");
        newUser.setPassword(encryptedPassword);

        if (userCreationRequest.getRole() != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : userCreationRequest.getRole()) {
                Role role = roleRepository.findByName(RoleType.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(role);
            }
            newUser.setRoles(roles);

            for (Role role : roles) {
                if (role.getName() == RoleType.ROLE_USER) {
                    newUser.setEmailVerified(0);
                } else {
                    newUser.setEmailVerified(1);
                }
            }
        }
        return userAdminRepository.save(newUser);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        Sort sort = Sort.by(pageable.getSort().getOrderFor("registrationDate").getDirection(), pageable.getSort().getOrderFor("registrationDate").getProperty());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return userAdminRepository.findAll(pageable);
    }



    @Override
    public User getUserById(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidArgumentException("Null id");
        }

        return userAdminRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(Long id, UserUpdateRequest userUpdate) {
        User existingUser = userAdminRepository.findById(id).orElse(null);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        existingUser.setEmail(userUpdate.getEmail());
        existingUser.setFirstName(userUpdate.getFirstName());
        existingUser.setLastName(userUpdate.getLastName());
        existingUser.setPhone(userUpdate.getPhone());
        existingUser.setAddress(userUpdate.getAddress());
        existingUser.setCity(userUpdate.getCity());
        existingUser.setState(userUpdate.getState());
        existingUser.setZip(userUpdate.getZip());
        existingUser.setCountry(userUpdate.getCountry());
        existingUser.setEmailVerified(1);
        existingUser.setEnabled(userUpdate.getEnabled());

        Set<Role> roles = updateUserRoles(id, userUpdate.getRoles());
        existingUser.setRoles(roles);

        return userAdminRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userAdminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        List<Order> orders = orderAdminRepository.findByUserId(id);

        for (Order order : orders) {
            List<OrderDetail> orderDetails = order.getOrderDetailList();
            orderDetailsAdminRepository.deleteAll(orderDetails);
        }

        orderAdminRepository.deleteAll(orders);

        userAdminRepository.delete(user);
    }



    private Set<Role> updateUserRoles(Long userId, List<String> userRole) {

        List<Integer> roleIds = new ArrayList<>();

        for (String roleName : userRole) {
            Optional<Role> role = roleRepository.findByName(RoleType.valueOf(roleName));
            if (role.isPresent()) {
                roleIds.add(role.get().getRole_id());
            }
        }

        List<Role> existingUserRoles = roleRepository.findByUserId(userId);

        existingUserRoles.clear();

        for (Integer roleId : roleIds) {
            Optional<Role> roleOptional = roleRepository.findById(roleId);
            roleOptional.ifPresent(existingUserRoles::add);
        }

        return new HashSet<>(existingUserRoles);
    }

}
