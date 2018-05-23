package com.nordkern.soeofficer.db;

import com.google.inject.Inject;
import com.nordkern.soeofficer.api.Relation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by mortenfrank on 24/11/2017.
 */
public class RelationsDAO extends AbstractDAO<Relation> {

    @Inject
    public RelationsDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Relation> findParentsOfPerson(Long id) {
        return list(namedQuery("com.nordkern.soeofficer.api.Relations.findParents").setParameter("id",id));
    }

    public List<Relation> findChildrenOfPerson(Long id) {
        return list(namedQuery("com.nordkern.soeofficer.api.Relations.findChildren").setParameter("id",id));
    }

    public Relation create(Relation relation) {
        return persist(relation);
    }

    public void update(Relation relation) {
        currentSession().saveOrUpdate(relation);
    }

}
