package com.vcode.handler;

import com.vcode.entity.Rank;
import com.vcode.entity.RankProblemItem;
import com.vcode.entity.RankUser;

import java.util.*;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
public class RankDataHandler {
  public static List<RankUser> sortRankData(List<Rank> rankList) {
    Map<String, RankUser> rankUserMap = new HashMap<>(16);
    for (Rank item : rankList) {
      RankUser rankUser;
      if (rankUserMap.containsKey(item.getUserAccount())) {
        rankUser = rankUserMap.get(item.getUserAccount());
      } else {
        // init rankUser
        rankUser = new RankUser(item);
      }
      RankProblemItem problemItem = new RankProblemItem(item);
      rankUser.pushItemToList(problemItem);
      rankUser.setTotalAcNum(rankUser.getTotalAcNum() + item.getAcNum());
      rankUser.setTotalErrorNum(rankUser.getTotalErrorNum() + item.getWrongNum());
      rankUser.setTotalTimeUsed(rankUser.getTotalTimeUsed() + item.getUsedTime());
      rankUserMap.put(item.getUserAccount(), rankUser);
    }
    List<RankUser> rankUsers = new ArrayList<>(rankUserMap.values());
    // collation
    Comparator<RankUser> c = (rankUser1, rankUser2) -> {
      if (rankUser1.getTotalAcNum() > rankUser2.getTotalAcNum()) {
        return -1;
      } else if (rankUser1.getTotalAcNum() == rankUser2.getTotalAcNum()) {
        return -Long.compare(rankUser2.getTotalTimeUsed(), rankUser1.getTotalTimeUsed());
      } else {
        return 1;
      }
    };
    rankUsers.sort(c);
    return rankUsers;
  }
}
