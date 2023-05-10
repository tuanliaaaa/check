package film.api.service;

import film.api.DTO.FilmRequestDTO;
import film.api.models.*;
import film.api.repository.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private ActorChapterRepository actorChapterRepository;
    @Autowired
    private CategoryFilmRepository categoryFilmRepository;
    public String getUniqueFileName(String fileName, String uploadDir) {
        String newFileName = fileName;
        int index = 1;
        File uploadedFile = new File(uploadDir + newFileName);
        while (uploadedFile.exists()) {
            newFileName = fileName.replaceFirst("[.][^.]+$", "") + "(" + index + ")" + "." +
                    FilenameUtils.getExtension(fileName);
            uploadedFile = new File(uploadDir + newFileName);
            index++;
        }
        return newFileName;
    }
    public String saveFile(MultipartFile file,String typeFile){
        //Lưu Image về server
        String rootDir = System.getProperty("user.dir");
        // Đường dẫn tương đối đến thư mục "images"
        String relativePath = "/src/main/resources/static/"+typeFile+"/";

        // Lưu file vào thư mục image
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileNameNew =getUniqueFileName(fileName,StringUtils.cleanPath(rootDir+relativePath));
        Path path = Paths.get(StringUtils.cleanPath(rootDir+relativePath)+fileNameNew);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Lưu đường dẫn của file vào CSDL
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/"+typeFile+"/")
                .path(fileNameNew)
                .toUriString();
        return  fileUrl;
    }
    public Film findById(Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    public Film saveFilm(FilmRequestDTO filmPost)  {


        if(filmPost.getFilmName()==null||filmPost.getFilmName().replaceAll("\\s+", "").equals(""))
        {
            throw new IllegalArgumentException("Vui Lòng nhập Tên Film");
        }
        if(filmPost.getDescription()==null||filmPost.getDescription().equals(""))
        {
            throw new IllegalArgumentException("Vui Lòng nhập Mô Tả Film");
        }

        if(filmPost.getListActor()==null)
        {
            throw new IllegalArgumentException("Vui Lòng nhập Tên Diễn Viên");
        }else{
            if(filmPost.getListActor().equals(""))throw new IllegalArgumentException("Vui Lòng nhập Tên Diễn Viên");
        }
        if(filmPost.getListCategory()==null)
        {
            throw new IllegalArgumentException("Vui Lòng nhập Thể Loại Film");
        }else{
            if(filmPost.getListCategory().equals("")) throw new IllegalArgumentException("Vui Lòng nhập Thể Loại Film");
        }
        if(filmPost.getImage()==null)
        {
            throw new IllegalArgumentException("Vui Lòng nhập Image Film");
        }
        if(filmPost.getBannerFilmName()==null)
        {
            throw new IllegalArgumentException("Vui Lòng nhập Banner Film");
        }
        if(filmPost.getTrailerFilm()==null)
        {
            throw new IllegalArgumentException("Vui Lòng nhập Trailer Film");
        }
        if(filmPost.getFilmBollen()==1)
        {
            if(filmPost.getChapterName()==null) throw new IllegalArgumentException("Vui Lòng nhập tên Chapter 1");
            if(filmPost.getChapterDescription()==null) throw new IllegalArgumentException("Vui Lòng nhập mô tả Chapter 1");
        }
        String image = saveFile(filmPost.getImage(),"images");
        String banner = saveFile(filmPost.getBannerFilmName(),"images");
        String trailer =saveFile(filmPost.getTrailerFilm(),"videos");
        String status="Đang Ra";
        String video="";
        if(filmPost.getVideo()!=null){
            video =saveFile(filmPost.getVideo(),"videos");
            status="Đã Ra";
        }

        Film film = filmRepository.save(new Film(null, filmPost.getFilmName(), filmPost.getDescription(), banner, filmPost.getFilmBollen(), trailer,image ));
        String[] categoryString = filmPost.getListCategory().split(",");
        Long[] categoryList = new Long[categoryString.length];
        for(int i = 0; i < categoryString.length; i++) {
            categoryList[i] = Long.parseLong(categoryString[i]);
        }
        String[] actorString = filmPost.getListCategory().split(",");
        Long[] actorList = new Long[actorString.length];
        for(int i = 0; i < actorString.length; i++) {
            actorList[i] = Long.parseLong(actorString[i]);
        }
        for(Long i:categoryList){
            Category category=categoryRepository.findById(i).orElseThrow(null);
            categoryFilmRepository.save(new CategoryFilm(null,category,film));
        }
        Chapter chapterNew=new Chapter(null,"",1,video,film,"",trailer,image, LocalDateTime.now(),null,status);
        if(filmPost.getFilmBollen()==0)
        {
            chapterNew.setChapterName(filmPost.getFilmName());
            chapterNew.setChapterDescription(filmPost.getDescription());

        }else{
            chapterNew.setChapterName(filmPost.getChapterName());
            chapterNew.setChapterDescription(filmPost.getChapterDescription());
        }
        if(filmPost.getVideo()!=null){
            chapterNew.setChapterPremieredDay(LocalDateTime.now());
        }
        Chapter chapterNewSave=chapterRepository.save(chapterNew);
        for(Long i:actorList){
            Actor actor=actorRepository.findById(i).orElseThrow(null);
            actorChapterRepository.save(new ActorChapter(null,actor,chapterNewSave));
        }

        return film;
    }
    public Film updateFilm(Long filmID,FilmRequestDTO filmPatch){
        Film film = filmRepository.findById(filmID).orElse(null);
        Chapter chapter = chapterRepository.getChapterByFilmID(film.getId()).get(0);

        if(filmPatch.getFilmName()!=null||filmPatch.getFilmName().replaceAll("\\s+", "").equals("")==false)
        {
            film.setFilmName(filmPatch.getFilmName());
            chapter.setChapterName(filmPatch.getFilmName());
        }
        if(filmPatch.getDescription()!=null||filmPatch.getDescription().equals("")==false)
        {
            film.setFilmDescription(filmPatch.getDescription());
            chapter.setChapterDescription(filmPatch.getDescription());
        }

        if(filmPatch.getListActor()!=null && filmPatch.getListCategory()!=null)
        {
            String[] actorString = filmPatch.getListCategory().split(",");
            Long[] actorList = new Long[actorString.length];
            for(int i = 0; i < actorString.length; i++) {
                actorList[i] = Long.parseLong(actorString[i]);
            }
            String[] categoryString = filmPatch.getListCategory().split(",");
            Long[] categoryList = new Long[categoryString.length];
            for(int i = 0; i < categoryString.length; i++) {
                categoryList[i] = Long.parseLong(categoryString[i]);
            }

            if(actorList.length==0)throw new IllegalArgumentException("Vui Lòng nhập Actor");
            if(categoryList.length==0)throw new IllegalArgumentException("Vui Lòng nhập Category");
            List<ActorChapter>  actorChapterList = actorChapterRepository.findActorChapterByChapterId(chapter.getId());
            for(ActorChapter actorChapter:actorChapterList){
                actorChapterRepository.delete(actorChapter);
            }


            for(Long i :actorList){
                Actor actor =actorRepository.findById(i).orElse(null);
                if(actor!=null){
                    ActorChapter  actorChapter = new ActorChapter(null,actor,chapter);
                    actorChapterRepository.save(actorChapter);
                }
            }


            List<CategoryFilm>  categoryFilms = categoryFilmRepository.findCategoryFilmByFilmID(film.getId());
            for(CategoryFilm categoryFilm:categoryFilms){
                categoryFilmRepository.delete(categoryFilm);
            }

            for(Long i :categoryList){
                Category category =categoryRepository.findById(i).orElse(null);
                if(category!=null){
                    CategoryFilm  categoryFilm = new CategoryFilm(null,category,film);
                    categoryFilmRepository.save(categoryFilm);
                }
            }
        }
        if(filmPatch.getImage()!=null)
        {
            String image = saveFile(filmPatch.getImage(),"images");
            film.setFilmImage(image);
            chapter.setChapterImage(image);
        }
        if(filmPatch.getBannerFilmName()!=null)
        {
            String banner = saveFile(filmPatch.getBannerFilmName(),"images");
            film.setBannerFilmName(banner);

        }
        if(filmPatch.getTrailerFilm()!=null)
        {
            String trailer =saveFile(filmPatch.getTrailerFilm(),"videos");
            film.setTrailerFilm(trailer);
            chapter.setTrailerChapter(trailer);
        }
        if(filmPatch.getFilmBollen()==1)
        {
            if(filmPatch.getChapterName()==null) throw new IllegalArgumentException("Vui Lòng nhập tên Chapter 1");
            if(filmPatch.getChapterDescription()==null) throw new IllegalArgumentException("Vui Lòng nhập mô tả Chapter 1");
            film.setFilmBollen(filmPatch.getFilmBollen());
            chapter.setChapterName(filmPatch.getChapterName());
            chapter.setChapterDescription(filmPatch.getChapterDescription());
        }

        String status="";
        if(filmPatch.getVideo()!=null){
            String video =saveFile(filmPatch.getVideo(),"videos");
            chapter.setVideo(video);
            chapter.setChapterPremieredDay(LocalDateTime.now());
            status="Đã Ra";
        }else{
            status="Đang Ra";
        }
        chapterRepository.save(chapter);
        filmRepository.save(film);

        return film;
    }
    public List<Film> findUsersByFilmNameContain(String filmName) {
        return filmRepository.findUsersByFilmNameContain(filmName);
    }
    public  void deleteFilmByID(Long filmID){
//        List<CategoryFilm> categoryFilms =categoryFilmRepository.findCategoryFilmByFilmID(filmID);
//        for(CategoryFilm categoryFilm:categoryFilms){
//            categoryFilmRepository.delete(categoryFilm);
//        }
    }
    public Film save(Film film) {
        return filmRepository.save(film);
    }

    public void deleteById(Long id) {
        filmRepository.deleteById(id);
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }
    public Film filmByIdChapter(Long chapterId){
        return filmRepository.filmByIdChapter(chapterId);
    }

    public List<Film> filmByCategoryId(Long categoryId){
        return filmRepository.categoryAllFim(categoryId);
    }
    public List<Film> searchFilm(String key) {
        return filmRepository.searchFilm(key);
    }
}