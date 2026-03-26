package fit.hutech.LePhuocToan_3296.controller.admin;

import fit.hutech.LePhuocToan_3296.service.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/audit-logs")
@RequiredArgsConstructor
public class AdminAuditLogController {
    private final SystemLogService systemLogService;

    @GetMapping
    public String auditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entity,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fromDate,
            Model model) {

        var pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        var logPage = (action != null && !action.isEmpty())
                ? systemLogService.getLogsByAction(action, pageable)
                : systemLogService.getAllLogs(pageable);

        model.addAttribute("logs", logPage.getContent());
        model.addAttribute("currentPage", logPage.getNumber());
        model.addAttribute("totalPages", logPage.getTotalPages());
        model.addAttribute("totalElements", logPage.getTotalElements());
        model.addAttribute("action", action);
        model.addAttribute("entity", entity);
        model.addAttribute("keyword", keyword);
        model.addAttribute("fromDate", fromDate);

        return "admin/audit-logs";
    }
}
