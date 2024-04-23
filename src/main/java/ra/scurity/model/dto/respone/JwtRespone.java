package ra.scurity.model.dto.respone;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtRespone {
    private String accessToken;
    private String fullName;
    private String email;
    private Boolean status;
    private Collection<? extends GrantedAuthority> roles;
}
