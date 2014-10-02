package pl.tkowalcz;

import rx.Observable;

import java.util.List;

public interface ITwitterSearch {

	Observable<List<TwitterUser>> searchUsers(String prefix);
}
