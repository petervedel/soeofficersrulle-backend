package com.nordkern.soeofficer.db;

import com.google.inject.Inject;
import com.nordkern.soeofficer.api.Officer;
import com.nordkern.soeofficer.api.OfficerCorps;
import com.nordkern.soeofficer.api.OfficerSearch;
import com.nordkern.soeofficer.api.Person;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.util.*;

/**
 * Created by mortenfrank on 24/11/2017.
 */
@Slf4j
public class OfficerDAO extends AbstractDAO<Officer> {
    @Inject
    PersonDAO personDAO;

    @Inject
    public OfficerDAO(SessionFactory factory) {
        super(factory);
    }

    public Officer findById(Long id, boolean limitless) {
        if (!limitless) {
            Officer officer = get(id);
            Officer masked = new Officer();
            masked.setAppointedUntil(officer.getAppointedUntil());
            masked.setPromotions(officer.getPromotions());
            masked.setId(officer.getId());
            masked.setAppointedDate(officer.getAppointedDate());
            masked.setDodabNumber(officer.getDodabNumber());
            masked.setAppointedNumber(officer.getAppointedNumber());
            masked.setTerminationCause(officer.getTerminationCause());

            Person maskedPerson = new Person();
            maskedPerson.setId(officer.getPerson().getId());
            maskedPerson.setDateOfBirth(officer.getPerson().getDateOfBirth());
            maskedPerson.setDateOfDeath(officer.getPerson().getDateOfDeath());
            maskedPerson.setGender(officer.getPerson().getGender());
            maskedPerson.setGivenName("");
            maskedPerson.setSurname("");

            masked.setPerson(maskedPerson);
            return masked;
        } else {
            return get(id);
        }
    }

    public List<Officer> findByCriteriaLimited(List<Officer> officers) {
        List<Officer> officersCopy = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, -110);
        Date dateBefore110Years = cal.getTime();
        Officer officerCopy;
        Person personCopy;

        for (Officer officer : officers) {
            if (officer.getPerson().getDateOfBirth().after(dateBefore110Years)) {
                officerCopy = new Officer();
                officerCopy.setId(officer.getId());
                officerCopy.setTerminationCause(officer.getTerminationCause());
                officerCopy.setDodabNumber(officer.getDodabNumber());
                officerCopy.setAppointedDate(officer.getAppointedDate());
                officerCopy.setAppointedUntil(officer.getAppointedUntil());
                officerCopy.setPromotions(officer.getPromotions());
                officerCopy.setAppointedNumber(officer.getAppointedNumber());

                personCopy = new Person();
                personCopy.setId(officer.getPerson().getId());
                personCopy.setGender(officer.getPerson().getGender());
                personCopy.setDateOfDeath(officer.getPerson().getDateOfDeath());
                personCopy.setDateOfBirth(officer.getPerson().getDateOfBirth());
                officerCopy.setPerson(personCopy);

                officersCopy.add(officerCopy);
            } else {
                officersCopy.add(officer);
            }
        }

