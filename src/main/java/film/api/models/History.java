package film.api.models;

import javax.validation.constraints.NotNull;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private double WatchedTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter Chapter;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User User;

    private int Rate;

    @NotNull
    private LocalDateTime HistoryView;
}
