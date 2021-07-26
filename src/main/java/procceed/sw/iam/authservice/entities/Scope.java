package procceed.sw.iam.authservice.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "scope")
public class Scope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String scope;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
