package com.nordkern.soeofficer.db;

import com.google.inject.Inject;
import com.nordkern.soeofficer.api.PersonRelation;
import com.nordkern.soeofficer.api.Relation;
import com.nordkern.soeofficer.api.RelationsResponse;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mortenfrank on 24/11/2017.
 */
public class RelationsDAO extends AbstractDAO<Relation> {

    @Inject
    public RelationsDAO(SessionFactory factory) {
        super(factory);
    }

    public Relation findById(Long id) {
        return get(id);
    }

    public RelationsResponse findRelationsOfPerson(Long id) {
        RelationsResponse response = new RelationsResponse();

        List<PersonRelation> children = getChildren(id);
        List<PersonRelation> parents = getParents(id);

        response.setChildren(children);
        response.setParents(parents);

        return response;
    }

    private List<PersonRelation> handle(Iterator iterator) {
        List<PersonRelation> result = new ArrayList<>();
        PersonRelation entry;
        Object[] tuple;

        while(iterator.hasNext()) {
            tuple = (Object[]) iterator.next();
            entry = new PersonRelation();
            entry.setId(((Integer)tuple[0]).longValue());
            entry.setGivenName(((String)tuple[1]));
            entry.setSurname(((String)tuple[2]));
            entry.setDateOfBirth((Date) tuple[3]);
            entry.setGender(PersonRelation.Gender.valueOf((String)tuple[4]));
            if (tuple[5] != null)
                entry.setDateOfDeath((Date)tuple[5]);
            else
                entry.setDateOfDeath(null);
            if (tuple[6] != null) {
                entry.setOfficerId(((Integer)tuple[6]).longValue());
            } else {
                entry.setOfficerId(null);
            }
            entry.setType(null);
            entry.setRelationID(((Integer)tuple[7]).longValue());

            result.add(entry);
        }
        return result;
    }


    private List<PersonRelation> getParents(Long id) {
        Iterator resultIterator =list(namedQuery("com.nordkern.soeofficer.api.Relations.findParents").setParameter("id",id)).iterator();
        return handle(resultIterator);
    }

    private List<PersonRelation> getChildren(Long id) {
        Iterator resultIterator =list(namedQuery("com.nordkern.soeofficer.api.Relations.findChildren").setParameter("id",id)).iterator();
        return handle(resultIterator);
    }

    public Relation create(Relation relation) {
        return persist(relation);
    }

    public void update(Relation relation) {
        currentSession().saveOrUpdate(relation);
    }

    public void delete(Long id) {
        Relation relation = findById(id.longValue());
        currentSession().delete(relation);
    }
}
