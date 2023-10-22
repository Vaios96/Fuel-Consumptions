package chicko.project.fuelconsumptions.service;

import chicko.project.fuelconsumptions.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    public User findUserByUsername(String username);
}
