package com.generated.waterLeak.constants;

/**
 * 审计日志模板。每个实体至少 4 条，所有写操作必须记录；
 * 字段变更时需同步修改本文件与调用处。
 */
public final class LogTemplates {
  // PipelineSegment
  public static final String SEGMENT_CREATE = "管网分段创建 segment=%s";
  public static final String SEGMENT_UPDATE = "管网分段更新 segment=%s";
  public static final String SEGMENT_RISK_CHANGE = "管网分段风险等级变更 segment=%s risk=%s";
  public static final String SEGMENT_EXPORT = "管网分段导出 segment=%s";
  // InspectionPoint
  public static final String POINT_CREATE = "巡检点创建 point=%s";
  public static final String POINT_UPDATE = "巡检点更新 point=%s";
  public static final String POINT_STATUS = "巡检点状态变更 point=%s status=%s";
  public static final String POINT_OVERDUE = "巡检点超期 point=%s overdueDays=%d";
  // LeakReport
  public static final String LEAK_CREATE = "漏损上报 report=%d level=%s point=%d";
  public static final String LEAK_VERIFY = "漏损核实 report=%d result=%s";
  public static final String LEAK_BLOCK = "漏损派工被阻塞 report=%d reason=%s";
  public static final String LEAK_EXPORT = "漏损报告导出 report=%d";
  // RepairOrder
  public static final String ORDER_DISPATCH = "维修派工 order=%d report=%d crew=%d date=%s";
  public static final String ORDER_DISPATCH_CONFLICT = "维修派工冲突 crew=%d date=%s 当日已存在未关闭维修单";
  public static final String ORDER_ACCEPT = "维修验收通过 order=%d cost=%s";
  public static final String ORDER_ACCEPT_FAILED = "维修验收失败回滚 order=%d reason=%s";
  // MaterialUsage
  public static final String MATERIAL_ISSUE = "材料领用 order=%d material=%s qty=%s warehouse=%s";
  public static final String MATERIAL_RETURN = "材料退回 order=%d material=%s qty=%s";
  public static final String MATERIAL_STOCK_DEDUCT = "库存扣减 material=%s warehouse=%s qty=%s";
  public static final String MATERIAL_EXPORT = "材料流水导出 order=%d";

  private LogTemplates() {}
}
