package film.api.service;

import film.api.DTO.ChapterHotDTO;
import film.api.DTO.HistoryRequestDTO;
import film.api.models.Actor;
import film.api.models.Chapter;
import film.api.models.History;
import film.api.models.User;
import film.api.repository.ChapterRepository;
import film.api.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    public List<History> getList(){

        return historyRepository.findAll();
    }

        public List<ChapterHotDTO> getChaptersHotCount(LocalDateTime fromDay, LocalDateTime toDay) {
            List<Object[]> results = historyRepository.getChaptersHotCount(fromDay, toDay);
            List<ChapterHotDTO> chapterHotDTOs = new ArrayList<>();
            for (Object[] result : results) {
                ChapterHotDTO chapterHotDTO = new ChapterHotDTO();
                BigInteger bigInt = (BigInteger) result[0];
                Long chapterID = bigInt.longValue();
                Optional<Chapter> chapter=chapterRepository.findById(chapterID);
                chapterHotDTO.setId((BigInteger) result[0]);
                chapterHotDTO.setCountView((BigInteger) result[1]);
                chapterHotDTO.setRateAvg((BigDecimal) result[2]);
                chapterHotDTO.setChapterImage(chapter.get().getChapterImage());
                chapterHotDTO.setChapterName(chapter.get().getChapterName());
                chapterHotDTOs.add(chapterHotDTO);
            }
            return chapterHotDTOs;
        }

    public History getHistory(Long idChapter,Long userID){

        try{
            History history = historyRepository.historyByUserIDAndChapterID(userID,idChapter);
            return history;
        }
        catch (NoResultException ex){
            return null;
        }

    }
    public Long getUserID(String username){
        Long userid = historyRepository.useridByUserName(username);
        return userid;
    }
    public History saveHistory(History history){
        return historyRepository.save(history);
    }
    public History  updateHistory(User user, Chapter chapter, HistoryRequestDTO historyPatch){
        History history = historyRepository.historyByUserIDAndChapterID(user.getId(),chapter.getId());
        if(historyPatch.getRate()!=null)
        {
            history.setRate(historyPatch.getRate());
        }
        if(historyPatch.getHistoryView()!=null){
            history.setHistoryView(historyPatch.getHistoryView());
        }
        if(historyPatch.getWatchedTime()!=null){
            history.setWatchedTime(historyPatch.getWatchedTime());
        }
        return historyRepository.save(history);
    }
    public List<History> getListhistory(Long id){
        return historyRepository.historyByIdUser(id);
    }
    public List<Chapter> findChaptersByUserId(Long filmID){
        return historyRepository.findChaptersByUserId(filmID);
    }
    public List<Chapter> getChaptersHot(LocalDateTime fromDay, LocalDateTime toDay) {
        List<Object[]> results = historyRepository.getChaptersHotCount(fromDay, toDay);
        List<Chapter> chapterHots = new ArrayList<>();
        for (Object[] result : results) {
            ChapterHotDTO chapterHotDTO = new ChapterHotDTO();
            BigInteger bigInt = (BigInteger) result[0];
            Long chapterID = bigInt.longValue();
            Chapter chapter=chapterRepository.findById(chapterID).orElse(null);
            if(chapter!=null)chapterHots.add(chapter);

        }
        return chapterHots;
    }
}
