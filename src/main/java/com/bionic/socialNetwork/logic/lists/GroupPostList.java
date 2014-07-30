package com.bionic.socialNetwork.logic.lists;

import com.bionic.socialNetwork.dao.GroupDao;
import com.bionic.socialNetwork.dao.GroupPostDao;
import com.bionic.socialNetwork.dao.impl.GroupDaoImpl;
import com.bionic.socialNetwork.dao.impl.GroupPostDaoImpl;
import com.bionic.socialNetwork.models.Group;
import com.bionic.socialNetwork.models.GroupPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Collection;
import java.sql.Date;

/**
 * @author Dmytro Troshchuk
 * @version 1.00  23.07.14.
 */
public class GroupPostList {
    private Collection<GroupPost> groupPosts;

    @JsonIgnore
    private static final Logger LOGGER =
            LogManager.getLogger(GroupPostList.class.getName());

    public GroupPostList(long id, int page) {
        try {
            GroupPostDao groupPostDao = new GroupPostDaoImpl();
            Group group = new GroupDaoImpl().selectById(id);
            groupPostDao.selectLastWith(group, page);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public Collection<GroupPost> getGroupPosts() {
        return groupPosts;
    }
}
