package com.generated.waterLeak.controllers;

import com.generated.waterLeak.routes.RepairOrderRoutes;
import com.generated.waterLeak.services.RepairOrderService;
import com.generated.waterLeak.types.MaterialUsagePayload;
import com.generated.waterLeak.types.RepairOrderPayload;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RepairOrderRoutes.PATH)
public class RepairOrderController {
  private final RepairOrderService service;

  public RepairOrderController(RepairOrderService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list() { return service.list(); }

  /** 派工：BURST 未核实拒绝；同一维修队同一天只允许一张未关闭维修单。 */
  @PostMapping("/dispatch")
  public Map<String, Object> dispatch(@RequestBody RepairOrderPayload payload) { return service.dispatch(payload); }

  /** 领料 + 验收：同一事务，验收失败不扣料、不关单。 */
  @PostMapping("/{id}/accept")
  public Map<String, Object> accept(@PathVariable Long id, @RequestBody MaterialUsagePayload payload) {
    return service.accept(id, payload);
  }
}
