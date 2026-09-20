package com.generated.waterLeak.constants;

public final class LogTemplates {
  public static final String CREATE = "create";
  public static final String UPDATE = "update";
  public static final String STATUS = "status";
  public static final String EXPORT = "export";
  /** 漏损上报：reportId、pointId、level */
  public static final String LEAK_REPORT = "漏损上报 reportId=%d pointId=%d level=%s";
  /** 漏损核实：reportId、verifyStatus */
  public static final String LEAK_VERIFY = "漏损核实 reportId=%d verifyStatus=%s";
  /** 派工：orderId、reportId、crewId、workDate */
  public static final String DISPATCH = "派工 orderId=%d reportId=%d crewId=%d workDate=%s";
  /** 派工被阻止：reportId、code、reason */
  public static final String DISPATCH_BLOCKED = "派工被阻止 reportId=%d code=%s reason=%s";
  /** 领料：orderId、materialCode、quantity */
  public static final String PICKUP = "领料 orderId=%d material=%s quantity=%d";
  /** 验收通过：orderId、cost */
  public static final String ACCEPT = "验收通过 orderId=%d cost=%s";
  /** 验收失败回滚：orderId */
  public static final String ACCEPT_FAILED = "验收失败已回滚 orderId=%d";
}
