package com.bionic.socialNetwork.logic;

import com.bionic.socialNetwork.dao.InterestDao;
import com.bionic.socialNetwork.dao.UserDao;
import com.bionic.socialNetwork.dao.impl.InterestDaoImpl;
import com.bionic.socialNetwork.dao.impl.UserDaoImpl;
import com.bionic.socialNetwork.models.Interest;
import com.bionic.socialNetwork.models.User;

import java.util.*;

/**
 * @version 1.0 on 28.07.2014.
 * @autor Bish_UA
 */
public class EditUserProfileLogic {




    public void edit(long userId, String name, String surname,
                     String position, String interests) {
        try {
            logic(userId,name,surname,position,interests);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void logic(long userId, String name, String surname,
                       String position, String interests) throws Exception {
        UserDao userDao = new UserDaoImpl();
        User user = userDao.selectById(userId);
        if (name != null) user.setName(name);
        if (surname != null) user.setSurname(surname);
        if (position !=null) user.setPosition(position);
        userDao.update(user);

        editInterests(userId, interests);
    }



    private void editInterests(long userId, String interest) throws Exception {

        //parsing income line to request array
        String[] requestArray = interest.split(",");
        int length = requestArray.length;
        for (int i = 0; i < length; i++) {
            requestArray[i] = requestArray[i].trim();
        }

        //convert request array to request list
        List<String> requestList = new ArrayList<String>(Arrays.asList(requestArray));

        UserDao userDao = new UserDaoImpl();

        //getting current interests from DB
        List<Interest> interestsFromDb = userDao.selectAllInterests(userId);

        //convert interest lists from DB to 2 String lists (for deleting and adding users interests)
        List<String> interestForDelete = new ArrayList<String>(interestsFromDb.size());
        List<String> interestsForSubtraction = new ArrayList<String>(interestsFromDb.size());
        for (Interest current : interestsFromDb) {
            interestForDelete.add(current.getInterest());
            interestsForSubtraction.add(current.getInterest());

        }

        //getting list of interests what we must to delete
        interestForDelete.removeAll(requestList);

        //getting current user from DB
        User user = userDao.selectById(userId);

        //deleting old interests from DB
        InterestDao interestDao = new InterestDaoImpl();
        for (String current : interestForDelete) {
            Interest delInterest = interestDao.selectByInterest(current);
            userDao.deleteInterests(delInterest, user);
        }

        //searching interests for insert into DB
        if (requestList.size() != 0) {
            requestList.removeAll(interestsForSubtraction);
        }

        //inserting new interest to DB
        if (requestList.size() != 0) {
            Interest inter = null;
            for (String current : requestList) {
                inter = null;
                inter = interestDao.selectByInterest(current);
                if (inter == null) {
                    inter = new Interest();
                    inter.setInterest(current);
                    Set<User> set = new HashSet<User>();
                    set.add(user);
                    inter.setUsers(set);
                    interestDao.insert(inter);
                } else {
                    Set<User> set = inter.getUsers();
                    set.add(user);
                    inter.setUsers(set);
                    interestDao.update(inter);

                }
            }
        }
    }
}