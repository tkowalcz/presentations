package pl.tkowalcz.twitter;

import pl.tkowalcz.TwitterUser;
import rx.Observable;

import java.util.List;

public interface ITwitterSearch {

	Observable<List<TwitterUser>> searchUsers(String prefix);
}
