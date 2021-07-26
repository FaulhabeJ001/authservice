package procceed.sw.iam.authservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_grant_type")
public class ClientGrantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String grantType;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
