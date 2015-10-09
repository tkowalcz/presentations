package pl.tkowalcz.twitter;

import java.util.List;

import rx.Observable;

/**
 * Describes contract for twitter service returning list of users matching given prefix.
 */
public interface ITwitterSearch {

	Observable<List<TwitterUser>> searchUsers(String prefix);
}
