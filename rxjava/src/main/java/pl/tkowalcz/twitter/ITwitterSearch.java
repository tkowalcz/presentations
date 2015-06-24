package pl.tkowalcz.twitter;

import pl.tkowalcz.examples.ui.TwitterUser;
import rx.Observable;

import java.util.List;

/**
 * Describes contract for twitter service returning list of users matching given prefix.
 */
public interface ITwitterSearch {

	Observable<List<TwitterUser>> searchUsers(String prefix);
}
