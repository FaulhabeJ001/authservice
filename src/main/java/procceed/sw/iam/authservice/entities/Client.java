package procceed.sw.iam.authservice.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String client;
    private String secret;
    private String redirectUri;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<ClientGrantType> clientGrantType;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Scope> scopes;

    @Override
    public int hashCode() {
        return Objects.hashCode(client);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Client other = (Client) obj;
        return Objects.equals(client, other.getClient());
    }
}
