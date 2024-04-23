package ra.scurity.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ra.scurity.constants.RoleName;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
