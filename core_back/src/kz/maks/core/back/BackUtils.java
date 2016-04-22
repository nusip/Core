package kz.maks.core.back;

import com.google.common.base.Strings;
import kz.maks.core.shared.dtos.AbstractSearchParams;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;

public class BackUtils {

    public static long getRowCount(Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        long rowCount = (long) criteria.uniqueResult();
        return rowCount;
    }

    public static boolean setPaginationAndSorting(Criteria criteria, long rowCount, AbstractSearchParams params) {
        if (!isNullOrEmpty(params.getSortField())) {
            criteria.addOrder(params.getSortAsc() ? asc(params.getSortField()) : desc(params.getSortField()));
        }
        int pagesCount = (int) (rowCount / params.getPageSize() + (rowCount % params.getPageSize() > 0 ? 1 : 0));
        int firstRow = (params.getPage() - 1) * params.getPageSize();
        criteria.setFirstResult(firstRow).setMaxResults(params.getPageSize());
        boolean hasNext = params.getPage() < pagesCount;
        return hasNext;
    }

}
