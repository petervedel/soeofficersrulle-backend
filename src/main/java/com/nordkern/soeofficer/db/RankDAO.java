package com.nordkern.soeofficer.db;

import com.google.inject.Inject;
import com.nordkern.soeofficer.api.Rank;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by mortenfrank on 24/11/2017.
 */
public class RankDAO extends AbstractDAO<Rank> {

    @Inject
    public RankDAO(SessionFactory factory) {
        super(factory);
    }

    public Rank findById(Long id) {
        return get(id);
    }

    public Rank create(Rank rank) {
        return persist(rank);
    }

    public void update(Rank rank) {
        currentSession().saveOrUpdate(rank);
    }

    public void delete(Integer id) {
        Rank rank = findById(id.longValue());
        currentSession().delete(rank);
    }


    public List<Rank> findAllActiveOnDate(Date date) {
        return list(namedQuery("com.nordkern.soeofficer.api.RanksActiveOnDateParam.find").setParameter("theDate", date));
    }

    public List<Rank> findAll() {
        return list(namedQuery("com.nordkern.soeofficer.api.Rank.findAll"));
    }
}
