package com.generated.waterLeak.controllers;

import com.generated.waterLeak.routes.RepairOrderRoutes;
import com.generated.waterLeak.services.RepairOrderService;
import com.generated.waterLeak.types.BizException;
import com.generated.waterLeak.types.RepairOrderAcceptPayload;
import com.generated.waterLeak.types.RepairOrderPayload;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RepairOrderRoutes.PATH)
public class RepairOrderController {
  private static final Logger log = LoggerFactory.getLogger(RepairOrderController.class);

  private final RepairOrderService service;

  public RepairOrderController(RepairOrderService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list();
  }

  @GetMapping(RepairOrderRoutes.CREWS)
  public List<Map<String, Object>> crews() {
    return service.listCrews();
  }

  /** 派工：BURST 未核实不能派工；同一维修队同一天只能有一张未关闭维修单。 */
  @PostMapping(RepairOrderRoutes.DISPATCH)
  public Map<String, Object> dispatch(@Valid @RequestBody RepairOrderPayload payload) {
    try {
      return service.dispatch(payload);
    } catch (BizException e) {
      log.warn("repair dispatch rejected: code={} message={}", e.getCode(), e.getMessage());
      throw e;
    }
  }

  /** 领料 + 验收：同一事务，验收失败不得扣料或关单。 */
  @PostMapping(RepairOrderRoutes.ACCEPT)
  public Map<String, Object> accept(@PathVariable Long id, @Valid @RequestBody RepairOrderAcceptPayload payload) {
    try {
      return service.accept(id, payload);
    } catch (BizException e) {
      log.warn("repair accept rejected: id={} code={} message={}", id, e.getCode(), e.getMessage());
      throw e;
    }
  }
}
