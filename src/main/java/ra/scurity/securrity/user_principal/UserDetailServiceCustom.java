package ra.scurity.securrity.user_principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.scurity.model.entity.Users;
import ra.scurity.repository.IUserRepository;

@Service
public class UserDetailServiceCustom implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(useremail).orElseThrow(() -> new UsernameNotFoundException("username not found"));

        return UserPrincipal.builder()
                .id(users.getId())
                .fullName(users.getFullName())
                .email(users.getEmail())
                .password(users.getPassword())
                .status(users.isStatus())
                .authorities(
                        users.getRoles().stream().map(roles ->
                                new SimpleGrantedAuthority(roles.getRoleName().name())).toList()
                )

                .build();
    }
}
