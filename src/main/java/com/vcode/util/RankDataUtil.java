package com.vcode.util;

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
public class RankDataUtil {
  public static List<RankUser> SortRankData(List<Rank> rankList) {
    Map<String, RankUser> rankUserMap = new HashMap<>();
    for (Rank item : rankList) {
      if (rankUserMap.containsKey(item.getUserAccount())) {
        RankUser rankUser = rankUserMap.get(item.getUserAccount());
        RankProblemItem problemItem = new RankProblemItem(item);
        rankUser.pushItemToList(problemItem);
        rankUser.setTotalAcNum(rankUser.getTotalAcNum() + item.getAcNum());
        rankUser.setTotalErrorNum(rankUser.getTotalErrorNum() + item.getWrongNum());
        rankUser.setTotalTimeUsed(rankUser.getTotalTimeUsed() + item.getUsedTime());
        rankUserMap.put(item.getUserAccount(), rankUser);
      } else {
        // init rankUser
        RankUser rankUser = new RankUser(item);
        // init rankProblemItem
        RankProblemItem problemItem = new RankProblemItem(item);
        // process item
        rankUser.pushItemToList(problemItem);
        rankUser.setTotalAcNum(rankUser.getTotalAcNum() + item.getAcNum());
        rankUser.setTotalErrorNum(rankUser.getTotalErrorNum() + item.getWrongNum());
        rankUser.setTotalTimeUsed(rankUser.getTotalTimeUsed() + item.getUsedTime());
        rankUserMap.put(item.getUserAccount(), rankUser);
      }
    }
    List<RankUser> rankUsers = new ArrayList<>(rankUserMap.values());
    // collation
    Comparator<RankUser> c = (rankUser1, rankUser2) -> {
      if (rankUser1.getTotalAcNum() > rankUser2.getTotalAcNum()) return -1;
      else if (rankUser1.getTotalAcNum() == rankUser2.getTotalAcNum()) {
        return -Long.compare(rankUser2.getTotalTimeUsed(), rankUser1.getTotalTimeUsed());
      } else {
        return 1;
      }
    };
    rankUsers.sort(c);
    return rankUsers;
  }
}
