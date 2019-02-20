package org.jcy.timeline.web.controller;

import org.jcy.timeline.web.model.FetchRequest;
import org.jcy.timeline.web.model.FetchResponse;
import org.jcy.timeline.web.model.RegisterRequest;
import org.jcy.timeline.web.model.RegisterResponse;
import org.jcy.timeline.web.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class TimelineController {

	@Autowired
	private TimelineService timelineService;

	/**
	 *
	 */
	@MessageMapping("/startAutoUpdate")
	public void startAutoUpdate(Principal principal) {
		if (principal != null) {
			timelineService.startAutoUpdate(principal.getName());
		}
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	/**
	 *
	 * @param request
	 */
	@RequestMapping("/register")
	@ResponseBody
	public RegisterResponse register(RegisterRequest request, HttpSession session) {
		return timelineService.register(session.getId(), request.getUri(), request.getProjectName());
	}

	/**
	 *
	 * @param request
	 */
	@RequestMapping("/new")
	@ResponseBody
	public FetchResponse fetchNew(FetchRequest request) {
		return timelineService.fetchNew(request.getId());
	}

	/**
	 *
	 * @param request
	 */
	@RequestMapping("/more")
	@ResponseBody
	public FetchResponse fetchMore(FetchRequest request) {
		return timelineService.fetchMore(request.getId());
	}

}