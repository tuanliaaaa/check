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
public class Film {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    private String FilmName;

    @NotNull
    private String FilmDescription;

    @NotNull
    private String BannerFilmName;

    @NotNull
    private int FilmBollen;

    @NotNull
    private String TrailerFilm;

    @NotNull
    private String FilmImage;
}
