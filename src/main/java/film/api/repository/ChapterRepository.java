package film.api.repository;

import film.api.models.Chapter;
import film.api.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    @Query("SELECT C FROM Chapter C JOIN Film F on F.Id=C.Film.Id where F.Id=:filmID")
    List<Chapter> getChapterByFilmID(@Param("filmID") Long filmID);
    @Query("SELECT c FROM Chapter c WHERE c.Film.Id = (SELECT c.Film.Id FROM c WHERE c.Id = :id)")
    List<Chapter> chapterByChapterId(@Param("id") Long id);

    @Query("SELECT c FROM Chapter c WHERE c.Id = :id")
    Chapter ChapterByIdChapter(@Param("id") Long id);
    @Query("SELECT c FROM Chapter c WHERE c.id NOT IN (:chapterIDList)")
    List<Chapter> findAllByIdNotIn(@Param("chapterIDList") List<Long> chapterIDList);
    @Query("SELECT c FROM Chapter c ORDER BY c.ChapterCreateDay DESC")
    List<Chapter> Newest();
    @Query("SELECT c.ChapterNumber FROM Chapter c WHERE c.Id = (  SELECT MAX(c.ChapterNumber)  FROM c  where c.Film.Id= :idFilm )")
    Integer chapternumberbyIdFilmInt(@Param("idFilm") Long idFilm);

}