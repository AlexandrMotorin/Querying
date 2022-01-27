package org.example.querying.dictionaryEntries;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "word")
@ToString(exclude = "translate")
@Builder
@Entity
public class NumericWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int word;

    @Builder.Default
    @OneToMany(mappedBy = "numericWord", cascade = CascadeType.PERSIST)
    private List<RussianWord> translate = new ArrayList<>();

    public void addTranslate(RussianWord word){
        translate.add(word);
        word.setNumericWord(this);
    }

}