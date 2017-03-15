package org.lpw.ranch.doc;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface DocDao {
    DocModel findById(String id);

    PageList<DocModel> query(int pageSize, int pageNum);

    void save(DocModel doc);

    void read(String id, int n);

    void favorite(String id, int n);

    void comment(String id, int n);
}
