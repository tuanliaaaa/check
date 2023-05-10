package film.api.DTO;

import film.api.models.Chapter;
import film.api.models.Film;
import film.api.repository.ChapterRepository;
import film.api.service.ChapterService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FilmChaptersDTO {

    private Long Id;


    private String FilmName;


    private String FilmDescription;


    private String BannerFilmName;


    private int FilmBollen;


    private String TrailerFilm;


    private String FilmImage;
    private List<ChapterDTO> chapters;
    public FilmChaptersDTO(Film film){
        this.Id=film.getId();
        this.FilmName=film.getFilmName();
        this.BannerFilmName=film.getBannerFilmName();
        this.FilmBollen=film.getFilmBollen();
        this.FilmImage=film.getFilmImage();
        this.FilmDescription=film.getFilmDescription();
        this.TrailerFilm =film.getTrailerFilm();

    }
}
