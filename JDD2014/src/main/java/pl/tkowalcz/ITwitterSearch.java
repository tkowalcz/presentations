package pl.tkowalcz;

import rx.Observable;
import twitter4j.User;

public interface ITwitterSearch {

	Observable<User> searchUsers(String prefix);
}
