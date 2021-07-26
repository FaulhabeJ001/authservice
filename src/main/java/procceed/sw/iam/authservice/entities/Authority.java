package procceed.sw.iam.authservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_generator")
    @SequenceGenerator(name="authority_generator", sequenceName = "authority_id_seq", allocationSize=1)
    private Long id;
    private String authority;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = true)
    @JsonIgnore
    private MyUser user;
}
