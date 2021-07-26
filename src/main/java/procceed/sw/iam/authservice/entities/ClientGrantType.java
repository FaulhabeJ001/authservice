package procceed.sw.iam.authservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "client_grant_type")
public class ClientGrantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String grantType;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
