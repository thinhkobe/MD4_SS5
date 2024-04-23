package ra.scurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.scurity.constants.RoleName;
import ra.scurity.model.dto.request.UserLogin;
import ra.scurity.model.dto.request.UserRegister;
import ra.scurity.model.dto.respone.JwtRespone;
import ra.scurity.model.entity.Roles;
import ra.scurity.model.entity.Users;
import ra.scurity.repository.IRoleRepository;
import ra.scurity.repository.IUserRepository;
import ra.scurity.securrity.jwt.JwtProvider;
import ra.scurity.securrity.user_principal.UserPrincipal;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service

public class UserServiceIMPL implements IUserservice {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    public boolean register(UserRegister userRegister) {
        Users users = Users.builder()
                .email(userRegister.getEmail())
                .fullName(userRegister.getFullName())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .status(true)
                .build();
        if (userRegister.getRoles() != null && !userRegister.getRoles().isEmpty()) {
            Set<Roles> rolesSet = new HashSet<>();
            userRegister.getRoles().forEach(
                    r -> {
                        switch (r) {
                            case "ADMIN":
                                rolesSet.add(iRoleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "USER":
                                rolesSet.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            default:
                                rolesSet.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                        }
                    }
            );
        }else {
            Set<Roles> roles = new HashSet<>();
            roles.add(iRoleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
            users.setRoles(roles);
        }
        userRepository.save(users);
        return false;
    }

    @Override
    public JwtRespone login(UserLogin userLogin) {
        Authentication authentication;
        try {
            authentication=authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(),userLogin.getPassword()));

        }catch (AuthenticationException e){
            throw  new RuntimeException();
        }
        UserPrincipal userPrincipal=(UserPrincipal) authentication.getPrincipal();
        if (!userPrincipal.isStatus()){
            throw new RuntimeException();
        }
        String token=jwtProvider.generrateToken(userPrincipal);
        return JwtRespone.builder()
                .accessToken(token)
                .email(userLogin.getEmail())
                .fullName(userPrincipal.getFullName())
                .roles(userPrincipal.getAuthorities())
                .status(userPrincipal.isStatus())
                .build();
    }
}
