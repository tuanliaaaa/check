package film.api.controller;

import film.api.DTO.ChapterActorsDTO;
import film.api.DTO.ChapterHotDTO;
import film.api.DTO.ChapterRequestDTO;
import film.api.DTO.FilmRequestDTO;
import film.api.models.Chapter;
import film.api.models.Film;
import film.api.service.ActorService;
import film.api.service.CategoryService;
import film.api.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ApiV1")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ActorService actorService;

    @PostMapping("/ChapterByFilmID/{filmID}")
    public ResponseEntity<?> PatchFilm(@PathVariable("filmID") Long filmID,@ModelAttribute ChapterRequestDTO chapterPost){
        Chapter chapter =chapterService.addChapter(filmID,chapterPost);
        return new ResponseEntity<>(chapter, HttpStatus.CREATED);
    }
    @GetMapping("/ChapterByID/{chapterID}")
    public ResponseEntity<?> getChapterByID(@PathVariable("chapterID") Long chapterID){
        Chapter chapter =chapterService.findByID(chapterID);
        ChapterActorsDTO chapterActorsDTO =new ChapterActorsDTO();
        chapterActorsDTO.loadData(chapter,actorService);
        return new ResponseEntity<>(chapterActorsDTO, HttpStatus.OK);
    }
    @PatchMapping("/ChapterByID/{chapterID}")
    public ResponseEntity<?> updateChapterByID(@PathVariable("chapterID") Long chapterID,@ModelAttribute ChapterRequestDTO chapterPatch){
        Chapter chapter =chapterService.updateChapter(chapterID,chapterPatch);
        return new ResponseEntity<>(chapter, HttpStatus.OK);
    }
    @GetMapping("/ChapterHot")
    public ResponseEntity<?> chapterHot(){

        List<Chapter> newChapterList = chapterService.newestChapters();

        return new ResponseEntity<>(newChapterList, HttpStatus.OK);
    }
}
