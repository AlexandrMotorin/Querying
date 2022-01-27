package org.example.querying.dictionaryEntries;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "word")
@ToString(exclude = {"englishWord","numericWord"})
@Builder
@Entity
public class RussianWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String word;

    @ManyToOne(fetch = FetchType.LAZY)
    private EnglishWord englishWord;

    @ManyToOne(fetch = FetchType.LAZY)
    private NumericWord numericWord;
}
