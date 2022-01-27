package org.example.querying.dictionaryEntries.main;

import lombok.Cleanup;
import org.example.querying.HibernateUtils.SessionUtils;
import org.example.querying.dictionaryEntries.EnglishWord;
import org.example.querying.dictionaryEntries.NumericWord;
import org.example.querying.dictionaryEntries.RussianWord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class Run {
    public static void main(String[] args) {
       @Cleanup SessionFactory sessionFactory = SessionUtils.buildSessionFactory();
       @Cleanup Session session = sessionFactory.openSession();

       session.beginTransaction();

       saveEntry(session,"fire",List.of("гореть","обжигать","пламя"));

       session.getTransaction().commit();
    }

    private static void fillDb(Session session) {
        var dogEn = EnglishWord.builder()
                .word("dog")
                .build();

        var dogRu = RussianWord.builder()
                .word("собака")
                .build();

        var dogRu2 = RussianWord.builder()
                .word("бобик")
                .build();

        var dogNum = NumericWord.builder()
                .word(12341)
                .build();

        dogEn.addTranslate(dogRu);
        dogEn.addTranslate(dogRu2);
        dogNum.addTranslate(dogRu);

        session.save(dogRu2);
        session.save(dogRu);
        session.save(dogEn);
        session.save(dogNum);
    }

    private static void saveEntry(Session session, String en, List<String> ru){
        var englishWord = EnglishWord.builder().word(en).build();
        session.save(englishWord);

        ru.stream().map(s -> RussianWord.builder().word(s).build())
                .forEach(s -> {
                    session.save(s);
                    englishWord.addTranslate(s);
                });
    }


    void saveEntry(String rus, String eng){

    }

    void saveEntry(String rus, int num){

    }
}
