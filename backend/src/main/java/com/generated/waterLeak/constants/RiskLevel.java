package com.generated.waterLeak.constants;

/** 管段风险等级。核查列表按 EXTREME → HIGH → MEDIUM → LOW 排序（rank 越小越靠前）。 */
public enum RiskLevel {
  LOW, MEDIUM, HIGH, EXTREME;

  public static int rank(String level) {
    if (level == null) return values().length;
    return switch (level) {
      case "EXTREME" -> 0;
      case "HIGH" -> 1;
      case "MEDIUM" -> 2;
      case "LOW" -> 3;
      default -> values().length;
    };
  }
}
