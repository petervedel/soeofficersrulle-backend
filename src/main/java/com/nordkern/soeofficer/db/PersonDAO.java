package com.nordkern.soeofficer.db;

import com.google.inject.Inject;
import com.nordkern.soeofficer.api.CSVFile;
import com.nordkern.soeofficer.api.Officer;
import com.nordkern.soeofficer.api.Person;
import com.nordkern.soeofficer.api.PersonSearch;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mortenfrank on 24/11/2017.
 */
@Slf4j
public class PersonDAO extends AbstractDAO<Person> {

    @Inject
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Person findById(Long id) {
        return get(id);
    }

    public List<Person> findByCriteria(PersonSearch personSearch) {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        List<Predicate> conditionsList = new ArrayList<>();
        List<Person> result = new ArrayList<>();

        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);
        query.select(root);
        Predicate givenName = builder.equal(root.get("givenName"),personSearch.getGivenName());
        Predicate surname = builder.equal(root.get("surname"),personSearch.getSurname());
        Predicate gender = builder.equal(root.get("gender"),personSearch.getGender());

        if (personSearch.getYearOfBirthFrom() == null && personSearch.getGivenName() == null && personSearch.getSurname() == null && personSearch.getGender() == null) {
            result = list(namedQuery("com.nordkern.soeofficer.api.Person.findAll"));
            return result;
        }
        if (personSearch.getYearOfBirthFrom() != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, personSearch.getYearOfBirthFrom());
            cal.set(Calendar.DAY_OF_YEAR, 1);
            Date yearOfBirthFrom = cal.getTime();

            cal.set(Calendar.YEAR, personSearch.getYearOfBirthTo());
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            Date yearOfBirthTo = cal.getTime();

            conditionsList.add(builder.between(root.get("dateOfBirth"),yearOfBirthFrom,yearOfBirthTo));
        }
        if (personSearch.getGivenName() != null) {
            conditionsList.add(givenName);
        }
        if (personSearch.getSurname() != null) {
            conditionsList.add(surname);
        }
        if (personSearch.getGender() != null) {
            conditionsList.add(gender);
        }

        List<Person> persons = currentSession().createQuery(query.select(root).where(builder.and((conditionsList.toArray(new Predicate[]{}))))).getResultList();
        result = new ArrayList<>(persons);
        if (0 < persons.size()) {
            List<Officer> officers_tmp = list(namedQuery("com.nordkern.soeofficer.api.Officer.findAll"));


            for (Officer officer : officers_tmp) {
                if (persons.contains(officer.getPerson())) {
                    result.remove(officer.getPerson());
                }
            }
        }

        return result;
    }

    public void processFile(CSVFile file) throws ParseException {
        Person entry;

        String content = file.getFile();
        String[] lines = content.split("\\r?\\n");
        String[] attr;

        log.info("Length: "+content.length());
        for (String line : lines) {
            log.info("Line: "+line);
            if (!line.contains("given_name")) {
                attr = line.split(",");
                entry = new Person();
                entry.setGivenName(attr[0].trim().replace("\"", "").replace("\'",""));
                entry.setSurname(attr[1].trim().replace("\"", "").replace("\'",""));
                entry.setDateOfBirth((new SimpleDateFormat("dd/MM/yyyy")).parse(attr[2].trim().replace("\"", "").replace("\'","")));
                entry.setGender(Person.Gender.valueOf(attr[3].trim().replace("\"", "").replace("\'","")));
                if (attr[4].trim().contains("NULL") || attr[4].trim().contains("null")) {
                    entry.setDateOfDeath(null);
                } else {
                    entry.setDateOfDeath((new SimpleDateFormat("dd/MM/yyyy")).parse(attr[4].trim().replace("\"", "").replace("\'","")));
                }

                create(entry);
            }
        }
    }

    public Person create(Person person) {
        return persist(person);
    }

    public void update(Person person) {
        currentSession().saveOrUpdate(person);
    }

    public void delete(Integer id) {
        Person person = findById(id.longValue());
        currentSession().delete(person);
    }

    public List<Person> findAll() {
        return list(namedQuery("com.nordkern.soeofficer.api.Person.findAll"));
    }
}
