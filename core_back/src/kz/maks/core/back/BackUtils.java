package kz.maks.core.back;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

public class BackUtils {

    public static long getRowCount(Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        long rowCount = (long) criteria.uniqueResult();
        return rowCount;
    }

    public static boolean setPagination(Criteria criteria, long rowCount, int pageSize, int page) {
        int pagesCount = (int) (rowCount / pageSize + (rowCount % pageSize > 0 ? 1 : 0));
        int firstRow = (page - 1) * pageSize;
        criteria.setFirstResult(firstRow).setMaxResults(pageSize);
        boolean hasNext = page < pagesCount;
        return hasNext;
    }

}
