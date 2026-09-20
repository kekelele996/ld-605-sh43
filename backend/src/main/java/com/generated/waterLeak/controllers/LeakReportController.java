package com.generated.waterLeak.controllers;

import com.generated.waterLeak.routes.LeakReportRoutes;
import com.generated.waterLeak.services.LeakReportService;
import com.generated.waterLeak.types.LeakReportPayload;
import com.generated.waterLeak.types.VerifyPayload;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LeakReportRoutes.PATH)
public class LeakReportController {
  private final LeakReportService service;

  public LeakReportController(LeakReportService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list() { return service.list(); }

  /** 漏损核查队列：BURST→TRACE、管段风险高→低、同分看巡检超期。 */
  @GetMapping("/verification-queue")
  public List<Map<String, Object>> verificationQueue() { return service.verificationQueue(); }

  /** 漏损上报。 */
  @PostMapping
  public Map<String, Object> report(@RequestBody LeakReportPayload payload) { return service.report(payload); }

  /** 漏损核实：VERIFIED / REJECTED。 */
  @PostMapping("/{id}/verify")
  public Map<String, Object> verify(@PathVariable Long id, @RequestBody VerifyPayload payload) {
    return service.verify(id, payload);
  }
}
