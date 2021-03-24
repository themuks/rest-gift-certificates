package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.dao.exception.DaoException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userDao.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
            return SecurityUser.fromUser(user);
        } catch (DaoException e) {
            throw new UsernameNotFoundException(e.getLocalizedMessage(), e);
        }
    }
}
