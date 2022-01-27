package org.example.querying.dictionaryEntries;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "word")
@ToString(exclude = "translate")
@Builder
@Entity
public class EnglishWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    @Builder.Default
    @OneToMany(mappedBy = "englishWord")
    private List<RussianWord> translate = new ArrayList<>();

    public void addTranslate(RussianWord word){
        translate.add(word);
        word.setEnglishWord(this);
    }

}
