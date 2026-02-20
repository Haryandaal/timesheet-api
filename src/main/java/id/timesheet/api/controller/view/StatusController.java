package id.timesheet.api.controller.view;

import id.timesheet.api.dto.response.StatusResponse;
import id.timesheet.api.entity.Status;
import id.timesheet.api.repository.StatusRepository;
import id.timesheet.api.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusRepository statusRepository;
    private final StatusService statusService;

    @GetMapping
    public String index(Model model) {
        List<Status> statuses = statusRepository.findAll();
        model.addAttribute("statuses", statuses);
        return "status/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("status", new Status());
        return "status/form";
    }

    @GetMapping(path = "/edit/{id}")
    public String form(Model model, @PathVariable(required = false) String id) {
        StatusResponse response = statusService.getById(id);
        model.addAttribute("status", response);
        return "status/form";
    }

    @PostMapping(path = "/save")
    public String save(@ModelAttribute Status status) {

        if (status.getId() != null  && !status.getId().isEmpty()) {
            Status existing = statusRepository.findById(status.getId())
                    .orElseThrow(() -> new RuntimeException("Status not found"));

            existing.setName(status.getName());

            statusRepository.save(existing); // UPDATE
        } else {
            Status newStatus = Status.builder()
                    .name(status.getName())
                    .build();
            statusRepository.save(newStatus); // CREATE
        }

        return "redirect:/status";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable String id) {
        statusRepository.deleteById(id);
        return "redirect:/status";
    }
}
