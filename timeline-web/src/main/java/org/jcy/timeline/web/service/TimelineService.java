package org.jcy.timeline.web.service;

import org.jcy.timeline.core.provider.git.GitItem;
import org.jcy.timeline.core.ui.FetchOperation;
import org.jcy.timeline.web.model.FetchResponse;
import org.jcy.timeline.web.model.GitItemUi;
import org.jcy.timeline.web.model.RegisterResponse;
import org.jcy.timeline.web.ui.WebTimeline;
import org.jcy.timeline.web.ui.WebTimelineFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimelineService {

	private static final Map<String, WebTimeline> timelineMapping = new HashMap<>();

	public static boolean isValid(String sessionId) {
		return timelineMapping.containsKey(sessionId);
	}

	/**
	 *
	 * @param sessionId
	 * @param uri
	 * @param projectName
	 */
	public RegisterResponse register(String sessionId, String uri, String projectName) {
		RegisterResponse response = new RegisterResponse();
		response.setId(sessionId);
		WebTimeline timeline = WebTimelineFactory.create(sessionId, uri, projectName);
		synchronized (timelineMapping) {
			if (timelineMapping.containsKey(sessionId)) {
				unregister(sessionId);
			}
			timelineMapping.put(sessionId, timeline);
		}

		response.setSuccess(true);
		response.setItems(this.convert(timeline.getItems()));

		return response;
	}

	/**
	 *
	 * @param sessionId
	 */
	public boolean unregister(String sessionId) {
		boolean result = false;
		synchronized (timelineMapping) {
			WebTimeline old = timelineMapping.remove(sessionId);
			if (old != null) {
				old.stopAutoFresh();
				result = true;
			}
		}
		return result;
	}

	/**
	 *
	 * @param sessionId
	 */
	public boolean startAutoUpdate(String sessionId) {
		WebTimeline timeline = this.timelineMapping.get(sessionId);
		if (timeline != null) {
			timeline.startAutoFresh();
			return true;
		}
		return false;
	}

	public FetchResponse fetchMore(String sessionId) {
		return this.fetch(sessionId, FetchOperation.MORE);
	}

	public FetchResponse fetchNew(String sessionId) {
		return this.fetch(sessionId, FetchOperation.NEW);
	}

	private FetchResponse fetch(String sessionId, FetchOperation fetchOperation) {

		FetchResponse response = new FetchResponse();
		if (fetchOperation == null) {
			response.setSuccess(false);
			response.setCause("Can't match the fetch operation!");
			return response;
		}

		WebTimeline timeline = this.timelineMapping.get(sessionId);
		if (timeline == null) {
			response.setSuccess(false);
			response.setCause(String.format("The sessionId [%s] is not registered!", sessionId));
			return response;
		}

		response.setItems(this.convert(timeline.fetch(fetchOperation)));
		response.setSuccess(true);
		return response;
	}

	private List<GitItemUi> convert(List<GitItem> items) {
		if (!CollectionUtils.isEmpty(items)) {
			return items.stream().map(GitItemUi::new).collect(Collectors.toList());
		}

		return Collections.EMPTY_LIST;
	}
}