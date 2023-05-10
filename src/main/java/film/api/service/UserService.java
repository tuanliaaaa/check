package film.api.service;

import film.api.DTO.UserByAdminDTO;
import film.api.DTO.UserSignupDTO;
import film.api.configuration.security.UserChangePassword;
import film.api.models.Role;
import film.api.models.User;
import film.api.models.UserRole;
import film.api.repository.RoleRepository;
import film.api.repository.UserRepository;
import film.api.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;


    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public List<User> findUsersByNameContain(String name) {
        return userRepository.findUsersByNameContain(name);
    }

    public  User updateUser(User user){
        return  userRepository.save(user);
    }
    public User save(User user) {
        Role role =roleRepository.findById(2L).orElse(null);

        User userex=userRepository.findByUsername(user.getUsername());
        if(userex!=null)throw new IllegalArgumentException("User đã tồn tại");

        if(user.getUsername()==null){
            throw new IllegalArgumentException("VUi lòng nhập username");
        }
        if(user.getPassword()==null){
            throw new IllegalArgumentException("VUi lòng nhập Password");
        }
        UserRole userRole = new UserRole(null,role,user);
        User usernew=userRepository.save(user);
        userRoleRepository.save(userRole);

        return usernew;
    }

    public User ChangePassword(String username, UserChangePassword userChangePassword){
        User user=userRepository.findByUsername(username);
        user.setPassword(userChangePassword.getPasswordNew());

        return  userRepository.save(user);
    }
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User updateUser(Long id, UserByAdminDTO userPatchDTO) {
        Object s=userRepository.findById(id);
        User user = userRepository.findById(id).orElse(null);

        if(userPatchDTO.getFullname() != null) {
            user.setFullname(userPatchDTO.getFullname());
        }

        if(userPatchDTO.getUsername() != null) {
            user.setUsername(userPatchDTO.getUsername());
        }

        return userRepository.save(user);
    }

}