        return officersCopy;
    }

    public List<Officer> findByCriteria(OfficerSearch officerSearch, boolean limitless) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        List<Predicate> personConditionsList = new ArrayList<>();
        List<Predicate> officerConditionsList = new ArrayList<>();
        List<Person> persons;
        List<Officer> officers = new ArrayList<>();

        CriteriaQuery<Person> personQuery = builder.createQuery(Person.class);
        CriteriaQuery<Officer> officerQuery = builder.createQuery(Officer.class);
        Root<Person> personRoot = personQuery.from(Person.class);
        Root<Officer> officerRoot = officerQuery.from(Officer.class);
        personQuery.select(personRoot);
        officerQuery.select(officerRoot);

        Predicate givenName = builder.equal(personRoot.get("givenName"),officerSearch.getGivenName());
        Predicate surname = builder.equal(personRoot.get("surname"),officerSearch.getSurname());
        Predicate gender = builder.equal(personRoot.get("gender"),officerSearch.getGender());

        if (officerSearch.getYearOfBirthFrom() != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, officerSearch.getYearOfBirthFrom());
            cal.set(Calendar.DAY_OF_YEAR,1);
            Date yearOfBirthFrom = cal.getTime();

            cal.set(Calendar.YEAR, officerSearch.getYearOfBirthTo());
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DAY_OF_MONTH, 31);

            Date yearOfBirthTo = cal.getTime();

            personConditionsList.add(builder.between(personRoot.get("dateOfBirth"),yearOfBirthFrom,yearOfBirthTo));
        }
        if (officerSearch.getGivenName() != null) {
            personConditionsList.add(givenName);
        }
        if (officerSearch.getSurname() != null) {
            personConditionsList.add(surname);
        }
        if (officerSearch.getGender() != null) {
            personConditionsList.add(gender);
        }
        if (officerSearch.getAppointedYearFrom() != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, officerSearch.getAppointedYearFrom());
            cal.set(Calendar.DAY_OF_YEAR,1);
            Date appointedYearFrom = cal.getTime();

            cal.set(Calendar.YEAR, officerSearch.getAppointedYearTo());
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            Date appointedYearTo = cal.getTime();

            officerConditionsList.add(builder.between(officerRoot.get("appointedDate"),appointedYearFrom,appointedYearTo));
        }

        if (0 < personConditionsList.size()) {
            persons = currentSession().createQuery(personQuery.select(personRoot).where(builder.and((personConditionsList.toArray(new Predicate[]{}))))).getResultList();
            List<Officer> officers_tmp = list(namedQuery("com.nordkern.soeofficer.api.Officer.findAll"));


            for (Officer officer : officers_tmp) {
                if (persons.contains(officer.getPerson())) {
                    officers.add(officer);
                }
            }

            if (officers.size() == 0) {
                return officers;
            }
        } else if (officerConditionsList.size() == 0 && officerSearch.getRankID() == null) {
            officers = findAll();
        }

        if (0 < officerConditionsList.size()) {
            List<Officer> officers_tmp= currentSession().createQuery(officerQuery.select(officerRoot).where(builder.and((officerConditionsList.toArray(new Predicate[]{}))))).getResultList();
            List<Officer> result = new ArrayList<>();
            if (0 < officers.size()) {
                if (0 < officers_tmp.size()) {
                    for (Officer officer : officers) {
                        if (officers_tmp.contains(officer)) {
                            result.add(officer);
                        }
                    }
                    officers.clear();
                    officers.addAll(result);
                } else {
                    officers.clear();
                    return officers;
                }
            } else {
                officers.addAll(officers_tmp);
            }
        }

        if (officerSearch.getRankID() != null) {
            List<Officer> officers_tmp = list(namedQuery("com.nordkern.soeofficer.api.Officer.officersWithLastRank").setParameter("rank_id",officerSearch.getRankID()));
            List<Officer> result = new ArrayList<>();
            if (0 < officers.size()) {
                if (0 < officers_tmp.size()) {
                    for (Officer officer : officers) {
                        if (officers_tmp.contains(officer)) {
                            result.add(officer);
                        }
                    }
                    officers.clear();
                    officers.addAll(result);
                } else {
                    officers.clear();
                    return officers;
                }
            } else {
                officers.addAll(officers_tmp);
            }
        }


        if (!limitless) {
            return findByCriteriaLimited(officers);
        }

        return officers;
    }

    public Officer create(Officer officer) throws ParseException {
        if (officer.getAppointedUntil() == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(9999, 11, 31);
            officer.setAppointedUntil(cal.getTime());
        }
        return persist(officer);
    }

    public List<OfficerCorps> findOfficersActiveOnDate(Date date, boolean limitless) {
        List<OfficerCorps> result = new ArrayList<>();
        Iterator resultIterator = list(namedQuery("com.nordkern.soeofficer.api.Officer.officersActiveAtDate").setParameter("date",date)).iterator();
        OfficerCorps entry;
        Object[] tuple;
        while(resultIterator.hasNext()) {
            tuple = (Object[]) resultIterator.next();
            entry = new OfficerCorps();
            entry.setId(((Integer)tuple[0]).longValue());
            if (tuple[1] != null)
                entry.setAppointedNumber(((Integer)tuple[1]).longValue());
            else
                entry.setAppointedNumber(null);
            if (tuple[2] != null)
                entry.setDodabNumber(((Integer)tuple[2]).longValue());
            else
                entry.setDodabNumber(null);
            entry.setAppointedDate((Date) tuple[3]);
            entry.setAppointedUntil((Date)tuple[4]);
            if (tuple[5] != null)
                entry.setTerminationCause(Officer.TerminationCause.valueOf((String)tuple[5]));
            else
                entry.setTerminationCause(null);
            entry.setPersonId(((Integer)tuple[6]).longValue());
            entry.setRankName((String)tuple[7]);
            entry.setGivenName((String)tuple[8]);
            entry.setSurname((String)tuple[9]);
            entry.setDateOfBirth((Date)tuple[10]);
            entry.setGender(Person.Gender.valueOf((String)tuple[11]));

            if (!limitless) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.YEAR, -110);
                Date dateBefore110Years = cal.getTime();

                if (entry.getDateOfBirth().after(dateBefore110Years)) {
                    entry.setGivenName("");
                    entry.setSurname("");
                }
            }

            result.add(entry);
        }
        return result;
    }

    public void update(Officer officer) throws ParseException {
        if (officer.getAppointedUntil() == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(9999, 11, 31);
            officer.setAppointedUntil(cal.getTime());
        }
        personDAO.update(officer.getPerson());
        currentSession().saveOrUpdate(officer);
    }

    public void delete(Integer id) {
        Officer officer = findById(id.longValue(),true);
        currentSession().delete(officer);
    }

    public List<Officer> findAll() {
        return list(namedQuery("com.nordkern.soeofficer.api.Officer.findAll"));
    }
}
