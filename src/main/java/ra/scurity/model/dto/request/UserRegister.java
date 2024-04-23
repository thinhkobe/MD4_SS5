package ra.scurity.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRegister {
    @NotBlank
    private String  fullName;
    @NotBlank
    private String  email;
    @NotBlank
    private String  password;
    private Set<String> roles;
}
