package film.api.DTO;

import film.api.models.Film;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class FilmDTO {
    private Long Id;


    private String FilmName;


    private String FilmDescription;


    private String BannerFilmName;


    private int FilmBollen;


    private String TrailerFilm;


    private String FilmImage;
    public FilmDTO(Film film){
        this.Id=film.getId();
        this.FilmName=film.getFilmName();
        this.BannerFilmName=film.getBannerFilmName();
        this.FilmBollen=film.getFilmBollen();
        this.FilmImage=film.getFilmImage();
        this.FilmDescription=film.getFilmDescription();
        this.TrailerFilm =film.getTrailerFilm();
    }
}
