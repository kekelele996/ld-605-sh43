package com.generated.waterLeak.constants;

/** 漏损等级。核查列表按 BURST → MAJOR → MINOR → TRACE 排序（rank 越小越靠前）。 */
public enum LeakLevel {
  TRACE, MINOR, MAJOR, BURST;

  public static int rank(String level) {
    if (level == null) return values().length;
    return switch (level) {
      case "BURST" -> 0;
      case "MAJOR" -> 1;
      case "MINOR" -> 2;
      case "TRACE" -> 3;
      default -> values().length;
    };
  }
}
