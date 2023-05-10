package film.api.configuration.security.controller;

import film.api.DTO.UserDTO;
import film.api.DTO.UserSignupDTO;
import film.api.configuration.security.*;
import film.api.models.User;
import film.api.service.ChapterService;
import film.api.service.HistoryService;
import film.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import film.api.models.Role;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.*;

@RestController
public class UserController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired

    private JwtUserDetailsService userDetailsServicea;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private UserService userService;
    @RequestMapping(value = "user", method = RequestMethod.GET)

    @Secured("ROLE_ADMIN")
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserSignupDTO user) throws Exception {
        return new ResponseEntity<>(userDetailsServicea.save(user),HttpStatus.CREATED);
    }
    @GetMapping("/ApiV1/UserByLogin")
    public ResponseEntity<?> getUserByIdLogin(HttpServletRequest request){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userID = historyService.getUserID(username);
        User user = userService.findById(userID);
        UserDTO userDTO = new UserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @PatchMapping("/ApiV1/UserByLogin")
    public ResponseEntity<?> changeFullName(HttpServletRequest request,@RequestBody UserDTO userDTO){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userID = historyService.getUserID(username);
        User user = userService.findById(userID);
        user.setFullname(userDTO.getFullName());
        User userUpdate = userService.updateUser(user);
        UserDTO userUpdateDTO = new UserDTO(userUpdate);
        return new ResponseEntity<>(userUpdateDTO, HttpStatus.OK);
    }


}