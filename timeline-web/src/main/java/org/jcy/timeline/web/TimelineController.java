package org.jcy.timeline.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/timeline")
public class TimelineController {

    @RequestMapping(value="/")
    public String index(Model model) {
        model.addAttribute("title", "MyTitle");
        model.addAttribute("records", null);
        return "index";
    }

    @RequestMapping(value="/more")
    public String more(Model model) {
        return "index";
    }
}
