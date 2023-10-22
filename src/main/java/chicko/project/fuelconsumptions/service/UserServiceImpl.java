package chicko.project.fuelconsumptions.service;

import chicko.project.fuelconsumptions.dao.IFuelConsumptionDAO;
import chicko.project.fuelconsumptions.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private IFuelConsumptionDAO dao;

    public UserServiceImpl(IFuelConsumptionDAO dao) {
        this.dao = dao;
    }

    @Override
    public User findUserByUsername(String username) {
        return dao.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dao.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),  // User's username
                user.getPassword(),   // User's password
                authorities          // User's authorities (roles)
        );

        return userDetails;
    }
}
