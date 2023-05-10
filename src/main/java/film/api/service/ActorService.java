package film.api.service;

import film.api.DTO.ActorDTO;
import film.api.DTO.UserByAdminDTO;
import film.api.models.Actor;
import film.api.models.Chapter;
import film.api.models.User;
import film.api.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;
    public List<Actor> getList(){

        return actorRepository.findAll();
    }

    public Actor addActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public Optional<Actor> findById(Long id) {
        return actorRepository.findById(id);
    }


    public Actor updateActor(Long id, ActorDTO actorPatchDTO) {

        Actor actor = actorRepository.findById(id).orElse(null);

        if(actorPatchDTO.getSex() != null) {
            actor.setSex(actorPatchDTO.getSex());
        }
        if(actorPatchDTO.getNativeLand() != null) {
            actor.setNativeLand(actorPatchDTO.getNativeLand());
        }
        if(actorPatchDTO.getAge() != null) {
            actor.setAge(actorPatchDTO.getAge());
        }
        if(actorPatchDTO.getActorName() != null) {
            actor.setActorName(actorPatchDTO.getActorName());
        }

        return actorRepository.save(actor);
    }


    public void deleteById(Long id) {
        actorRepository.deleteById(id);
    }

    public List<Actor> searchActors(String actorName) {
        return actorRepository.searchActors(actorName);
    }

    public List<Actor> findActorByChapterId(Long chapterId){
        return actorRepository.findActorByChapterId(chapterId);
    }
}
