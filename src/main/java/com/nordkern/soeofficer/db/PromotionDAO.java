package com.nordkern.soeofficer.db;

import com.google.inject.Inject;
import com.nordkern.soeofficer.api.Officer;
import com.nordkern.soeofficer.api.Promotion;
import com.nordkern.soeofficer.api.Rank;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by mortenfrank on 24/11/2017.
 */
@Slf4j
public class PromotionDAO extends AbstractDAO<Promotion> {

    @Inject
    OfficerDAO officerDAO;

    @Inject
    RankDAO rankDAO;

    @Inject
    public PromotionDAO(SessionFactory factory) {
        super(factory);
    }

    public Promotion findById(Long id) {
        return get(id);
    }

    public List<Promotion> findPromotionsByOfficerId(Long id) {
        return list(namedQuery("com.nordkern.soeofficer.api.Officer.promotionsForOfficer").setParameter("me",id));
    }

    public Promotion create(Promotion promotion) {
        return persist(promotion);
    }

    public void update(Promotion promotion) {
        Officer officer = findById(promotion.getId()).getOfficer();
        promotion.setOfficer(officer);
        currentSession().merge(promotion);
    }

    public void delete(Integer id) {
        Promotion promotion = findById(id.longValue());
        currentSession().delete(promotion);
    }

    public Promotion promote(Long id, Promotion promotion) {
        Officer officer = officerDAO.findById(id,true);
        Rank rank;
        if (officer.getPromotions().size()==0) {
            rank = rankDAO.findById(Integer.toUnsignedLong(1));
        } else {
            List<Rank> ranks = list(namedQuery("com.nordkern.soeofficer.api.Officer.nextRank").setParameter("officer_id",id).setParameter("date",promotion.getDateOfPromotion()));
            if (ranks.size() == 0) {
                return null;
            } else {
                rank = ranks.get(0);
            }
        }

        promotion.setOfficer(officer);
        promotion.setRank(rank);

        Rank firstRank = ((List<Rank>)list(namedQuery("com.nordkern.soeofficer.api.Officer.firstRank"))).get(0);
        if (firstRank.equals(rank)) {
            namedQuery("com.nordkern.soeofficer.api.Officer.setAppointedDate").setParameter("appointed_date",promotion.getDateOfPromotion()).setParameter("id",promotion.getOfficer().getId()).executeUpdate();
        }
        return create(promotion);
    }
}
