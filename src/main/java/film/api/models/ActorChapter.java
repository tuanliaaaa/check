package film.api.models;
import javax.validation.constraints.NotNull;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor Actor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter Chapter;
}
